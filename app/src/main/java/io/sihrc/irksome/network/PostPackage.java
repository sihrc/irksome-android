package io.sihrc.irksome.network;

import java.util.HashMap;

/**
 * Created by Chris on 9/5/15.
 */
public class PostPackage extends HashMap<String, Object> {
    String[] keys;

    public PostPackage() {
        super();
    }

    public static PostPackage keys(String... keys) {
        PostPackage postPackage = new PostPackage();
        postPackage.keys = keys;
        return postPackage;
    }

    public PostPackage values(Object... values) {
        for (int i = 0; i < keys.length; i++) {
            put(keys[i], values[i]);
        }

        return this;
    }
}
