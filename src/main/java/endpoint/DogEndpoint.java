package endpoint;

import lombok.RequiredArgsConstructor;
import model.Dog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.DogService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/dog")
public class DogEndpoint {
    private final DogService dogService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        Dog dog = dogService.findById(id);
        if (dog == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dog);
    }

    @PostMapping
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        return ResponseEntity.ok(dogService.createDog(dog));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id) {
        return dogService.deleteDog(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @RequestBody Dog updatedDog) {
        return ResponseEntity.ok(dogService.updateDog(id, updatedDog));
    }
}