package helper;

import com.github.javafaker.Faker;
import dataCard.CardInformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataHelper {
    private DataHelper() {
    }
    public static Faker faker = new Faker();

    public static String getValidFullName() {
        return faker.name().fullName();
    }

    public static String getMonth() {
        var months = new String[] {
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        };
        return months[new Random().nextInt(months.length)];
    }

    public static String getInvalidMonth(){
        int date = faker.number().numberBetween(13, 25);
        return String.valueOf(date);
    }

    public static String getInvalidFormatMonth() {
        int date = faker.number().numberBetween(0, 10);
        return String.valueOf(date);
    }

    public static String getYear() throws ParseException {
        Date date = new SimpleDateFormat("yy").parse(LocalDate.now().plusYears(5).toString());
        String pattern = "yy";
        String format = new SimpleDateFormat(pattern).format(date);
        return format;
    }

    public static String getInvalidYear(){
        int date = faker.number().numberBetween(10, 23);
        return String.valueOf(date);
    }


    public static String getCVCCode() {
        return String.valueOf(faker.number().numberBetween(100, 999));
    }

    public static String getInvalidFormatCVV() {
        return String.valueOf(faker.number().numberBetween(1, 99));

    }
    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }
    public static String getShortCardNumber() {
        return "4444 4444 44";
    }

    public static CardInformation getApprovedCard() throws ParseException {
        return new CardInformation(getApprovedCardNumber(), getMonth(), getYear(), getValidFullName(), getCVCCode());
    }

    public static CardInformation getDeclinedCard() throws ParseException {
        return new CardInformation(getDeclinedCardNumber(), getMonth(), getYear(), getValidFullName(), getCVCCode());
    }
}