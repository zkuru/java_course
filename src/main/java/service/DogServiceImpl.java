package service;

import dao.JdbcDogDao;
import lombok.RequiredArgsConstructor;
import model.Dog;
import utils.JdbcConnectionHolder;

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
        return dogDao.createDog(dog);
    }

    @Override
    public void deleteDog(Long id) {
        dogDao.deleteDog(id);
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        return dogDao.updateDog(id, dog);
    }
}