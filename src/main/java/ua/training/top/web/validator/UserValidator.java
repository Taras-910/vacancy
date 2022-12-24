package ua.training.top.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.training.top.model.User;
import ua.training.top.repository.UserRepository;

import java.util.Optional;

import static ua.training.top.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

@Component
public class UserValidator implements Validator {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User userDB = Optional.ofNullable(userRepository.getByEmail(user.getEmail())).orElse(new User());

        if (userDB.getEmail() != null && !userDB.getId().equals(user.getId())) {
            errors.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}
