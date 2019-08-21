package endpoint;

import dao.DogDao;
import lombok.RequiredArgsConstructor;
import model.Dog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/dog")
public class DogEndpoint {
    private final DogDao dogDao;

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
        Dog dog = dogDao.findById(id);
        if (dog == null)
            return ResponseEntity.notFound().build();
        dogDao.deleteDog(id);
        return ResponseEntity.ok(dog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @Valid @RequestBody Dog updatedDog) {
        return ResponseEntity.ok(dogDao.updateDog(id, updatedDog));
    }
}