package ua.training.top.util;

public class MessageUtil {
    public static final String
            url_matcher = "^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]" +
            "\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|" +
            "(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\" +
            "u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$",
            email_matcher = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            email_error = "[email] must be in the format of an email address",
            update_error_and_redirect = "update error {} and redirect to save",
            url_error = "[url] must be in URL or domain name format",
            check_error_data = "Error null data of resume {}",
            error_request = "{} at request {} {}",
            error_data = "ошибка данных",
            vacancy_not_be_null = "Vacancy must not be null ",
            user_not_be_null = "User must not be null ",
            freshen_not_be_null = "Freshen must not be null ",
            employer_not_be_null = "Employer must not be null ",
            email_not_be_null = "email must not be null",
            must_has_id = "Entity must has id",
            invite_sign_in = "?message=You are registered already. Please Sign in&username=",
            no_authorized_user_found = "No authorized user found",
            not_change_data = "Запрещено изменять учетные данные пользователя ",
            not_found = "Not found entity with {}",
            not_find_driver = "Could not find DB driver",
            not_own_data = "It's not allowed to change other user's data",
            setting_delay = "-".repeat(8)+" delay={} min {} sec"+"-".repeat(8),
            user_exist = "User already exist with this e-mail ",
            vacancy_exist_already = "Vacancy already exists, you could update it",
            exist_end_replace = "Vacancy {} already existed in the database but was replaced by {}",
            delay = "There is set delay within {} minutes",
            number_inform = "\n{} list.size={}\n",
            all = "all"
                    ;
}
