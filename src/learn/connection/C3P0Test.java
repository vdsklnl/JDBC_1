package learn.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

/**
 * @author vdsklnl
 * @create 2022-05-23 17:17
 * @Description C3P0数据库连接池测试
 */

public class C3P0Test {

    //方式一：
    @Test
    public void testGetConnection() throws Exception {

        //获取C3P0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8" );
        cpds.setUser("root");
        cpds.setPassword("lgbtqiapkdx");

        //设置参数对数据库连接池进行管理
        //设置初始数据库连接池连接数
        cpds.setInitialPoolSize(1);

        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁C3P0数据库连接池
        DataSources.destroy(cpds);

    }

    //方式二(使用配置文件)：
    @Test
    public void testConnection1() throws Exception {

        ComboPooledDataSource cpds = new ComboPooledDataSource("hello_c3p0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);

    }

}
