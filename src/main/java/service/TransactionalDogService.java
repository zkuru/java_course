package service;

import lombok.RequiredArgsConstructor;
import model.Dog;
import utils.JdbcConnectionHolder;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class TransactionalDogService implements DogService {
    private final JdbcConnectionHolder connectionHolder;
    private final DogService dogService;

    @Override
    public Dog findById(Long id) {
        return dogService.findById(id);
    }

    @Override
    public Dog createDog(Dog dog) {
        Connection connection;
        Dog createdDog = null;
        try {
            connection = connectionHolder.getConnection();
            connection.setAutoCommit(false);
            createdDog = dogService.createDog(dog);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connectionHolder.rollBack();
        } finally {
            connectionHolder.closeConnection();
        }
        return createdDog;
    }

    @Override
    public void deleteDog(Long id) {
        Connection connection;
        try {
            connection = connectionHolder.getConnection();
            connection.setAutoCommit(false);
            dogService.deleteDog(id);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connectionHolder.rollBack();
        } finally {
            connectionHolder.closeConnection();
        }
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        Connection connection;
        Dog updatedDog = null;
        try {
            connection = connectionHolder.getConnection();
            connection.setAutoCommit(false);
            updatedDog = dogService.updateDog(id, dog);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connectionHolder.rollBack();
        } finally {
            connectionHolder.closeConnection();
        }
        return updatedDog;
    }
}