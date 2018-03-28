package com.decode.gallery;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public static String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                }
            }
        }
        return null;
    }

    public static <T> T api(String url, Class<T> clasz) {
        return new Gson().fromJson(getJSON(url, 2000), clasz);
    }
}
