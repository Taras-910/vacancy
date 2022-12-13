package ua.training.top.util;

public class MessageUtil {
    public static final String
            user_exist = "User already exist with this e-mail ",
            update_error_and_redirect = "update error {} and redirect to save",
            url_error = "[url] must be in URL or domain name format",
            url_matcher = "^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]" +
            "\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|" +
            "(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\" +
            "u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$",
            all = "all",
            delay = "There is set delay within {} minutes",
            delay_week_day_freshen = "There is set delay within {} minutes for Freshen {}",
            email_matcher = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            email_error = "[email] must be in the format of an email address",
            email_not_be_null = "email must not be null",
            exist_end_replace = "Vacancy {} already existed in the database but was replaced by {}",
            check_error_data = "Error null data of entity {}",
            error_request = "{} at request {} {}",
            error_data = "data error",
            invite_sign_in = "?message=You are registered already. Please Sign in&username=",
            must_has_id = "Entity must has id",
            no_authorized_user_found = "No authorized user found",
            not_change_data = "It is forbidden to change credentials of ",
            not_found = "Not found entity with {}",
            not_find_driver = "Could not find DB driver",
            not_be_null = " entity must not be null ",
            time_number_inform = "\n{} time: {} ms list.size={}\n",
            empty_inform = "No filtering records found, refresh DB",
            setting_delay = "-".repeat(8)+" delay={} min {} sec"+"-".repeat(8);

}
