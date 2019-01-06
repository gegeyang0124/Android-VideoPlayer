package com.zy.player;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PlayerDataSource {

    public static final String URL_KEY_DEFAULT = "URL_KEY_DEFAULT";

    public int currentUrlIndex;
    public LinkedHashMap urlsMap = new LinkedHashMap();
    public String title = "";
    public HashMap headerMap = new HashMap();
    public boolean looping = false;
    public Object[] objects;

    public PlayerDataSource(String url) {
        urlsMap.put(URL_KEY_DEFAULT, url);
        currentUrlIndex = 0;
    }

    public PlayerDataSource(String url, String title) {
        urlsMap.put(URL_KEY_DEFAULT, url);
        this.title = title;
        currentUrlIndex = 0;
    }

    public PlayerDataSource(Object url) {
        urlsMap.put(URL_KEY_DEFAULT, url);
        currentUrlIndex = 0;
    }

    public PlayerDataSource(LinkedHashMap urlsMap) {
        this.urlsMap.clear();
        this.urlsMap.putAll(urlsMap);
        currentUrlIndex = 0;
    }

    public PlayerDataSource(LinkedHashMap urlsMap, String title) {
        this.urlsMap.clear();
        this.urlsMap.putAll(urlsMap);
        this.title = title;
        currentUrlIndex = 0;
    }

    public Object getCurrentUrl() {
        return getValueFromLinkedMap(currentUrlIndex);
    }

    public Object getCurrentKey() {
        return getKeyFromDataSource(currentUrlIndex);
    }

    public String getKeyFromDataSource(int index) {
        int currentIndex = 0;
        for (Object key : urlsMap.keySet()) {
            if (currentIndex == index) {
                return key.toString();
            }
            currentIndex++;
        }
        return null;
    }

    public Object getValueFromLinkedMap(int index) {
        int currentIndex = 0;
        for (Object key : urlsMap.keySet()) {
            if (currentIndex == index) {
                return urlsMap.get(key);
            }
            currentIndex++;
        }
        return null;
    }

    public boolean containsTheUrl(Object object) {
        if (object != null) {
            return urlsMap.containsValue(object);
        }
        return false;
    }
}
