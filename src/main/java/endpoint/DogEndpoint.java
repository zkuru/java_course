package endpoint;

import model.Dog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping(path = "/dog")
public class DogEndpoint {
    private static Long id = 3L;

    private static List<Dog> dogsCollection = new ArrayList<>(asList(
            new Dog().setId(1L).setName("bobik").setHeight(123).setWeight(12.4),
            new Dog().setId(2L).setName("sharick").setHeight(89).setWeight(8.9))
    );

    @GetMapping(path = "/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        Dog dog = findOne(id);
        if (dog == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dog);
    }

    @PostMapping
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        dogsCollection.add(dog.setId(id++));
        return ResponseEntity.ok(dog);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id) {
        Dog dog = findOne(id);
        if (dog == null)
            return ResponseEntity.notFound().build();
        dogsCollection.remove(dog);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Dog>> getDogs() {
        return ResponseEntity.ok(dogsCollection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @RequestBody Dog updatedDog) {
        Dog dog = findOne(id);
        if (dog != null)
            dogsCollection.remove(dog);
        dogsCollection.add(updatedDog.setId(id));
        return ResponseEntity.ok(updatedDog);
    }

    private Dog findOne(@PathVariable Long id) {
        return dogsCollection.stream().filter(d -> id.equals(d.getId())).findFirst().orElse(null);
    }
}