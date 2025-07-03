package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleJDBCRepository {

    // Implement class SimpleJDBCRepository, resolve all class methods for create, update, delete and search Users.
    // Use CustomDataSource and CustomConnector for performing statement or prepareStatement to database.

    private Connection connection;
    private PreparedStatement ps;
    private Statement st;

    private final CustomDataSource dataSource = CustomDataSource.getInstance();

    private static final String createUserSQL = "INSERT INTO myusers (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myusers SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String deleteUserSQL = "DELETE FROM myusers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser(User user) {
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        } finally {
            closeResources();
        }
        return null;
    }


    public User findUserById(Long userId) {
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by ID", e);
        } finally {
            closeResources();
        }
        return null;
    }

    public User findUserByName(String userName) {
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by name", e);
        } finally {
            closeResources();
        }
        return null;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            st = connection.createStatement();

            ResultSet rs = st.executeQuery(findAllUserSQL);
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        } finally {
            closeResources();
        }
        return users;
    }

    public User updateUser(User user) {
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(updateUserSQL);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());

            int updated = ps.executeUpdate();
            if (updated > 0) {
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        } finally {
            closeResources();
        }
        return null;
    }

    public void deleteUser(Long userId) {
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(deleteUserSQL);
            ps.setLong(1, userId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        } finally {
            closeResources();
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("firstname"));
        user.setLastName(rs.getString("lastname"));
        user.setAge(rs.getInt("age"));
        return user;
    }

    private void closeResources() {
        try {
            if (ps != null) ps.close();
        } catch (SQLException ignored) {
        }
        try {
            if (st != null) st.close();
        } catch (SQLException ignored) {
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException ignored) {
        }
    }
}