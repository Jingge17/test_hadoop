package liul.cn.hadoop.rpc;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

public class RPCClient {

    public static void main(String[] args) throws Exception {
        MyBizable proxy = RPC.getProxy(MyBizable.class, 123456,new InetSocketAddress("127.0.0.1", 8077) , new Configuration());
        String result = proxy.doSomething("服务端");
        System.out.println(result);
        RPC.stopProxy(proxy);
    }

}