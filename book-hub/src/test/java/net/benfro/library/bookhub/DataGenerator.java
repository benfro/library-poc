package net.benfro.library.bookhub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataGenerator {
    public static final Faker FAKER = new Faker();
    private static final SplittableRandom RANDOM = new SplittableRandom(System.currentTimeMillis());

    public static String ISBN() {
        return String.valueOf(RANDOM.nextInt(978, 980)) + '-' +
            RANDOM.nextInt(1, 10) + '-' +
            RANDOM.nextInt(10, 21) + '-' +
            RANDOM.nextInt(100000, 999999) + '-' +
            RANDOM.nextInt(1, 10);
    }

    public static String randStr(int length, String valueSpace) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            b.append(valueSpace.charAt(RANDOM.nextInt(valueSpace.length())));
        }
        return String.valueOf(b);
    }

    @Test
    void faker_is_smart() {
        assertNotEquals(new Faker().idNumber().valid(), new Faker().idNumber().valid());
    }

    @Test
    void test_length() {
        assertEquals(10, randStr(10, "ABCDEFGHIJKLMNOPQRSTUVXYZ1234567890").length());
    }
}
