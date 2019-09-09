package dao;

import lombok.RequiredArgsConstructor;
import model.Dog;
import utils.JdbcConnectionHolder;

import java.sql.*;
import java.time.ZoneId;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@RequiredArgsConstructor
public class JdbcDogDao implements DogDao {
    private final JdbcConnectionHolder connectionHolder;

    @Override
    public Dog findById(Long id) {
        try (Connection connection = connectionHolder.getConnection()) {
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
        PreparedStatement statement = null;
        try {
            Connection connection = connectionHolder.getConnection();
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
        } catch (SQLException e) {
            e.printStackTrace();
            closePreparedStatement(statement);
        }
        return dog;
    }

    @Override
    public void deleteDog(Long id) {
        PreparedStatement statement = null;
        try {
            Connection connection = connectionHolder.getConnection();
            statement = connection.prepareStatement("DELETE FROM DOG where id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closePreparedStatement(statement);
        }
    }

    @Override
    public Dog updateDog(Long id, Dog updatedDog) {
        PreparedStatement statement = null;
        try {
            Connection connection = connectionHolder.getConnection();
            statement = connection.prepareStatement("UPDATE DOG SET name = ?, date = ?, height = ?, weight = ? where id = ?");
            statement.setString(1, updatedDog.getName());
            statement.setDate(2, updatedDog.getDate() == null ? null
                    : Date.valueOf(updatedDog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            statement.setObject(3, updatedDog.getHeight(), Types.INTEGER);
            statement.setObject(4, updatedDog.getWeight(), Types.INTEGER);
            statement.setLong(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closePreparedStatement(statement);
        }
        return updatedDog.setId(id);
    }

    private void closePreparedStatement(PreparedStatement ps) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}