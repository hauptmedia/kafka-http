# Kafka HTTP endpoint

## Build instructions

Just run `sbt assembly` to build a standalone uber-jar file which can be used for deployments. 

```bash
sbt assembly
```


## Example usage

You can run the jar without any parameters. It will then start an embedded Kafka/ZooKeeper instance and create
a producer and consumer endpoint for the topic `test`. 

```bash
java -jar kafka-http-assembly-0.1.jar
```

You can override the default configuration by specifying Java system properties. You can find the available
configuration options at the bottom of this page.

```bash
java -Dconsumer.topics=topic1,topic2 \
     -Dproducer.topics=topic1 \
     -Dhttp.port=8888 \
     -jar kafka-http-assembly-0.1.jar
```

## Post messages to given topic

```bash
curl -X POST \
-d data='[{"key":"0","message":"Message 1"},{"key":"1","message":"Message 2"}]' \
http://localhost:8080/topic/test/
```

## Get messages for a topic

You can limit the amount of message you want to receive if you specify the `limit` parameter. 
 
```bash
curl "http://localhost:8080/topic/test/?limit=2"
```

## Benchmarking

You can utilize the benchmark scripts from the `scripts/` directory. Please make sure
that you have command line utility `ab` installed.
 
```bash
cd scripts
./bench_post_messages test
./bench_get_messages test
```


## Available configuration keys

`http.addr`                     - IP address where the http connector should bind to
`http.port`                     - Port where the http connector should bind to

`consumer.zookeeper.connect`    - Specifies the zookeeper connection string in the form hostname:port where host and port are the host and port of a zookeeper server (e.g. `hostname1:port1,hostname2:port2`)
`consumer.group.id`             - A string that uniquely identifies the group of consumer processes to which this consumer belongs
`consumer.topics`               - Comma seperated list of topics where a consumer endpoint should be registered (GET /topic/name)


`producer.metadata.broker.list` - Defines where the producer can find a one or more brokers to determine the leader for each topic (e.g. `broker1:9092,broker2:9092`)
`producer.topics`               - Comma seperated list of topics where a producer endpoint should be registered (POST /topc/name)

## Docker container

Have a look at https://github.com/hauptmedia/docker-kafka-http for a dockerized version of this application