package com.example.covidapp.ephId;

import com.example.covidapp.constant.AppConstant;
import com.example.covidapp.httprequest.KeyTimePair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EphemeralGenerator {
    // should load from property store
    public static boolean isDemo = true;


    public static String nextID(String secret, long time) {
        if (isDemo) {
            return generate(secret + time);
        } else {
            return generate(secret, time);
        }
    }

    public static String generateSecret() {
        if (isDemo) {
            return UUID.randomUUID().toString();
        } else {
            return null;
        }
    }

    public static List<String> getIDs(String secret, long time, int interval, int numberToGenerate) {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < numberToGenerate; i += 1) {
            ids.add(nextID(secret, time + (long) (i * interval)));
        }
        return ids;
    }

    public static List<String> getIDs(String secret, long time, int numberToGenerate) {
        return getIDs(secret, time, AppConstant.REGEN_RATE, numberToGenerate);
    }

    // n start with 1
    public static String getNthID(String secret, long time, int interval, int n) {
        if (n < 1)
            throw new IndexOutOfBoundsException();
        return nextID(secret, time + interval * (n - 1));
    }

    public static String getNthID(String secret, long time, int n) {
        return getNthID(secret, time, AppConstant.REGEN_RATE, n);
    }


    private static String generate(String s) {
        return UUID.nameUUIDFromBytes(s.getBytes()).toString();
    }

    private static String generate(String secret, long time) {
        return null;
    }
}


compare(KeyTimePair c){}
    List<string> = DB.getTempdata
    ListB = GenrateID(c.getserert, c.gettime, xx)
Collections.disjoin(list, listb);



