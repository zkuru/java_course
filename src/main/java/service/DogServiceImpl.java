package service;

import dao.JdbcDogDao;
import lombok.RequiredArgsConstructor;
import model.Dog;
import utils.JdbcConnectionHolder;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
    private final JdbcDogDao dogDao;
    private final JdbcConnectionHolder connectionHolder;

    @Override
    public Dog findById(Long id) {
        return dogDao.findById(id);
    }

    @Override
    public Dog createDog(Dog dog) {
        Connection connection;
        Dog createdDog = null;
        try {
            connection = connectionHolder.getConnection();
            connection.setAutoCommit(false);
            createdDog = dogDao.createDog(dog);
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
            dogDao.deleteDog(id);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connectionHolder.rollBack();
        } finally {
            connectionHolder.closeConnection();
        }
        dogDao.deleteDog(id);
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        Connection connection;
        Dog updatedDog = null;
        try {
            connection = connectionHolder.getConnection();
            connection.setAutoCommit(false);
            updatedDog = dogDao.updateDog(id, dog);
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