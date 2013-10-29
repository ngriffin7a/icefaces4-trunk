/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.application.showcase.util;

import java.util.Random;

/**
 * Simple class for generating random numbers.
 */
public class RandomNumberGenerator {

    private Random random;

    private static RandomNumberGenerator randomNumberGenerator;

    public static void main(String[] args) {
        int high = 100;
        int low = 50;
        Random random = new Random(System.currentTimeMillis());
        double rand = random.nextDouble();
        System.out.println("int rand " + rand);
        int x = (int) (rand * (high - low));
        System.out.println("int " + (x + low));
    }

    private RandomNumberGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    public static RandomNumberGenerator getInstance() {
        if (randomNumberGenerator != null) {
            return randomNumberGenerator;
        } else {
            randomNumberGenerator = new RandomNumberGenerator();
            return randomNumberGenerator;
        }
    }

    /**
     * Utility method for generating random double values in the range
     * specified by the low and high attributes. Used during the
     * storeIventory initialization.
     *
     * @param low  low range of the random number to be generated.
     * @param high high range of the random number to be generated.
     * @return random number in the between the numbers low and high.
     */
    public double getRandomDouble(double low, double high) {
        double rand = random.nextDouble();
        double x = rand * (high - low);
        return x + low;
    }

    /**
     * Utility method for generating random double values in the range
     * specified by the low and high attributes. Used during the
     * storeIventory initialization.
     *
     * @param low  low range of the random number to be generated.
     * @param high high range of the random number to be generated.
     * @return random number in the between the numbers low and high.
     */
    public int getRandomInteger(int low, int high) {

        return (int) getRandomDouble(low, high);
    }
}
