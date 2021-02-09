package ua.training.top.util.parser.data;

public class CorrectEmployerName {
    public static String getCorrectEmployerName(String companyName){
        companyName = companyName.isEmpty() ? "не указано" : companyName.trim();
        companyName = companyName.contains(" Профиль компании") ? companyName.replace("Профиль компании", "").trim() : companyName;
        companyName = companyName.contains(",") ? companyName.split(",")[0].trim() : companyName;
        return companyName;
    }
}
