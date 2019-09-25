package service;

import dao.JdbcDogDao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Dog;
import org.springframework.transaction.annotation.Transactional;

@NoArgsConstructor
@AllArgsConstructor
public class DogServiceImpl implements DogService {
    private JdbcDogDao dogDao;

    @Transactional(readOnly = true)
    public Dog findById(Long id) {
        return dogDao.findById(id);
    }

    @Transactional
    public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Transactional
    public void deleteDog(Long id) {
        dogDao.deleteDog(id);
    }

    @Transactional
    public Dog updateDog(Long id, Dog dog) {
        return dogDao.updateDog(id, dog);
    }
}