package de.hska.lkit.demo.web;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by patrickkoenig on 01.06.16.
 */

public class Timeline {


    private Map<Post, User> entries = new LinkedHashMap<>();

    public Map<Post, User> getEntries() {
        return entries;
    }

    public void setEntries(Map<Post, User> entries) {
        this.entries = entries;
    }


}
