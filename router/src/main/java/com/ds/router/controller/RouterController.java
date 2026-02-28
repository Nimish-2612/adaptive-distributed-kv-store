package com.ds.router.controller;

import com.ds.router.hash.ConsistentHashRing;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RouterController {
    private int getReplicaCount(int accessCount) {
        if (accessCount < 5) return 1;
        if (accessCount <= 10) return 2;
        return 3;
    }
    private final ConcurrentHashMap<String, AtomicInteger> accessCount =
            new ConcurrentHashMap<>();
    private final ConsistentHashRing ring = new ConsistentHashRing();
    private final RestTemplate restTemplate = new RestTemplate();

    public RouterController() {
        ring.addNode("http://localhost:8081");
        ring.addNode("http://localhost:8082");
        ring.addNode("http://localhost:8083");
        System.out.println("Router initialized");
    }

    @PutMapping("/put")
    public String put(@RequestParam String key,
                      @RequestParam String value) {

        int count = accessCount.getOrDefault(key, new AtomicInteger(0)).get();
        int replicas = getReplicaCount(count);

        String primaryNode = ring.getNode(key);

        // Always store in primary node
        restTemplate.put(primaryNode + "/put?key=" + key + "&value=" + value, null);

        // Store in additional nodes if needed
        if (replicas > 1) {
            for (String node : new String[]{
                    "http://localhost:8081",
                    "http://localhost:8082",
                    "http://localhost:8083"
            }) {

                if (!node.equals(primaryNode)) {
                    restTemplate.put(node + "/put?key=" + key + "&value=" + value, null);
                    replicas--;
                    if (replicas == 1) break;
                }
            }
        }

        return "Stored with " + getReplicaCount(count) + " replica(s)";
    }
    @GetMapping("/get")
    public String get(@RequestParam String key) {

        accessCount
                .computeIfAbsent(key, k -> new AtomicInteger(0))
                .incrementAndGet();

        System.out.println("Access count for " + key + " = " +
                accessCount.get(key).get());

        String node = ring.getNode(key);

        return restTemplate.getForObject(
                node + "/get?key=" + key,
                String.class
        );
    }
}