package liul.cn.hadoop.mapreduce.localyarnmr;

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
/*
 * 本地运行maperduce(无集群,用于检测mr代码正确性)
 */
public class WordcountDriverNative {
	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			args = new String[2];
			args[0] = "C:/Users/Administrator/Desktop/hdfs-test1.txt.copy";
			args[1] = "C:/Users/Administrator/Desktop/abc";
		}
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
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
