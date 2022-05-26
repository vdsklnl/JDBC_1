package learn.DAOupdate.junit;

import learn.DAOupdate.CustomerDAOImpl;
import learn.bean.Customer;
import learn.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author vdsklnl
 * @create 2022-05-23 15:03
 * @Description
 */

class CustomerDAOImplTest {

    private CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1999-04-04");
            Customer cust = new Customer(1, "王呆呆", "wang@gmail.com", new Date(date.getTime()));
            dao.insert(conn, cust);
            System.out.println("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn, 18);
            System.out.println("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1999-04-04");
            Customer cust = new Customer(22, "王晓晗", "wang@gmail.com", new Date(date.getTime()));
            dao.update(conn, cust);
            System.out.println("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getDruidConn();
            Customer cust = dao.getCustomerById(conn, 20);
            System.out.println(cust);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getDbcpConn();
            List<Customer> list = dao.getAll(conn);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            long count = dao.getCount(conn);
            System.out.println("表中记录数为：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    void getMaxDate() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxDate = dao.getMaxDate(conn);
            System.out.println("最大生日为：" + maxDate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}