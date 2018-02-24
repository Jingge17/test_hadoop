package liul.cn.hadoop.foo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;



public class HdfsClientFoo {
	FileSystem fs=null;
	Configuration conf=null;
	@Before
	public void init() throws Exception{
		conf = new Configuration();
		
		/**
		 * 参数优先级： 1、客户端代码中设置的值 2、classpath下的用户自定义配置文件 3、然后是服务器的默认配置
		 */
		//conf.set("fs.defaultFS", "hdfs://master1:9000");
		//conf.set("dfs.replication", "2");//副本数
		//conf.set("dfs.block.size","64m");//文件分块的块大小
		fs = FileSystem.get(new URI("hdfs://master1:9000"),conf,"root");
		
	}
	//测试上传
    @Test
    public void testUpload() throws Exception  {
    	fs.copyFromLocalFile(new Path("F:/hdfs-test1.txt"), new Path("/hdfs-foo1/hdfs-test1.txt.copy"));
        fs.close();
    }
    //测试下载
    @Test
    public void testDownload() throws Exception  {
		//参数4：是否使用原生的java操作本地文件系统;如果为false，则使用winutils；如果为true，则用java操作
    	fs.copyToLocalFile(new Path("/hdfs-foo/id_rsa.pub"), new Path("f:/id_rsa.pub.copy"));
    	fs.close();
    }
    //获取配置信息
    @Test
    public void testConf() {
    	Iterator<Entry<String, String>> iterator=conf.iterator();
    	while(iterator.hasNext()) {
    		Entry<String, String> entry=iterator.next();
    		System.out.println(entry.getValue()+"--"+entry.getValue());
    	}
    }
    //创建目录
    @Test
    public void testMkdir() throws Exception{
    	System.out.println(fs.mkdirs(new Path("/hdfs-foo2")));
    }
    //删除目录
    @Test
    public void testDelete()  throws Exception{
    	System.out.println(fs.delete(new Path("/hdfs-foo2"),true));
    } 
    //列出所有文件（路径、域名、名字等）
    @Test
    public void testListAll()  throws Exception{
    	FileStatus[] listStatus =fs.listStatus(new Path("/"));
    	for (FileStatus fileStatus : listStatus) {
			System.out.println(fileStatus.getPath()+"-----"+fileStatus.toString());
		}
    	//递归找到所有的文件
    	RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
    	while(listFiles.hasNext()) {
    		LocatedFileStatus next = listFiles.next();
    		String name = next.getPath().getName();
    		Path path = next.getPath();
    		System.out.println(name+"----"+path);
    	}
    }
    //用io流下载文件
    @Test
    public void testDownloadFileToLocal() throws Exception{
    	FSDataInputStream in = fs.open(new Path("/hdfs-foo/id_rsa.pub"));
    	FileOutputStream out = new FileOutputStream("F:/id_rsa.pub");
    	IOUtils.copyBytes(in, out, 4096);
    }
  //用io流上传文件
    @Test
    public void testUploadByStream() throws Exception{
    	FSDataOutputStream fsout = fs.create(new Path("/hdfs-foo3/id_rsa.pub"));
    	FileInputStream fsin = new FileInputStream("F:/id_rsa.pub");
    	IOUtils.copyBytes(fsin, fsout, 4096);
    }
    //用io流下载文件(偏移量)
    @Test
	public void testRandomAccess() throws Exception{
		
		FSDataInputStream in = fs.open(new Path("/hdfs-foo3/id_rsa.pub"));
		in.seek(30);	//流的起始偏移量
		FileOutputStream out = new FileOutputStream(new File("F:/id_rsa.pub"));
		IOUtils.copyBytes(in,out,19L,true);	//apache的工具类， 3.缓冲大小，4.是否关闭流
		
	}
    //读取block信息
    @Test
	public void testCat() throws Exception{
		FSDataInputStream in = fs.open(new Path("/hdfs-foo3/id_rsa.pub"));
		//拿到文件信息
		FileStatus[] listStatus = fs.listStatus(new Path("/hdfs-foo3/id_rsa.pub"));
		//获取这个文件的所有block的信息
		BlockLocation[] fileBlockLocations = fs.getFileBlockLocations(listStatus[0], 0L, listStatus[0].getLen());
		
		//第一个block的长度
		long length = fileBlockLocations[0].getLength();
		//第一个block的起始偏移量
		long offset = fileBlockLocations[0].getOffset();
		
		System.out.println(length);
		System.out.println(offset);
		
		//获取第一个block写入输出流
//		IOUtils.copyBytes(in, System.out, (int)length);
		byte[] b = new byte[4096];
		
		FileOutputStream os = new FileOutputStream(new File("F:/id_rsa.pub"));
		while(in.read(offset, b, 0, 4096)!=-1){
			os.write(b);
			offset += 4096;
			if(offset>length) return;
		};
		
		os.flush();
		os.close();
		in.close();
		
		
	}
    
       //获取文件系统信息(此处获取从服务器名称)
    @Test
	public void testServerFileInfo() throws Exception{
    	DatanodeInfo[] dataNodeStats = ((DistributedFileSystem) fs).getDataNodeStats();
    	for(DatanodeInfo dinfo: dataNodeStats){
			System.out.println(dinfo.getHostName());
		}
    }
    
}
