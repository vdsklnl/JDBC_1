package learn.DAOQueryRunner.junit;

import learn.DAOQueryRunner.CustomerDAOImpl;
import learn.bean.Customer;
import learn.utils.MyDbUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vdsklnl
 * @create 2022-05-25 14:39
 * @Description
 */

class CustomerDAOImplTest {

    private CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    void insert() {
        Connection conn = null;
        try {
            conn = MyDbUtils.getConn();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1982-06-28");
            Customer cust = new Customer(1, "叶凡", "yefan@gmail.com", new Date(date.getTime()));
            dao.insert(conn, cust);
            System.out.println("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDbUtils.closeResource(conn);
        }
    }

    @Test
    void deleteById() {
        Connection dbcpConn = null;
        try {
            dbcpConn = MyDbUtils.getDbcpConn();
            dao.deleteById(dbcpConn, 8);
            System.out.println("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDbUtils.closeResource(dbcpConn);
        }
    }

    @Test
    void update() {
        Connection druidConn = null;
        try {
            druidConn = MyDbUtils.getDruidConn();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1955-04-09");
            Customer cust = new Customer(24,"叶遮天","yezhetian@qq.com", new Date(date.getTime()));
            int update = dao.update(druidConn, cust);
            if(update > 0) {
                System.out.println("修改成功！");
            } else {
                System.out.println("修改失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDbUtils.closeResource(druidConn);
        }
    }

    @Test
    void getCustomerById() {
        Connection conn = null;
        try {
            conn = MyDbUtils.getConn();
            Customer customer = dao.getCustomerById(conn, 20);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDbUtils.closeResource(conn);
        }
    }

    @Test
    void getAll() {
        Connection dbcpConn = null;
        try {
            dbcpConn = MyDbUtils.getDbcpConn();
            List<Customer> list = dao.getAll(dbcpConn);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDbUtils.closeResource(dbcpConn);
        }
    }

    @Test
    void getCount() {
        Connection druidConn = null;
        try {
            druidConn = MyDbUtils.getDruidConn();
            long count = dao.getCount(druidConn);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDbUtils.closeResource(druidConn);
        }
    }

    @Test
    void getMaxDate() {
        Connection conn = null;
        try {
            conn = MyDbUtils.getConn();
            Date maxDate = dao.getMaxDate(conn);
            System.out.println(maxDate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDbUtils.closeResource(conn);
        }
    }

}