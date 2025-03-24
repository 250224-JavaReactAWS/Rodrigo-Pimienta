package com.revature.repos;

import com.revature.models.Discount;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAOImpl implements  DiscountDAO{
    @Override
    public Discount getCodeByUserAndCode(String code, int userId) {
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.discount_id, d.code, d.percentage, d.max_uses_per_user, d.expired_at, d.active, " +
                             "CASE " +
                             "    WHEN d.max_uses_per_user IS NULL THEN NULL " +
                             "    ELSE d.max_uses_per_user - ( " +
                             "        SELECT COUNT(od.discount_id) " +
                             "        FROM orders_discounts od " +
                             "        JOIN orders o ON od.order_id = o.order_id " +
                             "        WHERE od.discount_id = d.discount_id AND o.user_id = ? " +
                             "    ) " +
                             "END AS remain_uses " +
                             "FROM discounts d " +
                             "WHERE d.code = ?")) {

            stmt.setInt(1, userId);
            stmt.setString(2,code);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Discount(
                            rs.getInt("discount_id"),
                            rs.getString("code"),
                            rs.getDouble("percentage"),
                            rs.getShort("max_uses_per_user"),
                            rs.getObject("expired_at", OffsetDateTime.class),
                            rs.getBoolean("active"),
                            rs.getInt("remain_uses")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve discount code");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Discount getDiscountByCode(String code) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM DISCOUNTS WHERE CODE = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1, code);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new Discount(
                    rs.getInt("discount_id"),
                    rs.getString("code"),
                    rs.getDouble("percentage"),
                    rs.getShort("max_uses_per_user"),
                    rs.getObject("expired_at", OffsetDateTime.class),
                    rs.getBoolean("active")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the discount");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Discount updateStatus(int discountId, boolean status) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE DISCOUNTS SET ACTIVE = ?  WHERE DISCOUNT_ID = ?";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setBoolean(1,status);
            ps.setInt(2,discountId);

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(discountId) : null;
        }catch (SQLException e){
            System.out.println("Could not update the discount status");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Discount> getAvailableDiscountsByUser(int userId){
        List<Discount> availableDiscounts = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.discount_id, d.code, d.percentage, d.max_uses_per_user, d.expired_at, d.active, " +
                             "CASE " +
                             "    WHEN d.max_uses_per_user IS NULL THEN NULL " +
                             "    ELSE d.max_uses_per_user - ( " +
                             "        SELECT COUNT(od.discount_id) " +
                             "        FROM orders_discounts od " +
                             "        JOIN orders o ON od.order_id = o.order_id " +
                             "        WHERE od.discount_id = d.discount_id AND o.user_id = ? " +
                             "    ) " +
                             "END AS remain_uses " +
                             "FROM discounts d " +
                             "WHERE d.active = TRUE AND d.expired_at > CURRENT_TIMESTAMP AND " +
                             "    (d.max_uses_per_user IS NULL OR d.max_uses_per_user > ( " +
                             "        SELECT COUNT(od.discount_id) " +
                             "        FROM orders_discounts od " +
                             "        JOIN orders o ON od.order_id = o.order_id " +
                             "        WHERE od.discount_id = d.discount_id AND o.user_id = ? " +
                             "    ))" +
                             "ORDER BY DISCOUNT_ID ASC")) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Discount discount = new Discount(
                            rs.getInt("discount_id"),
                            rs.getString("code"),
                            rs.getDouble("percentage"),
                            rs.getShort("max_uses_per_user"),
                            rs.getObject("expired_at", OffsetDateTime.class),
                            rs.getBoolean("active"),
                            rs.getInt("remain_uses")
                    );
                    availableDiscounts.add(discount);
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve available discounts for the user");
            e.printStackTrace();
        }
        return availableDiscounts;
    }

    @Override
    public Discount create(Discount obj) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO DISCOUNTS (CODE, PERCENTAGE, MAX_USES_PER_USER, EXPIRED_AT) values (?,?,?,?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setString(1,obj.getCode());
            ps.setDouble(2,obj.getPercentage());
            ps.setInt(3,obj.getMaxUsesPerUser());
            ps.setObject(4,obj.getExpiredAt());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return getById(rs.getInt("discount_id"));

        }catch (SQLException e){
            System.out.println("Could not create the user");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Discount> getAll() {
        List<Discount> allDiscounts = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()) {

            String sql ="SELECT * FROM DISCOUNTS ORDER BY DISCOUNT_ID ASC";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Discount  d = new Discount(
                        rs.getInt("discount_id"),
                        rs.getString("code"),
                        rs.getDouble("percentage"),
                        rs.getShort("max_uses_per_user"),
                        rs.getObject("expired_at", OffsetDateTime.class),
                        rs.getBoolean("active")
                );

                allDiscounts.add(d);
            }
        }catch (SQLException e){
            System.out.println("Could not retrieve the discounts");
        }

        return allDiscounts;
    }

    @Override
    public Discount getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM DISCOUNTS WHERE DISCOUNT_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return new Discount(
                    rs.getInt("discount_id"),
                    rs.getString("code"),
                    rs.getDouble("percentage"),
                    rs.getShort("max_uses_per_user"),
                    rs.getObject("expired_at", OffsetDateTime.class),
                    rs.getBoolean("active")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the discount");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Discount update(Discount obj) {
        try(Connection conn = ConnectionUtil.getConnection()){
            // set query
            String sql ="UPDATE DISCOUNTS SET PERCENTAGE = ?, MAX_USES_PER_USER = ?, EXPIRED_AT = ? WHERE DISCOUNT_ID = ? ";

            // Create PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setDouble(1,obj.getPercentage());
            ps.setInt(2,obj.getMaxUsesPerUser());
            ps.setObject(3,obj.getExpiredAt());
            ps.setInt(4,obj.getDiscountId());

            // execute the statement
            int rows = ps.executeUpdate();

            return (rows > 0) ? getById(obj.getDiscountId()) : null;
        }catch (SQLException e){
            System.out.println("Could not update the discount");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
