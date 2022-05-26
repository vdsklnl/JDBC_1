package learn.DAOQueryRunner;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author vdsklnl
 * @create 2022-05-25 10:46
 * @Description 使用dbutils中QueryRunner进行设置对数据库通用操作DAO
 */

public abstract class BaseDAO<T> {

    private QueryRunner runner = new QueryRunner();

    //定义接受泛型类型变量
    private Class<T> type;

    //获取T的class对象，获取泛型的类型，泛型在被子类继承时确定
    public BaseDAO() {
        //获取子类的类型
        Class clazz = this.getClass();
        //获取父类的类型
        //getGenericSuperclass()用来获取当前类继承父类类型
        //ParameterizedType表示的是带泛型的类型
        ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();
        //获取具体泛型类型 getActualTypeArguments()
        //返回Type类型数组，记录父类全部泛型类型
        Type[] arguments = genericSuperclass.getActualTypeArguments();
        //获取具体的泛型类型
        this.type = (Class<T>) arguments[0];
    }

    /**
     * 通用增删改操作
     */
    public int update(Connection conn, String sql, Object ... args) {
        int count = 0;
        try {
            count = runner.update(conn, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 通用查询一条记录
     */
    public T getBean(Connection conn, String sql, Object ... args) {
        T t = null;
        try {
            t = runner.query(conn, sql, new BeanHandler<>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 通用查询多条记录
     */
    public List<T> getBeanList(Connection conn, String sql, Object ... args) {
        List<T> list = null;
        try {
            list = runner.query(conn, sql, new BeanListHandler<>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通用查询特殊记录
     */
    public Object getValue(Connection conn, String sql, Object ... args) {
        Object value = null;
        try {
            value = runner.query(conn, sql, new ScalarHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

}
