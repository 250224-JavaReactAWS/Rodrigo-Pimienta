package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDaoImpl implements  CartItemDAO{
    @Override
    public CartItem create(CartItem obj) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // sql
            String sql = "INSERT INTO CART_ITEMS (USER_ID, PRODUCT_ID, QUANTITY) VALUES (?,?,?) RETURNING *";

            // Prepared the statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,obj.getUserId());
            ps.setInt(2, obj.getProductId());
            ps.setInt(3,obj.getQuantity());

            // Execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return getById( rs.getInt("cart_item_id"));
        }catch (SQLException e){
            System.out.println("Could not add the item");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CartItem> getAll() {
        List<CartItem> allCartItems = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){


        String sql = "SELECT * FROM V_CART_ITEMS ORDER BY CART_ITEM_ID ASC";

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            CartItem ci = new CartItem(
                    rs.getInt("cart_item_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("product"),
                    rs.getDouble("product_price"),
                    rs.getInt("product_stock"),
                    rs.getBoolean("product_status"),
                    rs.getDouble("total")
            );

            allCartItems.add(ci);
        }

        }catch (SQLException e ) {
            System.out.println("Could not retrieve carts items");
            e.printStackTrace();
        }

        return allCartItems;
    }

    @Override
    public CartItem getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_CART_ITEMS WHERE CART_ITEM_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new CartItem(
                    rs.getInt("cart_item_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("product"),
                    rs.getDouble("product_price"),
                    rs.getInt("product_stock"),
                    rs.getBoolean("product_status"),
                    rs.getDouble("total")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the item");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CartItem update(CartItem obj) {
        try(Connection conn  = ConnectionUtil.getConnection()){

            // defined the sql
            String sql = "UPDATE CART_ITEMS SET quantity = ? WHERE PRODUCT_ID =? AND USER_ID =? AND CART_ITEM_ID = ?";

            // prepared statement
            PreparedStatement ps  = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, obj.getQuantity());
            ps.setInt(2, obj.getProductId());
            ps.setInt(3, obj.getUserId());
            ps.setInt(4, obj.getCartItemId());


            // execute hte query

            int rows = ps.executeUpdate();

            return (rows > 0 ) ? getById(obj.getCartItemId()) : null;
        }catch (SQLException e){
            System.out.println("Could no update the item");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "DELETE FROM CART_ITEMS WHERE CART_ITEM_ID = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {

            System.out.println("Could not delete the item");

            e.printStackTrace();

            return false;
        }
    }

    @Override
    public List<CartItem> getCartItemsByUser(int userId) {
        List<CartItem> userCartItems = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_CART_ITEMS WHERE USER_ID = ? ORDER BY CART_ITEM_ID ASC";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, userId);

            // execute query
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                CartItem ci = new CartItem(
                        rs.getInt("cart_item_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getString("category"),
                        rs.getString("product"),
                        rs.getDouble("product_price"),
                        rs.getInt("product_stock"),
                        rs.getBoolean("product_status"),
                        rs.getDouble("total")
                );

                userCartItems.add(ci);
            }

        }catch(SQLException e){
            System.out.println("Could not retrieve the user items");
            e.printStackTrace();
        }
        return userCartItems;
    }

    @Override
    public CartItem getCartItemByProductAndUser(int userId, int productId) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_CART_ITEMS WHERE USER_ID = ? AND PRODUCT_ID = ? ORDER BY CART_ITEM_ID ASC";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, userId);
            ps.setInt(2, productId);


            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new CartItem(
                    rs.getInt("cart_item_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("product"),
                    rs.getDouble("product_price"),
                    rs.getInt("product_stock"),
                    rs.getBoolean("product_status"),
                    rs.getDouble("total")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the item");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteCartItemByUser(int userId) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "DELETE FROM CART_ITEMS WHERE USER_ID = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {

            System.out.println("Could not delete the item");

            e.printStackTrace();

        }
        return false;
    }

    @Override
    public boolean deleteCartItemByProductAndUser(int userId, int productId) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "DELETE FROM CART_ITEMS WHERE USER_ID = ? AND PRODUCT_ID = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, productId);


            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {

            System.out.println("Could not delete the user item");

            e.printStackTrace();

        }
        return false;
    }
}
