package liul.cn.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerDemo {

	public static void main(String[] args) {
		Properties props = new Properties();
        props.put("bootstrap.servers", "smaster1:19092");
        props.put("metadata.broker.list","smaster1:19092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        // 发送业务消息
        // 读取文件 读取内存数据库 读socket端口
        for (int i = 1; i <= 100; i++) {
              try {
                    Thread.sleep(500);
              } catch (InterruptedException e) {
                    e.printStackTrace();
              }
              producer.send(new ProducerRecord<String, String>("wordcount","msg="+i));
        }

	}

}
