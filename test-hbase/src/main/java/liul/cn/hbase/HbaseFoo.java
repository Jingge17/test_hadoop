package liul.cn.hbase;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

public class HbaseFoo {
	// 与HBase数据库的连接对象
	Connection connection;
	 // 数据库元数据操作对象
	Admin admin;
	
	@Before
    public void setUp() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		 // 设置连接参数：HBase数据库所在的主机IP
        conf.set("hbase.zookeeper.quorum", "master1");
     // 设置连接参数：HBase数据库使用的端口
        conf.set("hbase.zookeeper.property.clientPort", "2181");
     // 取得一个数据库连接对象
        connection = ConnectionFactory.createConnection(conf);
        // 取得一个数据库元数据操作对象
        admin = connection.getAdmin();
	}
	@Test
	public void createTable() throws Exception{
		  // 数据表表名
        String tableNameString = "hb-java-foo";
        // 新建一个数据表表名对象
        TableName tableName = TableName.valueOf(tableNameString);
     // 数据表描述对象
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        
     // 列族描述对象
        HColumnDescriptor family= new HColumnDescriptor("column11");

        // 在数据表中新建一个列族
        hTableDescriptor.addFamily(family);

        // 新建数据表
        admin.createTable(hTableDescriptor);
        
	}

	 @Test
     public void queryTable() throws Exception{
        // 取得数据表对象
        Table table = connection.getTable(TableName.valueOf("hb-java-foo"));

        // 取得表中所有数据
        ResultScanner scanner = table.getScanner(new Scan());

        // 循环输出表中的数据
        for (Result result : scanner) {

            byte[] row = result.getRow();
            System.out.println("row key is:" + new String(row));

            List<Cell> listCells = result.listCells();
            for (Cell cell : listCells) {

                byte[] familyArray = cell.getFamilyArray();
                byte[] qualifierArray = cell.getQualifierArray();
                byte[] valueArray = cell.getValueArray();

                System.out.println("row value is:" + new String(familyArray) + new String(qualifierArray) 
                                                                             + new String(valueArray));
            }
        }

    }
	 
	 
	 /**
	     * 新建列族
	     */
	    @Test
	    public void addColumnFamily() throws Exception{
	        // 取得目标数据表的表名对象
	        TableName tableName = TableName.valueOf("hb-java-foo");

	        // 创建列族对象
	        HColumnDescriptor columnDescriptor = new HColumnDescriptor("column22");

	        // 将新创建的列族添加到指定的数据表
	        admin.addColumn(tableName, columnDescriptor);
	    }
	    
	    
	    /**
	     * 插入数据
	     */
	    @Test
	    public void insert() throws Exception{
	        // 取得一个数据表对象
	        Table table = connection.getTable(TableName.valueOf("hb-java-foo"));
	        // 生成数据集合
	        Put put = new Put(Bytes.toBytes("id11"));
	            put.addColumn(Bytes.toBytes("column11"),Bytes.toBytes("name"), Bytes.toBytes("namevalue"));

	        // 将数据集合插入到数据库
	        table.put(put);
	    }
}
