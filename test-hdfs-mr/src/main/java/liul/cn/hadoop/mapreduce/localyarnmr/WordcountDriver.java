package liul.cn.hadoop.mapreduce.localyarnmr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 * 判断单词个数
 */
/* 集群上运行mapreduce
 * 拖到linux hadoop集群上执行命令：hadoop jar mr-foo1.jar /hdfs-foo1/hdfs-test1.txt.copy /hdfs-foo4
 */
public class WordcountDriver {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			args = new String[2];
			args[0] = "hdfs://master1:9000/hdfs-foo1/hdfs-test1.txt.copy";
			args[1] = "hdfs://master1:9000/hdfs-foo5";
		}
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master1:9000"); 
		conf.set("mapreduce.framework.name", "yarn");
		conf.set("yarn.resoucemanager.hostname", "master1");
		conf.set("HADOOP_USER_NAME", "root");
		Job job = Job.getInstance(conf);
		job.setJarByClass(WordcountDriver.class);
		job.setMapperClass(WordcountMapper.class);
		job.setReducerClass(WordcountReducer.class);
		//指定mapper输出数据的kv类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		//指定最终输出的数据的kv类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		//指定输入输出文件所在目录
		FileInputFormat.setInputPaths(job,new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		//等待完成
		System.out.println(job.waitForCompletion(true));
	}

}
