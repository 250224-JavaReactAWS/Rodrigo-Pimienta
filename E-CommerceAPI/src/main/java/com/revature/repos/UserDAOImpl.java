package com.revature.repos;

import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO{
    /*
     Here I'm using polymorphism across Overriding the abstract methods
     inherent of the UserDAO and GeneralDAO interfaces

     ALSO, here I have to perform the database query's

     */

    // DEDICATED METHODS
    @Override
    public User getUserByEmail(String email) {

        // First, use Try with resource

        try(Connection conn = ConnectionUtil.getConnection()){

            // inti the query
            String sql="SELECT * FROM USERS WHERE EMAIL = ?";

            // Create a prepared Statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,email);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // this will only return one user

            if(!rs.next()){
                return null;
            }

            return new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"),
                UserRole.valueOf(rs.getString("role")),
                rs.getBoolean("active")
            );

        }catch (SQLException e) {
            //throw new RuntimeException(e);
            System.out.println("Could not retrieve user by email");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User updateStatus(int userId, boolean status) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // SET QUERY
            String sql ="UPDATE USERS SET ACTIVE = ?  WHERE USER_ID = ?";

            // SET PreparesStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setBoolean(1, status);
            ps.setInt(2,userId);

            int rows = ps.executeUpdate();

            if(rows < 0){
                return null;
            }

            return getById(userId);

        }catch(SQLException e ){
            System.out.println("Could not update the user status");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User updatePassword(int userId, String password) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // SET QUERY
            String sql ="UPDATE USERS SET PASSWORD = ?  WHERE USER_ID = ?";

            // SET PreparesStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1, password);
            ps.setInt(2,userId);

            int rows = ps.executeUpdate();

            if(rows < 0){
                return null;
            }

            return getById(userId);

        }catch(SQLException e ){
            System.out.println("Could not update user password");
            e.printStackTrace();
        }
        return null;
    }

    //CRUD METHODS
    @Override
    public User create(User obj) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO USERS (first_name, last_name, email, password) values (?,?,?,?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,obj.getFirstName());
            ps.setString(2,obj.getLastName());
            ps.setString(3,obj.getEmail());
            ps.setString(4,obj.getPassword());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    UserRole.valueOf(rs.getString("role")),
                    rs.getBoolean("active")
            );

        }catch (SQLException e){
            System.out.println("Could not create the user");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> getAll() {

        List<User> allUsers = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // define the query
            String sql ="SELECT * FROM USERS";

            // create the statement
            Statement stmt = conn.createStatement();

            // execute the query
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                User u = new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    UserRole.valueOf(rs.getString("role")),
                    rs.getBoolean("active")
                );

                allUsers.add(u);
            }

        }catch (SQLException e){
            System.out.println("Could not retrieve all users");
            e.printStackTrace();
        }

        return allUsers;
    }

    @Override
    public User getById(int id) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM USERS WHERE USER_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    UserRole.valueOf(rs.getString("role")),
                    rs.getBoolean("active")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the user");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User obj) {

        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE USERS SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PASSWORD = ? WHERE USER_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,obj.getFirstName());
            ps.setString(2,obj.getLastName());
            ps.setString(3,obj.getEmail());
            ps.setString(4,obj.getPassword());
            ps.setInt(5,obj.getUserId());

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(obj.getUserId()) : null;
        }catch (SQLException e){
            System.out.println("Could not update the user");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
