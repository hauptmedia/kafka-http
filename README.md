# Kafka HTTP endpoint

Example usage:

Run with 2 topics and use the embedded Kafka/ZooKeeper

```bash
java -jar -Dtopics=topic1,topic2 kafka-http-assembly-0.1.jar

cd scripts
./bench_post_messages topic1
./bench_get_messages topic2
```

You can get messages via `http://127.0.0.1:8080/topic/topic1/?limit=25`

