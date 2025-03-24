package com.revature.repos;

import com.revature.models.UserAddress;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAddressDAOImpl implements UserAddressDAO {

    @Override
    public UserAddress create(UserAddress obj) {

        try(Connection conn = ConnectionUtil.getConnection()){
            // user addresses
            // SET QUERY
            String sql ="INSERT INTO USER_ADDRESSES (USER_ID, COUNTRY, STATE, CITY, STREET, HOUSE_NUMBER, POSTAL_CODE)" +
                    "VALUES (?,?,?,?,?,?,?) RETURNING *";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, obj.getUserId());
            ps.setString(2,obj.getCountry());
            ps.setString(3,obj.getState());
            ps.setString(4,obj.getCity());
            ps.setString(5,obj.getStreet());
            ps.setString(6,obj.getHouseNumber());
            ps.setString(7,obj.getPostalCode());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new UserAddress(
                    rs.getInt("address_id"),
                    rs.getInt("user_id"),
                    rs.getString("country"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("street"),
                    rs.getString("house_number"),
                    rs.getString("postal_code"),
                    rs.getBoolean("active")
            );
        }catch (SQLException e){
            System.out.println("Could not add the user address");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserAddress> getAll() {
        List<UserAddress> allAddress = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // sql query
            String sql ="SELECT * FROM USER_ADDRESSES";

            // create statement
            Statement stmt = conn.createStatement();

            // execute stmt
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                UserAddress ua = new UserAddress(
                    rs.getInt("address_id"),
                    rs.getInt("user_id"),
                    rs.getString("country"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("street"),
                    rs.getString("house_number"),
                    rs.getString("postal_code"),
                    rs.getBoolean("active")
                );

                allAddress.add(ua);
            }


        }catch (SQLException e){
            System.out.println("Could not retrieve the user address");
            e.printStackTrace();
        }

        return allAddress;
    }

    @Override
    public UserAddress getById(int id) {

        try(Connection conn = ConnectionUtil.getConnection()){

            // Set query
            String sql="SELECT * FROM USER_ADDRESSES WHERE ADDRESS_ID = ?";

            // SET prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // SET VALUES
            ps.setInt(1,id);

            // Execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new UserAddress(
                rs.getInt("address_id"),
                rs.getInt("user_id"),
                rs.getString("country"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("street"),
                rs.getString("house_number"),
                rs.getString("postal_code"),
                rs.getBoolean("active")
            );

        }catch (SQLException e){
            System.out.println("Could not retrieve the user");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserAddress update(UserAddress obj) {

        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE USER_ADDRESSES SET COUNTRY = ?, STATE = ?, CITY = ?, STREET = ?, HOUSE_NUMBER = ?, POSTAL_CODE = ? WHERE ADDRESS_ID = ? AND USER_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,obj.getCountry());
            ps.setString(2,obj.getState());
            ps.setString(3,obj.getCity());
            ps.setString(4,obj.getStreet());
            ps.setString(5,obj.getHouseNumber());
            ps.setString(6,obj.getPostalCode());
            ps.setInt(7, obj.getAddressId());
            ps.setInt(8, obj.getUserId());


            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? obj : null;
        }catch (SQLException e){
            System.out.println("Could not update the address");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public List<UserAddress> getUserAddress(int userId) {
        List<UserAddress> allUserAddress = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // Set query
            String sql="SELECT * FROM USER_ADDRESSES WHERE USER_ID = ?";

            // SET prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);

            // SET VALUES
            ps.setInt(1,userId);

            // Execute query
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                UserAddress ua = new UserAddress(
                        rs.getInt("address_id"),
                        rs.getInt("user_id"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("street"),
                        rs.getString("house_number"),
                        rs.getString("postal_code"),
                        rs.getBoolean("active")
                );

                allUserAddress.add(ua);
            }

        }catch (SQLException e){
            System.out.println("Could not retrieve the user");
            e.printStackTrace();
        }

        return allUserAddress;
    }

    @Override
    public UserAddress updateStatus(int userId, boolean status) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // SET QUERY
            String sql ="UPDATE USER_ADDRESSES SET ACTIVE = ?  WHERE ADDRESS_ID = ?";

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
            System.out.println("Could not update the user address status");
            e.printStackTrace();
        }
        return null;
    }
}
