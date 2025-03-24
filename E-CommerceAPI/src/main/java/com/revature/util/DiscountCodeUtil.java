package com.revature.util;

import java.util.Arrays;
import java.util.List;

public class DiscountCodeUtil extends CodeUtil{
    @Override
    protected List<int[]> getCharRanges() {
        return Arrays.asList(
                new int[]{97, 122},  // Lowercase (a-z)
                new int[]{65, 90},   // Uppercase (A-Z)
                new int[]{48, 57}    // Digits (0-9)
        );
    }
}
