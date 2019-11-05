package dao;

import lombok.RequiredArgsConstructor;
import model.Dog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@RequiredArgsConstructor
public class HibernateDogDao implements DogDao {
    private final SessionFactory sessionFactory;

    @Override
    public Dog findById(Long id) {
        List<Dog> resultList = session().createQuery("from Dog d left join fetch d.awards where d.id = :id", Dog.class)
                .setParameter("id", id).getResultList();
        if (!resultList.isEmpty())
            return resultList.get(0);
        return null;
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