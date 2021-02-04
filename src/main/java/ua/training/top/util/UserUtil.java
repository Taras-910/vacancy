package ua.training.top.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import ua.training.top.model.Role;
import ua.training.top.model.User;

import java.util.Collections;

public class UserUtil {

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder, User userDb) {
        if(user.getRoles() == null) {
            user.setRoles(Collections.singleton(Role.USER));
        }
        String passwordDb = userDb == null ? null : userDb.getPassword();
        String password = user.getPassword();
        if(passwordDb == null) {
            user.setPassword(passwordEncoder.encode(password));
        } else {
            String cleared = passwordDb.replace("{noop}", "");
            if(!cleared.equals(password)) {
                // ToDo: send form confirm change password ('Are you sure?')
                user.setPassword(passwordEncoder.encode(password));
            }
            else {
                user.setPassword(passwordDb);
            }
        }
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
