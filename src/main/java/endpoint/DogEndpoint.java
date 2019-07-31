package endpoint;

import dao.InMemoryDogDao;
import lombok.RequiredArgsConstructor;
import model.Dog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/dog")
public class DogEndpoint {
    private final InMemoryDogDao dogDao;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Dog> getDog(@Valid @PathVariable Long id) {
        Dog dog = dogDao.findById(id);
        if (dog == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dog);
    }

    @PostMapping
    public ResponseEntity<Dog> createDog(@Valid @RequestBody Dog dog) {
        return ResponseEntity.status(201).body(dogDao.createDog(dog));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable Long id) {
        Dog dog = dogDao.deleteDog(id);
        return dog == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @Valid @RequestBody Dog updatedDog) {
        return ResponseEntity.ok(dogDao.updateDog(id, updatedDog));
    }
}