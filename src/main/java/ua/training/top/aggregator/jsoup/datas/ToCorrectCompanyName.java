package ua.training.top.aggregator.jsoup.datas;

public class ToCorrectCompanyName {
    public static String getCorrectCompanyName(String companyName){
        companyName = companyName.isEmpty() ? " не указано" : companyName.trim();
        companyName = companyName.contains(" Профиль компании") ? companyName.replace("Профиль компании", "").trim() : companyName;
        companyName = companyName.contains(",") ? companyName.split(",")[0].trim() : companyName;
        return companyName;
    }
}
