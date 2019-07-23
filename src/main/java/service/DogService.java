package service;

import lombok.Getter;
import model.Dog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Arrays.asList;

@Service
public class DogService {
    private static AtomicLong id = new AtomicLong(3);

    @Getter
    private static List<Dog> dogsCollection = Collections.synchronizedList(new ArrayList<>(asList(
            new Dog().setId(1L).setName("bobik").setHeight(123).setWeight(12.4),
            new Dog().setId(2L).setName("sharick").setHeight(89).setWeight(8.9))
    ));

    public Dog findById(Long id) {
        return dogsCollection.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public Dog createDog(Dog dog) {
        dogsCollection.add(dog.setId(id.getAndIncrement()));
        return dog;
    }

    public Dog deleteDog(Long id) {
        Dog dog = findById(id);
        dogsCollection.remove(dog);
        return dog;
    }

    public Dog updateDog(Long id, Dog updatedDog) {
        Dog dog = findById(id);
        if (dog == null)
            return createDog(updatedDog);
        else
            return dog.setName(updatedDog.getName()).setWeight(updatedDog.getWeight())
                    .setHeight(updatedDog.getHeight()).setDate(updatedDog.getDate());
    }
}