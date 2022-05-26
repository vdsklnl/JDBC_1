package learn.transaction;

import learn.utils.JDBCUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vdsklnl
 * @create 2022-05-21 11:39
 * @Description 使用PreparedStatement实现对于不同表的通用查询操作
 */

/* PreparedStatement优势
 * 1.PreparedStatement对sql语句预编译，保证逻辑不被更改
 * 2.可以操作Blob数据，占位符用流替代
 * 3.可以实现高效批量操作过程，减少SQL语句编写
 */

public class TransactionTest {

    //针对事务通用增删改操作
    public int update(Connection conn, String sql,Object ... args) {
        PreparedStatement ps = null;
        try {
            //生成PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //填充占位符(数据库从1开始，数组从0开始)
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            //执行操作
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            JDBCUtils.closeResource(null,ps);
        }
        return 0;
    }

    //体现多态性，返回接口具体实现类对象，调用相应实现类重写方法
    public <T> List<T> getForList(Class<T> clazz, String sql, Object ... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            //创建集合
            ArrayList<T> list = new ArrayList<>();

            //多条数据，改用while
            while(rs.next()) {
                //使用泛型生成对象,使用clazz
                T t = clazz.getDeclaredConstructor().newInstance();
                //给t对象属性赋值
                for (int i = 0; i < columnCount; i++) {
                    Object value = rs.getObject(i + 1);
                    String label = rsmd.getColumnLabel(i + 1);

                    //使用数据表对应类
                    Field field = clazz.getDeclaredField(label);
                    field.setAccessible(true);
                    field.set(t,value);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }

    //一定要避免脏读问题 level>2
    @Test
    public void testTransactionQuery() throws Exception{

        Connection conn = JDBCUtils.getConnection();
        //获取当前连接隔离等级
//        System.out.println(conn.getTransactionIsolation());

        //设置当前连接隔离等级
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        //取消自动提交数据
        conn.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);

    }

    @Test
    public void testTransactionUpdate() throws Exception{

        Connection conn = JDBCUtils.getConnection();
//        System.out.println(conn.getTransactionIsolation());

        //取消自动提交数据
        conn.setAutoCommit(false);

        String sql = "update user_table set balance = 5000 where user = ?";
        int update = update(conn, sql, "CC");
        Thread.sleep(15000);
        System.out.println(update);


    }

    //考虑事务通用查询
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object ... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if(rs.next()) {
                //使用泛型生成对象,使用clazz
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object value = rs.getObject(i + 1);
                    String label = rsmd.getColumnLabel(i + 1);

                    //使用数据表对应类
                    Field field = clazz.getDeclaredField(label);
                    field.setAccessible(true);
                    field.set(t,value);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }
}
