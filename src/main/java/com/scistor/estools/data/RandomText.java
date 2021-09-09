package com.scistor.estools.data;

import java.util.Random;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-07 15:29
 */
public class RandomText {

    public static String getRandomText(String[] strs) {
        Random random = new Random();
        String text = strs[random.nextInt(strs.length)];
        return text;
    }

    public static void main(String[] args) {
        String[] strings = {"我爱中国","线路","中国","线路2","大地","为什么"};
        String randomText = getRandomText(strings);
        System.out.println(randomText);
    }

}
