package learn.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author vdsklnl
 * @create 2022-05-24 10:57
 * @Description
 */

public class DruidTest {

    @Test
    public void testConnection() throws Exception {

        Properties pros = new Properties();
        pros.load(new FileInputStream(new File("src\\druid.properties")));
        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);

    }

}
