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
        session().createQuery("delete from Dog where id = :id")
                .setParameter("id", id).executeUpdate();
    }

    @Override
    public Dog updateDog(Long id, Dog updatedDog) {
        session().createQuery("update Dog set name = :name, date = :date, height = :height, weight = :weight " + "where id = :id")
                .setParameter("id", id)
                .setParameter("name", updatedDog.getName())
                .setParameter("date", updatedDog.getDate())
                .setParameter("height", updatedDog.getHeight())
                .setParameter("weight", updatedDog.getWeight())
                .executeUpdate();
        return updatedDog.setId(id);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}