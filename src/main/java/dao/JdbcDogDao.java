package dao;

import lombok.RequiredArgsConstructor;
import model.Dog;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZoneId;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@RequiredArgsConstructor
public class JdbcDogDao implements DogDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Dog findById(Long id) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM DOG where id = ?",
                (rs, rowNum) -> {
                    long dogId = rs.getLong(1);
                    String name = rs.getString(2);
                    Date date = rs.getDate(3);
                    Integer height = rs.getObject(4, Integer.class);
                    Integer weight = rs.getObject(5, Integer.class);
                    return new Dog().setId(dogId).setName(name).setDate(date).setHeight(height).setWeight(weight);
                }, id));
    }

    @Override
    public Dog createDog(Dog dog) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO DOG (name, date, height, weight) " +
                    "values (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            return preparedStatement(dog, statement);
        }, holder);
        return dog.setId(holder.getKey().longValue());
    }

    @Override
    public void deleteDog(Long id) {
        jdbcTemplate.update("DELETE FROM DOG where id = ?", id);
    }

    @Override
    public Dog updateDog(Long id, Dog updatedDog) {
        jdbcTemplate.update("UPDATE DOG SET name = ?, date = ?, height = ?, weight = ? where id = ?",
                statement -> {
                    preparedStatement(updatedDog, statement).setLong(5, id);
                });
        return updatedDog.setId(id);
    }

    private PreparedStatement preparedStatement(Dog dog, PreparedStatement statement) throws SQLException {
        statement.setString(1, dog.getName());
        statement.setDate(2, dog.getDate() == null ? null
                : Date.valueOf(dog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        statement.setObject(3, dog.getHeight(), Types.INTEGER);
        statement.setObject(4, dog.getWeight(), Types.INTEGER);
        return statement;
    }
}