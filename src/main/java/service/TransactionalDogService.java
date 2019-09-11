package service;

import lombok.RequiredArgsConstructor;
import model.Dog;
import utils.TransactionalProxy;

@RequiredArgsConstructor
public class TransactionalDogService implements DogService {
//    private final JdbcConnectionHolder connectionHolder;
    private final DogService dogService;

    @Override
    public Dog findById(Long id) {
        return dogService.findById(id);
    }

    @Override
    @TransactionalProxy
    public Dog createDog(Dog dog) {
        return dogService.createDog(dog);
    }

    @Override
    @TransactionalProxy
    public void deleteDog(Long id) {
        dogService.deleteDog(id);
    }

    @Override
    @TransactionalProxy
    public Dog updateDog(Long id, Dog dog) {
        return dogService.updateDog(id, dog);
    }
}