package liul.cn.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;  
public class WordCountTopology {

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException, InterruptedException {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new SentenceSpout(), 1);
		builder.setBolt("split", new SplitSentenceBolt(), 2).shuffleGrouping("spout");//spout分配策略shuffleGrouping(随机)
		builder.setBolt("count", new WordCountBolt(), 4).fieldsGrouping("split", new Fields("word"));//split分配策略fieldsGrouping(按字段分 相同的字段进入相同的bolt)
		Config conf = new Config();  
	    conf.setDebug(false);  
	    
	 /* 集群模式   */
        conf.setNumWorkers(2);  
        StormSubmitter.submitTopology("word-count-foo-onServerr", conf, builder.createTopology()); 
        
     /* 本地模式  
        LocalCluster cluster = new LocalCluster();  
        cluster.submitTopology("word-count-foo-onLocal", conf, builder.createTopology());  
        Thread.sleep(40000);    
        cluster.shutdown();   */
	
	}

}
