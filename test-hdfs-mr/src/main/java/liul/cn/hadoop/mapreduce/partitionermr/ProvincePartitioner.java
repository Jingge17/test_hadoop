package liul.cn.hadoop.mapreduce.partitionermr;

import java.util.HashMap;

import javax.security.auth.kerberos.KeyTab;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * partitioner根据mapper返回不同的数值将数据发送给不同的reducer
 *
 */
public class ProvincePartitioner extends Partitioner<Text, FlowBean>{

	public static HashMap<String, Integer> proviceDict = new HashMap<String, Integer>();
	static{
		proviceDict.put("136", 0);
		proviceDict.put("137", 1);
		proviceDict.put("138", 2);
		proviceDict.put("139", 3);
	}
	
	
	
	@Override
	public int getPartition(Text key, FlowBean value, int numPartitions) {
		String prefix = key.toString().substring(0, 3);
		Integer provinceId = proviceDict.get(prefix);
		
		return provinceId==null?4:provinceId;
	}



}