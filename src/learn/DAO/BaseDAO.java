package learn.DAO;

import com.sun.jdi.VMDisconnectedException;
import learn.transaction.User;
import learn.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vdsklnl
 * @create 2022-05-23 12:53
 * @Description 封装对于数据表的基础操作
 *              DAO: data(base) access object
 *              数据(库)访问对象
 */

public abstract class BaseDAO {

    //针对事务通用增删改操作
    public int update(Connection conn, String sql, Object ... args) {
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

    //体现多态性，返回接口具体实现类对象，调用相应实现类重写方法(考虑事务)
    public <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object ... args) {
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
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
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

    public <T> T getValue(Connection conn, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            if(rs.next()){
                return (T) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }

}
