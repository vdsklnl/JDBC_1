package learn.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author vdsklnl
 * @create 2022-05-25 11:20
 * @Description 使用QueryRunner完成数据库通用操作
 */

public class MyDbUtils {

    /**
     * 通过数据库连接池获取数据库连接
     */

    /*
     * Druid数据库连接池
     */
    private static DataSource druidSource = null;

    static {
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            Properties pros = new Properties();
            pros.load(is);
            druidSource = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getDruidConn() throws Exception {
        Connection conn = druidSource.getConnection();
        return conn;
    }

    /*
     * DBCP数据库连接池
     */
    private static DataSource dbcpSource = null;

    static {
        try {
            InputStream ras = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            Properties properties = new Properties();
            properties.load(ras);
            dbcpSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getDbcpConn() throws Exception {
        Connection conn = dbcpSource.getConnection();
        return conn;
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConn() throws Exception {

        //加载配置文件
        InputStream ras = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(ras);

        //读取配置文件
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);
        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;

    }

    /**
     * 关闭资源操作
     */
    public static void closeResource(Connection conn) {
        DbUtils.closeQuietly(conn);
    }

    public static void closeResource(PreparedStatement ps) {
        DbUtils.closeQuietly(ps);
    }

    public static void closeResource(ResultSet rs) {
        DbUtils.closeQuietly(rs);
    }

    public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        DbUtils.closeQuietly(conn, ps, rs);
    }

}
