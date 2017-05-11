package org.kidinov.just_weather.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtil {
    private static volatile AtomicInteger id = new AtomicInteger();
    private static final Random random = new SecureRandom();

    public static String generateString(int length) {
        return String.format("%" + length + "s", new BigInteger(length * 5, random)
                .toString(32))
                .replace('\u0020', '0');
    }

    public static String generateEmail() {
        return String.format("%s@%s.com", TestUtil.generateString(5), TestUtil.generateString(5));
    }

    public static long generateLong() {
        return random.nextLong();
    }

    public static int generateSmallInt() {
        return random.nextInt(100);
    }

    public static Integer generateId() {
        return id.incrementAndGet();
    }

    public static boolean generateBoolean() {
        return random.nextBoolean();
    }

    public static float generateFloat() {
        return random.nextFloat() * 100;
    }

    public static double generateDouble() {
        return random.nextDouble() * 100;
    }

}
