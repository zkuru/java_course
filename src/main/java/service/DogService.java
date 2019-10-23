package service;

import model.Dog;

public interface DogService {
    Dog findById(Long id);

    Dog createDog(Dog dog);

    void deleteDog(Long id);

    Dog updateDog(Dog updatedDog);
}