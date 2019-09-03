package dao;

import lombok.RequiredArgsConstructor;
import model.Dog;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneId;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@RequiredArgsConstructor
public class JdbcDogDao implements DogDao {
    private final DataSource dataSource;

    @Override
    public Dog findById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DOG where id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long dogId = resultSet.getLong(1);
                String name = resultSet.getString(2);
                Date date = resultSet.getDate(3);
                Integer height = resultSet.getObject(4, Integer.class);
                Integer weight = resultSet.getObject(5, Integer.class);
                return new Dog().setId(dogId).setName(name).setDate(date).setHeight(height).setWeight(weight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Dog createDog(Dog dog) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                    "INSERT INTO DOG (name, date, height, weight) values (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            statement.setString(1, dog.getName());
            statement.setDate(2, dog.getDate() == null ? null
                    : Date.valueOf(dog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            statement.setObject(3, dog.getHeight(), Types.INTEGER);
            statement.setObject(4, dog.getWeight(), Types.INTEGER);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next())
                dog.setId(resultSet.getLong(1));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack(connection);
        } finally {
            closeConnection(connection, statement);
        }
        return dog;
    }

    @Override
    public void deleteDog(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("DELETE FROM DOG where id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack(connection);
        } finally {
            closeConnection(connection, statement);
        }
    }

    @Override
    public Dog updateDog(Long id, Dog updatedDog) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE DOG SET name = ?, date = ?, height = ?, weight = ? where id = ?");
            statement.setString(1, updatedDog.getName());
            statement.setDate(2, updatedDog.getDate() == null ? null
                    : Date.valueOf(updatedDog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            statement.setObject(3, updatedDog.getHeight(), Types.INTEGER);
            statement.setObject(4, updatedDog.getWeight(), Types.INTEGER);
            statement.setLong(5, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack(connection);
        } finally {
            closeConnection(connection, statement);
        }
        return updatedDog.setId(id);
    }

    private void closeConnection(Connection c, PreparedStatement ps) {
        try {
            if (c != null)
                c.close();
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rollBack(Connection c) {
        if (c != null) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}