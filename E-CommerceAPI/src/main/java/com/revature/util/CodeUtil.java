package com.revature.util;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public abstract class CodeUtil {

    private static final Random RANDOM = new SecureRandom();

    protected abstract List<int[]> getCharRanges();

    public String generateResetCode(int length) {
        if (length < getCharRanges().size()) {
            throw new IllegalArgumentException("The code should be contain at least " + getCharRanges().size() + " character.");
        }

        StringBuilder code = new StringBuilder(length);
        List<int[]> charRanges = getCharRanges();

        // Ensure at least one character for each type
        for (int[] range : charRanges) {
            code.append(getRandomChar(range[0], range[1]));
        }

        // generated randomly the rest of the characters
        for (int i = charRanges.size(); i < length; i++) {
            int[] range = charRanges.get(RANDOM.nextInt(charRanges.size()));
            code.append(getRandomChar(range[0], range[1]));
        }

        return code.toString();
    }

    private char getRandomChar(int start, int end) {
        return (char) (start + RANDOM.nextInt(end - start + 1));
    }
}
