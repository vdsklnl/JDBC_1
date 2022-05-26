package learn.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author vdsklnl
 * @create 2022-05-20 15:58
 * @Description 封装获取连接和基本操作
 */

public class JDBCUtils {
    //数据库连接池只需创建一个
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("hello_c3p0");

    //c3p0获取连接
    public static Connection getC3P0Conn() throws Exception {
        Connection conn = cpds.getConnection();
        return conn;
    }

    //使用静态代码块处理
    private static DataSource source;
    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //dbcp数据库连接
    public static Connection getDbcpConn() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }



    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            source = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //druid数据库连接
    public static Connection getDruidConn() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }

    //获取数据库连接
    public static Connection getConnection() throws Exception {

        //读取配置文件,ClassLoader获取系统类加载器
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    //关闭资源
    public static void closeResource(Connection conn, Statement ps) {
        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //dbutils-1.3.jar包中，DbUtils类可以帮助关闭资源
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
//        if(ps != null){
//            try {
//                ps.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if(conn != null){
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if(rs != null){
//            try {
//                rs.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);
    }

}
