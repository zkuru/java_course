package service;

import lombok.RequiredArgsConstructor;
import model.Dog;

@RequiredArgsConstructor
public class TransactionalDogService implements DogService {
    private final DogService dogService;

    @Override
    public Dog findById(Long id) {
        return dogService.findById(id);
    }

    @Override
    public Dog createDog(Dog dog) {
        return dogService.createDog(dog);
    }

    @Override
    public void deleteDog(Long id) {
        dogService.deleteDog(id);
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        return dogService.updateDog(id, dog);
    }
}