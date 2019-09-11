package service;

import dao.JdbcDogDao;
import lombok.RequiredArgsConstructor;
import model.Dog;
import utils.CustomTransactional;

@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
    private final JdbcDogDao dogDao;

    @Override
    public Dog findById(Long id) {
        return dogDao.findById(id);
    }

    @Override
    @CustomTransactional
    public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Override
    @CustomTransactional
    public void deleteDog(Long id) {
        dogDao.deleteDog(id);
    }

    @Override
    @CustomTransactional
    public Dog updateDog(Long id, Dog dog) {
        return dogDao.updateDog(id, dog);
    }
}