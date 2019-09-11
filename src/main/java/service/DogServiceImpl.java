package service;

import dao.JdbcDogDao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Dog;
import utils.CGLibTransactional;

@NoArgsConstructor
@AllArgsConstructor
public class DogServiceImpl {
    private JdbcDogDao dogDao;

    public Dog findById(Long id) {
        return dogDao.findById(id);
    }

    @CGLibTransactional
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