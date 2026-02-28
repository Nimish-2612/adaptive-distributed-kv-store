package com.ds.kvnode.store;

import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {

    private static final ConcurrentHashMap<String, String> store =
            new ConcurrentHashMap<>();

    public static void put(String key, String value) {
        store.put(key, value);
    }

    public static String get(String key) {
        return store.get(key);
    }
}
