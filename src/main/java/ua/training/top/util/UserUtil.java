package ua.training.top.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ua.training.top.model.Role;
import ua.training.top.model.User;

import java.util.Collections;

public class UserUtil {

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

    public static final User withRoleUser(User user){
        user.setRoles(Collections.singleton(Role.USER));
        return user;
    }
}
