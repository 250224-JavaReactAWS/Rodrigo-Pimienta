package com.revature.repos;

import com.revature.models.OrderDiscount;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDiscountDAOImpl implements OrderDiscountDAO {
    @Override
    public OrderDiscount create(OrderDiscount obj) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO ORDERS_DISCOUNTS (DISCOUNT_ID, ORDER_ID, PERCENTAGE, DISCOUNT_AMOUNT, DISCOUNT_ORDER) values (?,?,?,?,?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,obj.getDiscountId());
            ps.setInt(2,obj.getOrderId());
            ps.setDouble(3,obj.getPercentage());
            ps.setDouble(4,obj.getAmount());
            ps.setInt(5,obj.getOrder());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new OrderDiscount(rs.getInt("discount_id"), rs.getInt("order_id"), rs.getDouble("percentage"), rs.getDouble("discount_amount"), rs.getInt("discount_order"));

        }catch (SQLException e){
            System.out.println("Could not create the order discounts");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<OrderDiscount> getAll() {
        List<OrderDiscount> allOrdersDiscounts = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM V_ORDERS_DISCOUNTS ORDER BY ORDER_ID ASC, DISCOUNT_ORDER ASC";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                OrderDiscount od = new OrderDiscount(rs.getInt("discount_id"), rs.getInt("order_id"), rs.getDouble("percentage"), rs.getDouble("discount_amount"), rs.getInt("discount_order"), rs.getString("code"));

                allOrdersDiscounts.add(od);
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve the orders discounts");
            e.printStackTrace();
        }

        return allOrdersDiscounts;
    }

    @Override
    public OrderDiscount getById(int id) {

        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM V_ORDERS_DISCOUNTS WHERE ORDER_DISCOUNT_ID = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return new OrderDiscount( rs.getInt("discount_id"), rs.getInt("order_id"), rs.getDouble("percentage"), rs.getDouble("discount_amount"), rs.getInt("discount_order"),rs.getString("code"));

        } catch (SQLException e) {
            System.out.println("Could not retrieve the order discount");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public OrderDiscount update(OrderDiscount obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public List<OrderDiscount> getOrderDiscounts(int orderId) {
        List<OrderDiscount> allOrdersDiscounts = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM V_ORDERS_DISCOUNTS WHERE ORDER_ID = ? ORDER BY DISCOUNT_ORDER ASC";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDiscount od = new OrderDiscount( rs.getInt("discount_id"), rs.getInt("order_id"), rs.getDouble("percentage"), rs.getDouble("discount_amount"), rs.getInt("discount_order"), rs.getString("code"));

                allOrdersDiscounts.add(od);
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve the orders discounts");
            e.printStackTrace();
        }

        return allOrdersDiscounts;
    }
}
