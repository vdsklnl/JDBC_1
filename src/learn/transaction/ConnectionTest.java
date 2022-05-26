package learn.transaction;

import learn.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author vdsklnl
 * @create 2022-05-22 17:15
 * @Description
 */

/*
 * 数据库事务：一组逻辑单元，使数据从一种状态变换为另一种状态
 *              ->一组逻辑操作单元：一个或多个DML操作
 *
 * 事务处理原则：保证所有事务都作为一个工作单元来执行，即使出现
 *              了故障，都不能改变这种执行方式。当在一个事务中
 *              执行多个操作时，要么所有的事务都被提交(commit)，
 *              那么这些修改就永久地保存下来；要么数据库管理系统
 *              将放弃所作的所有修改，整个事务回滚(rollback)
 *              到最初状态。
 *
 * 数据一旦提交，无法回滚
 *
 * 数据自动提交：
 *        DDL:一旦执行，就提交(commit)
 *        DML:默认提交，但可以设置 Set autocommit = false；来取消默认
 *        在关闭数据库连接时，自动提交(conn.close()会导致提交，在事务中避免)
 */

public class ConnectionTest {

    //针对user_table
    //AA用户转账100给BB,整个过程为事务，需同时完成或同时失败
    @Test
    public void testUpdate() {

        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");

    }

    //通用增删改操作(连接数据库中任意表)
    //sql占位符个数与可变形参个数相同
    public int update(String sql,Object ... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取连接
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }

    @Test
    public void testUpdateTransaction() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            //取消数据自动提交
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            int aa = update(conn, sql1, "AA");

            //模拟网络异常
//            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            int bb = update(conn, sql2, "BB");

            System.out.println("转账成功！");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            if(conn != null) {
                //针对数据库连接池，为防止其他人拿到不自动提交连接
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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

}
