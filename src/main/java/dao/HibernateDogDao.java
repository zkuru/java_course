package dao;

import lombok.RequiredArgsConstructor;
import model.Dog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@RequiredArgsConstructor
public class HibernateDogDao implements DogDao {
    private final SessionFactory sessionFactory;

    @Override
    public Dog findById(Long id) {
        return session().get(Dog.class, id);
    }

    @Override
    public Dog createDog(Dog dog) {
        Long id = (Long) session().save(dog);
        return dog.setId(id);
    }

    @Override
    public void deleteDog(Long id) {
        Dog dog = session().load(Dog.class, id);
        session().remove(dog);
    }

    @Override
    public Dog updateDog(Dog updatedDog) {
        session().saveOrUpdate(updatedDog);
        return updatedDog;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}