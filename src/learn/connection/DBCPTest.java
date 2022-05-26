package learn.connection;

import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author vdsklnl
 * @create 2022-05-23 21:59
 * @Description DBCP数据库连接测试
 */

public class DBCPTest {

    @Test
    public void testConnection() throws Exception {

        //创建DBCP数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql:///test?useUnicode=true&characterEncoding=utf8");
        source.setUsername("root");
        source.setPassword("lgbtqiapkdx");

        //设置数据库连接池管理相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        //...

        Connection conn = source.getConnection();
        System.out.println(conn);

    }

    //方式二(使用配置文件):推荐
    @Test
    public void testConnection1() throws Exception {

        Properties pros = new Properties();
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("src\\dbcp.properties");
//        pros.load(is);
        pros.load(new FileInputStream(new File("src\\dbcp.properties")));

        //创建dbcp数据库连接池
        DataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);

    }

}
