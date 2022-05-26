package learn.dbutils;

import com.mysql.jdbc.integration.jboss.ExtendedMysqlExceptionSorter;
import learn.bean.Customer;
import learn.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * @author vdsklnl
 * @create 2022-05-24 12:56
 * @Description common-dbutils为Apache提供开源工具类库，封装了针对数据库的增删改查操作
 */

public class QueryRunnerTest {

    //测试插入
    @Test
    public void testInsert() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDruidConn();
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            int update = runner.update(conn, sql, "黄纯靓", "huang@gmail.com", "1999-03-04");
            System.out.println(update);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    /*
     * BeanHandler:是ResultSetHandler接口实现类
     * 用于封装表中的一条记录
     */
    //测试查询
    @Test
    public void testQuery() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDbcpConn();

            String sql = "select id,name,email,birth from customers where id = ?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);

            Customer customer = runner.query(conn, sql, handler, 22);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    /*
     * BeanListHandler:是ResultSetHandler接口实现类
     * 用于封装表中的多条记录构成集合
     */
    //测试查询
    @Test
    public void testQuery1() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDbcpConn();

            String sql = "select id,name,email,birth from customers where id < ?";
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);

            List<Customer> list = runner.query(conn, sql, handler, 25);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    /*
     * MapHandler:是ResultSetHandler接口实现类
     * 对表中的一条记录以Map中Key-Value形式封装字段以及相应值
     */
    //测试查询
    @Test
    public void testQuery2() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDbcpConn();

            String sql = "select id,name,email,birth from customers where id = ?";
            MapHandler handler = new MapHandler();

            Map<String, Object> map = runner.query(conn, sql, handler, 22);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    /*
     * MapListHandler:是ResultSetHandler接口实现类
     * 对表中的多条记录以Map中Key-Value形式封装字段以及相应值
     */
    //测试查询
    @Test
    public void testQuery3() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDruidConn();

            String sql = "select id,name,email,birth from customers where id < ?";
            MapListHandler handler = new MapListHandler();

            List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 25);
            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    /*
     * ScalarHandler:是ResultSetHandler接口实现类
     * 用于查询特殊需求(如记录数，maxBirth等)
     */
    //测试查询
    @Test
    public void testQuery4() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDbcpConn();

            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();

            long count = (long) runner.query(conn, sql, handler);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    @Test
    public void testQuery5() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDruidConn();

            String sql = "select max(birth) from customers";
            ScalarHandler handler = new ScalarHandler();

            Date date = (Date) runner.query(conn, sql, handler);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

    /*
     * 自定义ResultSetHandler实现类
     */
    @Test
    public void testQuery6() {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDruidConn();

            String sql = "select id,name,email,birth from customers where id = ?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
                    if(resultSet.next()) {
                        int id = resultSet.getInt(1);
                        String name = resultSet.getString(2);
                        String email = resultSet.getString(3);
                        Date birth = resultSet.getDate(4);
                        Customer cust = new Customer(id, name, email, birth);
                        return cust;
                    }
                    return null;
                }
            };

            Customer customer = runner.query(conn, sql, handler, 20);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

    }

}
