package me.xihuxiaolong.library.utils;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/11/15.
 */

public class NumberUtils {

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
