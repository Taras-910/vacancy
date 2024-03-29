package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.AuthorizedUser;
import ua.training.top.model.User;
import ua.training.top.repository.UserRepository;
import ua.training.top.util.exception.MethodNotAllowedException;
import ua.training.top.util.exception.UpdateRestrictionException;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.service.AggregatorService.herokuRestriction;
import static ua.training.top.util.MessageUtil.*;
import static ua.training.top.util.UserUtil.prepareToSave;
import static ua.training.top.util.ValidationUtil.*;

@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Service("userService")
public class UserService implements UserDetailsService {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(@NotEmpty User user) throws MethodNotAllowedException{
        log.info("create {}", user);
        Assert.notNull(user, not_be_null);
        checkNew(user);
        return prepareAndSave(user, null);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkModificationAllowed(id);
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Assert.notNull(email, email_not_be_null);
        return checkNotFound(repository.getByEmail(email), "mail " + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
            log.info("getAll");
            return repository.getAll();
        }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user, int id) throws MethodNotAllowedException{
        log.info("update {} with id={}", user, id);
        checkModificationAllowed(id);
        assureIdConsistent(user, id);
        User userDb = user.getId() == null ? null : repository.get(user.getId());
        checkNotFoundWithId(prepareAndSave(user, userDb), user.id());
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        checkModificationAllowed(id);
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(user);  // !! need only for JDBC implementation
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException(not_found + email);
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user, User userDb) {
        return repository.save(prepareToSave(user, passwordEncoder, userDb));
    }

    protected void checkModificationAllowed(int id) throws MethodNotAllowedException{
        if (herokuRestriction && id < START_SEQ + 2) {
            throw new UpdateRestrictionException();
        }
    }
}

