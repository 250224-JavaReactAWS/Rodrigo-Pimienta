package com.revature.repos;

import com.revature.models.ProductReview;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductReviewDAOImpl implements ProductReviewDAO{
    @Override
    public ProductReview create(ProductReview obj) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql = "INSERT INTO PRODUCT_REVIEWS (USER_ID, PRODUCT_ID, COMMENT, RATING) values (?,?,?,?) RETURNING *";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1,obj.getUserId());
            ps.setInt(2,obj.getProductId());
            ps.setString(3,obj.getComment());
            ps.setShort(4,obj.getRating());


            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            return getById(rs.getInt("review_id"));

        }catch (SQLException e){
            System.out.println("Could not create the review");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProductReview> getAll() {

        List<ProductReview> allProductsReviews = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            String sql ="SELECT * FROM V_PRODUCT_REVIEWS";

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String nameUser =  rs.getString("first_name_user") +" "+ rs.getString("last_name_user");

                ProductReview pr = new ProductReview(
                        rs.getInt("review_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getString("comment"),
                        rs.getShort("rating"),
                        rs.getObject("review_date", OffsetDateTime.class),
                        nameUser,
                        rs.getString("product"),
                        rs.getString("category")
                );

                allProductsReviews.add(pr);
            }

        }catch (SQLException e ) {
            System.out.println("Could not retrieve reviews");
            e.printStackTrace();
        }

        return allProductsReviews;
    }

    @Override
    public ProductReview getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_PRODUCT_REVIEWS WHERE REVIEW_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, id);

            // execute query
            ResultSet rs = ps.executeQuery();

            if(!rs.next()){
                return null;
            }

            String nameUser =  rs.getString("first_name_user") +" "+ rs.getString("last_name_user");

            return new ProductReview(
                    rs.getInt("review_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getString("comment"),
                    rs.getShort("rating"),
                    rs.getObject("review_date", OffsetDateTime.class),
                    nameUser,
                    rs.getString("product"),
                    rs.getString("category")
            );

        }catch(SQLException e){
            System.out.println("Could not retrieve the review");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProductReview update(ProductReview obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public List<ProductReview> getProductReviewsByProduct(int productId) {
        List<ProductReview> allProductsReviews = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_PRODUCT_REVIEWS WHERE PRODUCT_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, productId);

            // execute query
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String nameUser =  rs.getString("first_name_user") +" "+ rs.getString("last_name_user");

                ProductReview pr = new ProductReview(
                        rs.getInt("review_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getString("comment"),
                        rs.getShort("rating"),
                        rs.getObject("review_date", OffsetDateTime.class),
                        nameUser,
                        rs.getString("product"),
                        rs.getString("category")
                );

                allProductsReviews.add(pr);
            }

        }catch(SQLException e){
            System.out.println("Could not retrieve the product reviews");
            e.printStackTrace();
        }
        return allProductsReviews;
    }

    @Override
    public List<ProductReview> getUserReviews(int userId) {
        List<ProductReview> allProductsReviews = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()){

            // set query
            String sql="SELECT * FROM V_PRODUCT_REVIEWS WHERE USER_ID = ?";

            // CREATE PREPARED STATEMENT
            PreparedStatement ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, userId);

            // execute query
            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                String nameUser =  rs.getString("first_name_user") +" "+ rs.getString("last_name_user");

                ProductReview pr = new ProductReview(
                        rs.getInt("review_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getString("comment"),
                        rs.getShort("rating"),
                        rs.getObject("review_date", OffsetDateTime.class),
                        nameUser,
                        rs.getString("product"),
                        rs.getString("category")
                );

                allProductsReviews.add(pr);
            }

        }catch(SQLException e){
            System.out.println("Could not retrieve the user reviews");
            e.printStackTrace();
        }
        return allProductsReviews;
    }
}
