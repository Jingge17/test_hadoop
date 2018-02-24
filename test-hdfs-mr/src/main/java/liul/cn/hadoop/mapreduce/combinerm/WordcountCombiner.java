package liul.cn.hadoop.mapreduce.combinerm;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
  * combiner其实就是一个在maper端执行的reducer，在不影响结果的情况下可以减少合并排序和网络传输数据给reducer端的时间
 */
public class WordcountCombiner extends Reducer<Text, IntWritable, Text, IntWritable>{

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

		int count=0;
		for(IntWritable v: values){
			
			count += v.get();
		}
		
		context.write(key, new IntWritable(count));
		
		
	}
	
	
}
