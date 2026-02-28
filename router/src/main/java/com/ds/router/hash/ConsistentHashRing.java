package com.ds.router.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashRing {

    private final SortedMap<Integer, String> ring = new TreeMap<>();
    private static final int VIRTUAL_NODES = 100;

    private int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes(StandardCharsets.UTF_8));
            int h = ((digest[0] & 0xFF) << 24)
                    | ((digest[1] & 0xFF) << 16)
                    | ((digest[2] & 0xFF) << 8)
                    | (digest[3] & 0xFF);
            return Math.abs(h);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNode(String nodeUrl) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            ring.put(hash(nodeUrl + "#" + i), nodeUrl);
        }
    }

    public String getNode(String key) {
        int hash = hash(key);

        SortedMap<Integer, String> tailMap = ring.tailMap(hash);
        Integer nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();

        return ring.get(nodeHash);
    }
}