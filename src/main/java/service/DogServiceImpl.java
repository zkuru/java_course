package service;

import dao.JdbcDogDao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Dog;
import utils.CGLibTransactional;
import utils.CustomTransactional;

@NoArgsConstructor
@AllArgsConstructor
public class DogServiceImpl implements DogService{
    private JdbcDogDao dogDao;

    @CustomTransactional
    public Dog findById(Long id) {
        return dogDao.findById(id);
    }

    @CustomTransactional
    public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @CGLibTransactional
    public void deleteDog(Long id) {
        dogDao.deleteDog(id);
    }

    @CGLibTransactional
    public Dog updateDog(Long id, Dog dog) {
        return dogDao.updateDog(id, dog);
    }
}