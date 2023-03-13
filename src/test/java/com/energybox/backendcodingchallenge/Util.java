package com.energybox.backendcodingchallenge;

import java.util.Random;

/** Utility class */
public final class Util {

    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyz";
    private static final int RANDOM_STRING_LENGTH = 3;

    /** Utility method for generating a random string of length 3 */
    public static String randomString() {
        StringBuffer randomString = new StringBuffer();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randomString.append(ch);
        }
        return randomString.toString();
    }

    private static int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }
}
