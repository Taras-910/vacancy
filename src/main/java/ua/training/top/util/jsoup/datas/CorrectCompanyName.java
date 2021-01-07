package ua.training.top.util.jsoup.datas;

public class CorrectCompanyName {
    public static String getCorrectCompanyName(String companyName){
        companyName = companyName.isEmpty() ? " не указано" : companyName.trim();
        companyName = companyName.contains(" Профиль компании") ? companyName.replace("Профиль компании", "").trim() : companyName;
        companyName = companyName.contains(",") ? companyName.split(",")[0].trim() : companyName;
        return companyName;
    }
}
