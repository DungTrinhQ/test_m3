package service;

import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    public ProductDAOImpl() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baithi?useSSL=false", "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public List<Product> findAllProduct() {
        List<Product> list = new ArrayList<>();
        Product p = null;
        String query = "select p.id, p.name, p.price, p.quantity, p.color, p.description, category.name" +
                "from product join category on product.category_id = category.id ;";
        try (Connection connection = getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("p.id");
                String name = resultSet.getString("p.name");
                int price = resultSet.getInt("p.price");
                int quantity = resultSet.getInt("p.quantity");
                String color = resultSet.getString("p.color");
                String description = resultSet.getString("p.description");
                String category = resultSet.getString("category.name");
                p = new Product(id, name, price, quantity, color, description, category);
                list.add(p);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return list;
    }

    @Override
    public void insertProduct(Product product) {
        String query = "insert into product(name,price,quantity,color,description,category_id) values (?,?,?,?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, product.getName());
            pstm.setInt(2, product.getPrice());
            pstm.setInt(3, product.getQuantity());
            pstm.setString(4, product.getColor());
            pstm.setString(5, product.getDescription());
            pstm.setString(6, product.getCategory());
            pstm.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public boolean deleteProduct(int id) {
        boolean delete = false;
        String query = "delete from product where id = ?;";
        try (Connection connection = getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setInt(1, id);
            delete = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return delete;
    }

    @Override
    public boolean updateProduct(Product product) {
        boolean update = false;
        String query = "update product set name =?, price =?, quantity =?, color=?, description = ?, category_id = ? where id = ?;";
        try (Connection connection = getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, product.getName());
            pstm.setInt(2, product.getPrice());
            pstm.setInt(3, product.getQuantity());
            pstm.setString(4, product.getColor());
            pstm.setString(5, product.getDescription());
            pstm.setString(6, product.getCategory());
            pstm.setInt(7, product.getId());
            update = pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            printSQLException(e);
        }
        return update;
    }

    @Override
    public Product selectProduct(int id) {
        Product product = null;
        String query = "select product.id, product.name, product.price, product.quantity, product.color, product.description, category.name from product join category on product.category_id = category.id where product.id = ?;";
        try (Connection connection = getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setInt(1, id);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("product.name");
                int price = resultSet.getInt("product.price");
                int quantity = resultSet.getInt("product.quantity");
                String color = resultSet.getString("product.color");
                String description = resultSet.getString("product.description");
                String category = resultSet.getString("category.name");
                product = new Product(id, name, price, quantity, color, description, category);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return product;
    }

    @Override
    public List<Product> selectProductByName(String name) {
        List<Product> list = new ArrayList<>();
        Product product = null;
        String query = "select product.id, product.name, product.price, product.quantity, product.color, product.description, category.name from product join category on product.category_id = category.id where product.name regex = ?;";
        try (Connection connection = getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, name);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("product.id");
                int price = resultSet.getInt("product.price");
                int quantity = resultSet.getInt("product.quantity");
                String color = resultSet.getString("product.color");
                String description = resultSet.getString("product.description");
                String category = resultSet.getString("category.name");
                product = new Product(id, name, price, quantity, color, description, category);
                list.add(product);
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        return list;

    }
}

