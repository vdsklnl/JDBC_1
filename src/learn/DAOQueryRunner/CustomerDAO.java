package learn.DAOQueryRunner;

import learn.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author vdsklnl
 * @create 2022-05-23 13:08
 * @Description 使用接口规范对于customers表的常用操作
 */

public interface CustomerDAO {

    //将cust对象添加到数据库中
    void insert(Connection conn, Customer cust);

    //删除表中一条记录
    void deleteById(Connection conn, int id);

    //针对内存中cust对象去修改表中指定记录
    int update(Connection conn, Customer cust);

    //根据id查询得到对应的Customer对象
    Customer getCustomerById(Connection conn, int id);

    //查询表中所有记录构成集合
    List<Customer> getAll(Connection conn);

    //返回数据表中条目数
    long getCount(Connection conn);

    //返回表中员工最大生日
    Date getMaxDate(Connection conn);
    
}
