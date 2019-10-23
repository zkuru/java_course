package dao;

import lombok.Getter;
import model.Dog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.qala.datagen.RandomShortApi.integer;
import static java.util.Arrays.asList;

public class InMemoryDogDao implements DogDao {
    private static AtomicLong id = new AtomicLong(3);

    @Getter
    private static List<Dog> dogsCollection = Collections.synchronizedList(new ArrayList<>(asList(
            new Dog().setId(1L).setName("bobik").setHeight(123).setWeight(integer()),
            new Dog().setId(2L).setName("sharick").setHeight(89).setWeight(integer()))
    ));

    public Dog findById(Long id) {
        return dogsCollection.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public Dog createDog(Dog dog) {
        dogsCollection.add(dog.setId(id.getAndIncrement()));
        return dog;
    }

    public void deleteDog(Long id) {
        Dog dog = findById(id);
        dogsCollection.remove(dog);
    }

    public Dog updateDog(Dog updatedDog) {
        Dog dog = findById(updatedDog.getId());
        if (dog == null)
            return createDog(updatedDog);
        else
            return dog.setName(updatedDog.getName()).setWeight(updatedDog.getWeight())
                    .setHeight(updatedDog.getHeight()).setDate(updatedDog.getDate());
    }
}