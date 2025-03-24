package com.revature.repos;

import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> userOrders = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {

            // set query
            String sql = "SELECT * FROM V_ORDERS WHERE status = ? ORDER BY ORDER_ID ASC";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setObject(1, status.name(), Types.OTHER);

            // execute query
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String address = rs.getString("house_number");
                address +=  " " + rs.getString("street");
                address +=  ", " + rs.getString("city");
                address +=  ", " + rs.getString("state");
                address +=  " " + rs.getString("postal_code");
                address +=  ", " + rs.getString("country");

                Order o = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getInt("address_id"),
                        rs.getDouble("subtotal"),
                        rs.getDouble("discount"),
                        rs.getDouble("total_price"),
                        OrderStatus.valueOf(rs.getString("status")),
                        address
                );

                userOrders.add(o);
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve the user orders");
            e.printStackTrace();
        }

        return userOrders;
    }

    @Override
    public List<Order> getOrdersByUser(int userId) {
        List<Order> userOrders = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {

            // set query
            String sql = "SELECT * FROM V_ORDERS WHERE USER_ID = ? ORDER BY ORDER_ID ASC";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, userId);

            // execute query
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String address = rs.getString("house_number");
                address +=  " " + rs.getString("street");
                address +=  ", " + rs.getString("city");
                address +=  ", " + rs.getString("state");
                address +=  " " + rs.getString("postal_code");
                address +=  ", " + rs.getString("country");

                Order o = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getInt("address_id"),
                        rs.getDouble("subtotal"),
                        rs.getDouble("discount"),
                        rs.getDouble("total_price"),
                        OrderStatus.valueOf(rs.getString("status")),
                        address
                );

                userOrders.add(o);
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve the user orders");
            e.printStackTrace();
        }

        return userOrders;
    }

    @Override
    public Order updateOrderStatus(int orderId, OrderStatus status) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE ORDERS SET STATUS = ? WHERE ORDER_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setObject(1, status.name(), Types.OTHER);
            ps.setInt(2,orderId);

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(orderId) : null;
        }catch (SQLException e){
            System.out.println("Could not update the status");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order create(Order obj) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO ORDERS (USER_ID, ADDRESS_ID, SUBTOTAL,DISCOUNT) values (?,?,?,?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,obj.getUserId());
            ps.setInt(2,obj.getAddressId());
            ps.setDouble(3,obj.getSubtotal());
            ps.setDouble(4,obj.getDiscount());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return getById(rs.getInt("order_id"));
        }catch (SQLException e){
            System.out.println("Could not create the user");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Order> getAll() {
        List<Order> allOrders = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {

            // SQL
            String sql = "SELECT * FROM V_ORDERS ORDER BY ORDER_ID ASC";

            // STMT
            Statement stmt = conn.createStatement();

            // Execute query
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                String address = rs.getString("house_number");
                address +=  " " + rs.getString("street");
                address +=  ", " + rs.getString("city");
                address +=  ", " + rs.getString("state");
                address +=  " " + rs.getString("postal_code");
                address +=  ", " + rs.getString("country");

                Order o = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getInt("address_id"),
                        rs.getDouble("subtotal"),
                        rs.getDouble("discount"),
                        rs.getDouble("total_price"),
                        OrderStatus.valueOf(rs.getString("status")),
                        address
                );

                allOrders.add(o);
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve the orders");
            e.printStackTrace();
        }


        return allOrders;
    }

    @Override
    public Order getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            // set query
            String sql = "SELECT * FROM V_ORDERS WHERE ORDER_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            String address = rs.getString("house_number");
            address +=  " " + rs.getString("street");
            address +=  ", " + rs.getString("city");
            address +=  ", " + rs.getString("state");
            address +=  " " + rs.getString("postal_code");
            address +=  ", " + rs.getString("country");

            return new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getInt("address_id"),
                    rs.getDouble("subtotal"),
                    rs.getDouble("discount"),
                    rs.getDouble("total_price"),
                    OrderStatus.valueOf(rs.getString("status")),
                    address
            );

        } catch (SQLException e) {
            System.out.println("Could not retrieve the product");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order update(Order obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
