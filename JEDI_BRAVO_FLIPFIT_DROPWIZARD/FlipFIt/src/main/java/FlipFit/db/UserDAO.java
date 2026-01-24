package FlipFit.db;

import FlipFit.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Optional<User> findByName(String name) {
        return currentSession().createNamedQuery("FlipFit.core.User.findByName", User.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }

    public User create(User user) {
        currentSession().save(user); // Use save to explicitly ensure ID generation
        currentSession().flush();
        return user;
    }

    public User update(User user) {
        return persist(user);
    }

    public void delete(User user) {
        currentSession().delete(user);
    }

    public List<User> findAll() {
        return currentSession().createNamedQuery("FlipFit.core.User.findAll", User.class).getResultList();
    }
}