package learn.DAOQueryRunner;

import learn.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author vdsklnl
 * @create 2022-05-25 14:21
 * @Description
 */

public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO {

    @Override
    public void insert(Connection conn, Customer cust) {
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id = ?";
        update(conn, sql, id);
    }

    @Override
    public int update(Connection conn, Customer cust) {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        int update = update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
        return update;
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = getBean(conn, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        List<Customer> list = getBeanList(conn, sql);
        return list;
    }

    @Override
    public long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        long value = (long) getValue(conn, sql);
        return value;
    }

    @Override
    public Date getMaxDate(Connection conn) {
        String sql = "select max(birth) from customers";
        Date value = (Date) getValue(conn, sql);
        return value;
    }
}
