package ru.netology.treval;

import com.github.javafaker.Faker;

import java.time.LocalDate;

public class DataHelper {
    private static Faker faker = new Faker();

    public static class CardInfo {
        private String number;
        private String status;

        public CardInfo(String number, String status) {
            this.number = number;
            this.status = status;
        }

        public String getNumber() {
            return number;
        }

        public String getStatus() {
            return status;
        }
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String generateMonth() {
        return String.format("%02d", faker.number().numberBetween(1, 13));
    }

    public static String generateYear() {
        int currentYear = LocalDate.now().getYear() % 100;
        int randomOffset = faker.number().numberBetween(1, 4);
        return String.format("%02d", currentYear + randomOffset);
    }

    public static String generateOwnerName() {
        return faker.name().fullName();
    }

    public static String generateCvc() {
        return String.format("%03d", faker.number().numberBetween(100, 1000));
    }
}