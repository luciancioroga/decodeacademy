package com.decode.gallery;

/**
 * Created by lucian.cioroga on 3/12/2018.
 */

public class U {
    public static String format(long duration) {
        long minutes = duration / 60000;
        long remainder = duration % 60000;
        long seconds = remainder / 1000;
        int ms = (int) Math.floor((remainder % 1000) / 100);
        return minutes + ":" + (seconds < 10 ? "0" : "") + seconds + "." + ms;
    }
}
