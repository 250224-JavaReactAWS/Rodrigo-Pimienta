package com.revature.repos;

import com.revature.models.Product;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO{
    @Override
    public List<Product> getAllActive() {
        List<Product> allProducts = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            String sql ="SELECT * FROM V_PRODUCTS WHERE ACTIVE = TRUE ORDER BY PRODUCT_ID ASC";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getBoolean("active"),
                        rs.getDouble("rating")
                );

                allProducts.add(p);
            }

        }catch (SQLException e ) {
            System.out.println("Could not retrieve active products");
            e.printStackTrace();
        }

        return allProducts;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {

        List<Product> allProducts = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_PRODUCTS WHERE CATEGORY_ID = ? ORDER BY PRODUCT_ID ASC";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, categoryId);

            // execute query
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getBoolean("active"),
                        rs.getDouble("rating")
                );

                allProducts.add(p);
            }

        }catch(SQLException e){
            System.out.println("Could not retrieve the products of the category");
            e.printStackTrace();
        }

        return allProducts;
    }

    @Override
    public Product create(Product obj) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO PRODUCTS (category_id, name, description, price, stock) values (?,?,?,?,?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,obj.getCategoryId());
            ps.setString(2,obj.getName());
            ps.setString(3,obj.getDescription());
            ps.setDouble(4,obj.getPrice());
            ps.setInt(5,obj.getStock());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getBoolean("active"),
                    0.0
            );

        }catch (SQLException e){
            System.out.println("Could not create the product");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Product> getAll() {
        List<Product> allProducts = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            String sql ="SELECT * FROM V_PRODUCTS ORDER BY PRODUCT_ID ASC";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getBoolean("active"),
                        rs.getDouble("rating")
                );

                allProducts.add(p);
            }

        }catch (SQLException e ) {
            System.out.println("Could not retrieve products");
            e.printStackTrace();
        }

        return allProducts;
    }

    @Override
    public Product getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_PRODUCTS WHERE PRODUCT_ID = ? ORDER BY PRODUCT_ID ASC";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getBoolean("active"),
                    rs.getDouble("rating")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the product");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product update(Product obj) {

        try(Connection conn  = ConnectionUtil.getConnection()){

            // defined the sql
            String sql = "UPDATE PRODUCTS SET CATEGORY_ID = ?, NAME =?, DESCRIPTION = ?, PRICE =?, STOCK =? WHERE PRODUCT_ID =?";

            // prepared statement
            PreparedStatement ps  = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, obj.getCategoryId());
            ps.setString(2,obj.getName());
            ps.setString(3,obj.getDescription());
            ps.setDouble(4,obj.getPrice());
            ps.setInt(5,obj.getStock());
            ps.setInt(6,obj.getProductId());

            // execute hte query

            int rows = ps.executeUpdate();

            return (rows > 0 ) ? getById(obj.getProductId()) : null;
        }catch (SQLException e){
            System.out.println("Could no update the product");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product updateStockById(int productId, int stock) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE PRODUCTS SET STOCK = ?  WHERE PRODUCT_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,stock);
            ps.setInt(2,productId);

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(productId) : null;
        }catch (SQLException e){
            System.out.println("Could not update the product stock");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product updateStatusById(int productId, boolean status) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE PRODUCTS SET ACTIVE = ?  WHERE PRODUCT_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setBoolean(1,status);
            ps.setInt(2,productId);

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(productId) : null;
        }catch (SQLException e){
            System.out.println("Could not update the product status");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
