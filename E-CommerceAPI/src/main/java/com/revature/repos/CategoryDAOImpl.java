package com.revature.repos;

import com.revature.models.Category;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO{
    @Override
    public Category create(Category obj) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO CATEGORY (name) values (?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,obj.getName());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new Category(
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getBoolean("active")
            );

        }catch (SQLException e){
            System.out.println("Could not create the category");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Category> getAll() {
        List<Category> allCategories = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM CATEGORY";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

                Category c = new Category(
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getBoolean("active")
                );

                allCategories.add(c);
            }

        }catch (SQLException e){
            System.out.println("Could not retrieve categories");
        }

        return allCategories;
    }

    @Override
    public Category getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM CATEGORY WHERE CATEGORY_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new Category(
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getBoolean("active")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the category");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category update(Category obj) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE CATEGORY SET NAME = ?  WHERE CATEGORY_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,obj.getName());
            ps.setInt(2,obj.getCategoryId());

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(obj.getCategoryId()) : null;
        }catch (SQLException e){
            System.out.println("Could not update the category");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public Category getCategoryByName(String name) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM CATEGORY WHERE NAME = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1, name);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new Category(
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getBoolean("active")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the category");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category updateStatus(int categoryId, boolean status) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE CATEGORY SET ACTIVE = ?  WHERE CATEGORY_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setBoolean(1,status);
            ps.setInt(2,categoryId);

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(categoryId) : null;
        }catch (SQLException e){
            System.out.println("Could not update the category status");
            e.printStackTrace();
        }
        return null;
    }
}
