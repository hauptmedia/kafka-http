# Kafka HTTP endpoint

## Build instructions

Just run `sbt assembly` to build a standalone uber-jar file which can be used for deployments. 

```bash
sbt assembly
```


## Example usage

You can run the jar without any parameters. It will then start the an embedded Kafka/ZooKeeper instance and create
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


You can get messages via `http://127.0.0.1:8080/topic/topic1/?limit=25`

