package com.mayihi.store.dao.daoImpl;

import com.mayihi.store.Util.JDBCUtils;
import com.mayihi.store.Util.PageModel;
import com.mayihi.store.dao.OrderDao;
import com.mayihi.store.dao.ProductDao;
import com.mayihi.store.domain.Order;
import com.mayihi.store.domain.OrderItem;
import com.mayihi.store.domain.Product;
import com.mayihi.store.domain.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDaoImpl implements OrderDao {


    @Override
    public void saveOrder(Connection conn, Order order) throws Exception {
        QueryRunner runner = new QueryRunner();
        Object[] params = {order.getOid(), order.getOrdertime(), order.getTotal(), order.getState(), order.getAddress(), order.getName(), order.getTelephone(), order.getUser().getUid()};
        runner.update(conn, "insert into orders values(?,?,?,?,?,?,?,?)", params);
    }

    @Override
    public void saveOrderItems(Connection conn, OrderItem orderItem) throws Exception {
        QueryRunner runner = new QueryRunner();
        Object[] params = {orderItem.getItemid(), orderItem.getQuantity(), orderItem.getTotal(), orderItem.getProduct().getPid(), orderItem.getOrder().getOid()};
        runner.update(conn, "insert into orderitem values(?,?,?,?,?)", params);
    }


    public PageModel showMyOrdersWithPage(User user, int page) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        List<Order> list = runner.query("select * from orders where uid = ? limit ?,?", new BeanListHandler<Order>(Order.class), user.getUid(), (page - 1) * PageSize, PageSize);
        List<Order> orderList = new ArrayList<>();
        //列表中每行表示一个order
        for (Order order : list) {
            //遍历赋予order中的orderItem项，查找所有属于该order的order项，添加到list中，将该list与order关联
//            limit offset,num  : (page-1)*num
            List<Map<String, Object>> mapList = runner.query("select * from orderitem o,product p where o.pid = p.pid and oid = ?",
                    new MapListHandler(), order.getOid());
            setOrderItems(order, mapList);
//        最后将order放入order列表中
            orderList.add(order);
        }
//        将order列表放入PageModel中
//        查询一下所有的order条数
        Long res = (Long) runner.query("select count(*) from orders where uid = ?", new ScalarHandler(), user.getUid());
        PageModel pageModel = new PageModel(page, res.intValue(), PageSize);
        pageModel.setRecords(orderList);
        pageModel.setUrl("OrderServlet?method=showMyOrders");
        return pageModel;
    }

    @Override
    public List<OrderItem> queryOrderItemsByOid(String oid) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        List<OrderItem> items = runner.query("select * from orderitem where oid = ?", new ResultSetHandler<List<OrderItem>>() {
            @Override
            public List<OrderItem> handle(ResultSet resultSet) throws SQLException {
                OrderItem orderItem = new OrderItem();

                return null;
            }
        }, oid);

        return null;
    }

    @Override
    public Order queryOrderByOid(String oid) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from orders where oid = ?";
        Order order = runner.query(sql, new BeanHandler<Order>(Order.class), oid);

        List<Map<String, Object>> mapList = runner.query("select * from orderitem o,product p where o.pid = p.pid and oid = ?",
                new MapListHandler(), order.getOid());
        setOrderItems(order, mapList);
        return order;
    }

    @Override
    public int updateOrderByOrder(Order order) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update orders set ordertime=?,total=?,state=?,address=?,name=?,telephone=? where oid = ?";
        Object[] params = {order.getOrdertime(), order.getTotal(), order.getState(), order.getAddress(), order.getName(), order.getTelephone(), order.getOid()};
        return runner.update(sql, params);
    }

    /**
     * 这个方法是多余的，其实可以查出原先的order，再赋予一些特定的值，比如state，再使用updateOrderByOrder来进行更新
     * @param oid
     * @param state
     * @return
     * @throws Exception
     */
    @Override
    public int updateOrderStateByOid(String oid, int state) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update orders set state = ? where oid = ?";
        return runner.update(sql, state, oid);
    }

    @Override
    public int queryOrdersCount() throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        Long query = (Long) runner.query("select count(*) from orders", new ScalarHandler());
        return query.intValue();
    }

    @Override
    public List<Order> showOrdersWithPage(int page) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        return runner.query("select * from orders limit ?,?", new BeanListHandler<Order>(Order.class), (page - 1) * ProductDao.PageSize, ProductDao.PageSize);
    }

    @Override
    public List<Order> showOrdersWithPage(int page, int st) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        return runner.query("select * from orders where state=? limit ?,?", new BeanListHandler<Order>(Order.class), st,(page - 1) * ProductDao.PageSize, ProductDao.PageSize);
    }

    @Override
    public int queryOrdersCount(int st) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        Long query = (Long) runner.query("select count(*) from orders where state = ?", new ScalarHandler(),st);
        return query.intValue();
    }

    private void setOrderItems(Order order, List<Map<String, Object>> mapList) throws Exception {
        for (Map<String, Object> item : mapList) {
            OrderItem orderItem = new OrderItem();
            Product product = new Product();
//                默认的BeanUtils的时间格式处理有问题，需要加三行：
            // 由于BeanUtils将字符串"1992-3-3"向user对象的setBithday();方法传递参数有问题,手动向BeanUtils注册一个时间类型转换器
            // 1_创建时间类型的转换器
            DateConverter dt = new DateConverter();
            // 2_设置转换的格式
            dt.setPattern("yyyy-MM-dd");
            // 3_注册转换器
            ConvertUtils.register(dt, java.util.Date.class);
            BeanUtils.populate(orderItem, item);
            BeanUtils.populate(product, item);
            //将product与orderItem关联
            orderItem.setProduct(product);
//                将orderItem添加到order的orderItem列表中order。orderItem
            order.getOrderItems().add(orderItem);

        }
    }
}