package ua.training.top.util.refresh.salary;

import static java.lang.String.valueOf;

public class RateUtil {
    public static final float HRN_TO_USD_RATE = 28.5f;
    public static final float PLN_TO_USD_RATE = 3.7f;
    public static final float EUR_TO_USD_RATE = 1.2f;

    public static String eurToUsd(String eur) throws NumberFormatException{
        return checkValue((int) (Integer.parseInt(eur) * EUR_TO_USD_RATE) * 100);
    }

    public static String hrnToUsd(String hrn) throws NumberFormatException{
        return checkValue((int) (Integer.parseInt(hrn) / HRN_TO_USD_RATE) * 100);
    }

    public static String plnToUsd(String pln) throws NumberFormatException{
        return checkValue((int) ((Float.parseFloat(pln) * 100) / PLN_TO_USD_RATE));
    }

    public static String checkValue(int value){
        return  value > 40000000 ? valueOf((int) (value / 100)) : valueOf(value);
    }

}

//https://bank.gov.ua/ua/markets/exchangerates?date=01.12.2020&period=daily
//2020-12-01

//   salary: 6.0k-8.0k (UoP) PLN / month
//    salary: 10.9k-17.1k (b2b) pln / month
//    salary: 3.8k-4.3k (uz) pln / month
//    salary: 12.0k-18.0k (b2b) pln / month
//    salary: 15.1k-20.1k (b2b) pln / month
