package com.ds.kvnode.controller;

import com.ds.kvnode.store.KeyValueStore;
import org.springframework.web.bind.annotation.*;

@RestController
public class KVController {

    @PutMapping("/put")
    public String put(@RequestParam String key,
                      @RequestParam String value) {

        KeyValueStore.put(key, value);
        return "Stored key=" + key;
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        String value = KeyValueStore.get(key);
        return value != null ? value : "NULL";
    }
}
