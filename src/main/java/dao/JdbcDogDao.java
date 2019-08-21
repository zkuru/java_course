package dao;

import lombok.RequiredArgsConstructor;
import model.Dog;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.time.ZoneId;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@RequiredArgsConstructor
public class JdbcDogDao implements DogDao {
    private final JdbcDataSource dataSource;

    @Override
    public Dog findById(Long id) {
        try (Connection c = dataSource.getConnection()) {
            PreparedStatement statement = c.prepareStatement("SELECT * FROM DOG where id = ?");
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
        String INSERT_STATEMENT = "INSERT INTO DOG (name, date, height, weight) values (?, ?, ?, ?)";
        try (Connection c = dataSource.getConnection()) {
            PreparedStatement statement = c.prepareStatement(INSERT_STATEMENT, RETURN_GENERATED_KEYS);
            statement.setString(1, dog.getName());
            statement.setDate(2, dog.getDate() == null ? null
                    : Date.valueOf(dog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            statement.setObject(3, dog.getHeight(), Types.INTEGER);
            statement.setObject(4, dog.getWeight(), Types.INTEGER);
            statement.executeUpdate();
            if (statement.getGeneratedKeys().next())
                dog.setId(statement.getGeneratedKeys().getLong(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dog;
    }

    @Override
    public void deleteDog(Long id) {
        try (Connection c = dataSource.getConnection()) {
            PreparedStatement statement = c.prepareStatement("DELETE FROM DOG where id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dog updateDog(Long id, Dog updatedDog) {
        String UPDATE_STATEMENT = "UPDATE DOG SET name = ?, date = ?, height = ?, weight = ? where id = ?";
        try (Connection c = dataSource.getConnection()) {
            PreparedStatement statement = c.prepareStatement(UPDATE_STATEMENT);
            statement.setString(1, updatedDog.getName());
            statement.setDate(2, updatedDog.getDate() == null ? null
                    : Date.valueOf(updatedDog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            statement.setObject(3, updatedDog.getHeight(), Types.INTEGER);
            statement.setObject(4, updatedDog.getWeight(), Types.INTEGER);
            statement.setLong(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedDog.setId(id);
    }
}