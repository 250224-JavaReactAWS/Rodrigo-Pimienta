package com.revature.repos;


import com.revature.models.UserResetCode;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class UserResetCodeDAOImpl implements UserResetCodeDAO {
    @Override
    public UserResetCode getResetCodeByCode(String code) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // inti the query
            String sql="SELECT * FROM USER_RESET_CODES WHERE RESET_CODE = ?";

            // Create a prepared Statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,code);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // this will only return one user

            if(!rs.next()){
                return null;
            }

            return new UserResetCode(
                    rs.getInt("reset_code_id"),
                    rs.getInt("user_id"),
                    rs.getString("reset_code"),
                    rs.getBoolean("used"),
                    rs.getObject("expired_at", OffsetDateTime.class)
            );

        }catch (SQLException e) {
            //throw new RuntimeException(e);
            System.out.println("Could not retrieve user by email");
            e.printStackTrace();
        }
        return null;    }

    @Override
    public UserResetCode getActiveResetCodeByUser(int userId) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // define the query
            String sql ="SELECT * FROM USER_RESET_CODES WHERE USED = FALSE AND EXPIRED_AT > NOW() AND USER_ID = ? LIMIT 1";

            // create the statement
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            // execute the query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }
            return new UserResetCode(
                rs.getInt("reset_code_id"),
                rs.getInt("user_id"),
                rs.getString("reset_code"),
                rs.getBoolean("used"),
                    rs.getObject("expired_at", OffsetDateTime.class)
            );

        }catch (SQLException e){
            System.out.println("Could not retrieve all users");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public UserResetCode create(UserResetCode obj) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // SET QUERY USER_RESET_CODES
            String sql ="INSERT INTO USER_RESET_CODES (USER_ID, RESET_CODE, EXPIRED_AT) VALUES (?,?,?) RETURNING *";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, obj.getUserId());
            ps.setString(2,obj.getResetCode());
            ps.setObject(3,obj.getExpiredAt());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new UserResetCode(
                    rs.getInt("reset_code_id"),
                    rs.getInt("user_id"),
                    rs.getString("reset_code"),
                    rs.getBoolean("used"),
                    rs.getObject("expired_at", OffsetDateTime.class)
            );
        }catch (SQLException e){
            System.out.println("Could not create the reset code");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserResetCode> getAll() { return List.of(); }

    @Override
    public UserResetCode getById(int id) {
        return null;
    }

    @Override
    public UserResetCode update(UserResetCode obj) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE USER_RESET_CODES SET USED = TRUE WHERE USER_ID = ? AND  RESET_CODE_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,obj.getUserId());
            ps.setInt(2,obj.getResetCodeId());

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? obj : null;
        }catch (SQLException e){
            System.out.println("Could not update the status of the reset code");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
