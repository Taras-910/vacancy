package ua.training.top.util.parser.data;

public class CorrectUrl {

    public static String getCorrectUrlYandex(String url) {
        url = url.replaceAll("amp;", "");
        return url;
    }

    public static String getCorrectUrl(String text, String url){
        String prefix = "";
        switch (text){
            case "work"       : prefix = "https://www.work.ua";
                break;
            case "jooble"     : prefix = "https://ua.jooble.org/jdp/";
                break;
            case "indeed"     : prefix = "https://ua.indeed.com/описание-вакансии?jk=";
                break;
            case "rabota"     : prefix = "https://rabota.ua";
                break;
            case "nofluffjobs": prefix = "https://nofluffjobs.com";
                break;
            case "habr"       : prefix = "https://career.habr.com";
                break;
            case "djinni"     : prefix = "https://djinni.co";
                break;
                    }
        StringBuilder sb = new StringBuilder(prefix);
        return sb.append(url).toString();
    }

}
