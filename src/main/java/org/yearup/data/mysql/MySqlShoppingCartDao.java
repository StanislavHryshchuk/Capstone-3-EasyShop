package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        ShoppingCart shoppingCart = new ShoppingCart();
        String query = "SELECT * FROM shopping_cart AS sc \n" +
                "JOIN products AS p ON p.product_id = sc.product_id \n" +
                "WHERE user_id = ?;";
        try(
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1,userId);

            try(ResultSet resultSet = ps.executeQuery()) {
                while(resultSet.next()) {
                    shoppingCart = mapRow(resultSet, shoppingCart);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  shoppingCart;
    }

    @Override
    public void update(int userId, int productId, ShoppingCartItem item){
        String query = """ 
                UPDATE shopping_cart SET quantity = ?
                WHERE user_id = ? AND product_id = ?;
                """;
        try(
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
                ){
            ps.setInt(1,item.getQuantity());
            ps.setInt(2,userId);
            ps.setInt(1,productId);
            ps.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addOrIncrease(int userId, int productId, int quantity) {

        String query = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?,?,?)
                ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)
                """;
        try(
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1,userId);
            ps.setInt(2,productId);
            ps.setInt(3,quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(int userId) {
        String query = "DELETE FROM shopping_cart \n" +
                "WHERE user_id = ?;";
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
        ) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private ShoppingCart mapRow(ResultSet row, ShoppingCart shoppingCart) throws SQLException {

        Product product = new Product(
                row.getInt("product_id"),
                row.getString("name"),
                row.getBigDecimal("price"),
                row.getInt("category_id"),
                row.getString("description"),
                row.getString("color"),
                row.getInt("stock"),
                row.getBoolean("featured"),
                row.getString("image_url"));

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(
                product,
                row.getInt("quantity"));

        shoppingCart.add(shoppingCartItem);
        return shoppingCart;
    }
}
