package com.jakob.leetcode.剑指offer.n14II;

import java.math.BigInteger;
import java.util.Arrays;

public class Solution {
    public int cuttingRope(int n) {
        BigInteger[] dp = new BigInteger[n + 1];
        Arrays.fill(dp, BigInteger.valueOf(0));
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                BigInteger max = max3(dp[i], dp[j].multiply(BigInteger.valueOf(i - j)), BigInteger.valueOf(j * (i - j)));
                dp[i] = max;
            }
        }
        return dp[n].mod(BigInteger.valueOf(1000000007)).intValue();
    }

    private BigInteger max3(BigInteger a, BigInteger b, BigInteger c) {
        int ab = a.compareTo(b);
        if (ab >= 0) {
            return a.compareTo(c) >= 0 ? a : c;
        } else {
            return b.compareTo(c) >= 0 ? b : c;
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().cuttingRope(10));
    }
}
