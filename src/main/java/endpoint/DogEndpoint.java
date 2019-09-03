package endpoint;

import lombok.RequiredArgsConstructor;
import model.Dog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.DogService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/dog")
public class DogEndpoint {
    private final DogService dogService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Dog> getDog(@Valid @PathVariable Long id) {
        Dog dog = dogService.findById(id);
        if (dog == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dog);
    }

    @PostMapping
    public ResponseEntity<Dog> createDog(@Valid @RequestBody Dog dog) {
        return ResponseEntity.status(201).body(dogService.createDog(dog));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable Long id) {
        Dog dog = dogService.findById(id);
        if (dog == null)
            return ResponseEntity.notFound().build();
        dogService.deleteDog(id);
        return ResponseEntity.ok(dog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @Valid @RequestBody Dog updatedDog) {
        return ResponseEntity.ok(dogService.updateDog(id, updatedDog));
    }
}