package ua.training.top.util.parser.data;

public class CorrectUrl {

    public static String getCorrectUrlYandex(String url) {
        url = url.replaceAll("amp;", "");
        return url;
    }

    public static String getCorrectUrl(String text, String url){
        String prefix = switch (text) {
            case "work" -> "https://www.work.ua";
            case "jooble" -> "https://ua.jooble.org/jdp/";
            case "indeed" -> "https://ua.indeed.com/описание-вакансии?jk=";
            case "rabota" -> "https://rabota.ua";
            case "nofluffjobs" -> "https://nofluffjobs.com";
            case "habr" -> "https://career.habr.com";
            case "djinni" -> "https://djinni.co";
            default -> "";
        };
        StringBuilder sb = new StringBuilder(prefix);
        return sb.append(url).toString();
    }

}
