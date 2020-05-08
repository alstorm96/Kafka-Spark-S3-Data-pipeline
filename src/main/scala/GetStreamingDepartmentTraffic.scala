import java.sql.Timestamp

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.transfer.TransferManager
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.functions.{col, to_timestamp, split}


object GetStreamingDepartmentTraffic {
  def main(args: Array[String]): Unit = {


    val spark = SparkSession.
      builder.appName(name = "Get Streaming Department Traffic").
      master("local").
      getOrCreate()

    spark.sparkContext.setLogLevel("Error")
    spark.conf.set("spark.sql.shuffle.partitions", "2")

    import spark.implicits._

    val lines = spark.
      readStream.
      format("kafka").
      option("kafka.bootstrap.servers", "localhost:9092").
      option("subscribe", "dilip").
      option("includeTimestamp", true).
      load().
      selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)", "timestamp").
      as[(String, String, Timestamp)]




    val departmentTraffic = lines.
      where(split(split($"value", " ")(6), "/")(1) === "department").
      withColumn("department_name", split(split($"value", " ")(6), "/")(2)).
      groupBy(
        window($"timestamp", "60 seconds"),$"department_name"
      ).agg(count("value").alias("department_count"))


    departmentTraffic.createOrReplaceGlobalTempView("sample_analysis_table")

    val query = spark.sql("select *from global_temp.sample_analysis_table where department ='Fitness'")




    val result = departmentTraffic.writeStream.
      format("com.knoldus.spark.s3").
      option("accessKey","").
      option("secretKey","").
      option("bucket","mysparkproject").
      option("path", "s3://mysparkproject/transformResult.json").
      option("fileType","json").
      trigger(Trigger.ProcessingTime("60 seconds")).
      start()

    val qres = query.writeStream.
      format("com.knoldus.spark.s3").
      option("accessKey","").
      option("secretKey","").
      option("bucket","mysparkproject").
      option("path", "s3://mysparkproject/queryresult.json").
      option("fileType","json").
      trigger(Trigger.ProcessingTime("60 seconds")).
      start()

    spark.streams.awaitAnyTermination()


  }

}
