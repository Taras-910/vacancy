package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.User;
import ua.training.top.repository.UserRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static ua.training.top.util.ValidationUtil.*;

@Service
public class UserService {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(@Valid @NotEmpty User user) {
        log.info("create {}", user);
        Assert.notNull(user, "user must not be null");
        checkNew(user);
        return repository.save(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        Assert.notNull(user, "user must not be null");
        checkNotFoundWithId(repository.save(user), user.id());
    }

//    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(user);  // !! need only for JDBC implementation
    }

/*
    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
*/

}
