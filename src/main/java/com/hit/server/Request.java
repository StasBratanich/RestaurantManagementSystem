package com.hit.server;

import com.google.gson.JsonObject;

public class Request {
    private String action;
    private JsonObject jsonData;

    public Request(String action, JsonObject data) {
        this.action = action;
        this.jsonData = data;
    }

    public String getAction() {
        return action;
    }

    public JsonObject getData() {
        return jsonData;
    }
}

