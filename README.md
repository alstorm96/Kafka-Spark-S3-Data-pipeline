# Kafka-Spark-S3-Data-pipeline

This is a project of building real time data pipeline using Kafka Spark and AWS S3.

Following is the data source which is in streaming fashion basically webserver logs for an e-commerce site.

111.96.50.219 - - [09/May/2020:11:36:47 -0800] "GET /departments HTTP/1.1" 200 704 "-" "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0"
147.86.58.203 - - [09/May/2020:11:36:48 -0800] "GET /departments HTTP/1.1" 503 374 "-" "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36"
83.108.204.37 - - [09/May/2020:11:36:49 -0800] "GET /add_to_cart/205 HTTP/1.1" 200 1227 "-" "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0"
212.170.206.232 - - [09/May/2020:11:36:50 -0800] "GET /departments HTTP/1.1" 200 1247 "-" "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36"
217.204.72.122 - - [09/May/2020:11:36:51 -0800] "GET /department/team%20sports/products HTTP/1.1" 200 237 "-" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0"

This is how the data is generating ever minute. This data is ingested into a Kafka Topic and consumed using Spark Structured Streaming. Transformations are done in spark to get department traffic per minute. The result is stored in the AWS S3 bucket.


Make sure your kafka is installed and configured properly.

1.Start Zookeeper service
zookeeper-server-start.sh \
  -daemon /home/dilip/kafka/config/zookeeper.properties
 
2. Start Kafka
 kafka-server-start.sh \
  -daemon /home/dilip/kafka/server.properties

3. Create kafka topic and validate
kafka-topics.sh --zookeeper localhost:2181 \
  --create \
  --topic dilip \
  --partitions 2 \
  --replication-factor 1

kafka-topics.sh --zookeeper localhost:2181 \
  --list \
  --topic dilip 

kafka-topics.sh --zookeeper localhost:2181 \
  --describe \
  --topic dilip
  
4. Produce Messages to Kafka Topic
tail_logs.sh | kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic dilip
  
 Once your kafka is up and running. Start your spark Cluster and run using spark-submit. 
 This is a new branch to create pull request.
