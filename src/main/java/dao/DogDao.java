package dao;

import model.Dog;

public interface DogDao {
    Dog findById(Long id);

    Dog createDog(Dog dog);

    void deleteDog(Long id);

    Dog updateDog(Long id, Dog updatedDog);
}