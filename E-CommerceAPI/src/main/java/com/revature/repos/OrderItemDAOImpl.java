package com.revature.repos;

import com.revature.models.OrderItem;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO{
    @Override
    public OrderItem create(OrderItem obj) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, QUANTITY, PRICE) values (?,?,?,?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,obj.getOrderId());
            ps.setInt(2,obj.getProductId());
            ps.setInt(3,obj.getQuantity());
            ps.setDouble(4,obj.getPrice());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return getById(rs.getInt("order_item_id"));
        }catch (SQLException e){
            System.out.println("Could not create the user");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<OrderItem> getAll() {
        List<OrderItem> allOrderItems = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            String sql = "SELECT * FROM V_ORDER_ITEMS ORDER BY ORDER_ID ASC, ORDER_ITEM_ID ASC";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){

                OrderItem oi = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id") ,
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("product"),
                        rs.getDouble("total"),
                        rs.getString("category")
                );

                allOrderItems.add(oi);
            }

        }catch (SQLException e){
            System.out.println("Could not retrieve orders items");
        }

        return allOrderItems;
    }

    @Override
    public OrderItem getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_ORDER_ITEMS WHERE ORDER_ITEM_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getString("product"),
                    rs.getDouble("total"),
                    rs.getString("category")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the item");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OrderItem update(OrderItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public List<OrderItem> getOrderItemsBy(int orderId) {
        List<OrderItem> allOrderItems = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_ORDER_ITEMS WHERE ORDER_ID = ? ORDER BY ORDER_ITEM_ID ASC";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, orderId);

            // execute query
            ResultSet rs = ps.executeQuery();

             while (rs.next()){

                OrderItem oi = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("product"),
                        rs.getDouble("total"),
                        rs.getString("category")
                );

                allOrderItems.add(oi);
            }

        }catch(SQLException e){
            System.out.println("Could not retrieve the order items");
            e.printStackTrace();
        }
        return allOrderItems;
    }

    @Override
    public boolean getItemByProductAndUser(int userId, int productId) {
        boolean hasBought = false;

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM orders o " +
                             "JOIN order_items oi ON o.order_id = oi.order_id " +
                             "WHERE o.user_id = ? AND oi.product_id = ? AND o.status = 'DELIVERED'")) { // Ajusta el estado 'DELIVERED'

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    hasBought = (count > 0);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasBought;
    }
}
