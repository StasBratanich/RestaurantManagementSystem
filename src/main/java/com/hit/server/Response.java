package com.hit.server;

import com.google.gson.JsonObject;

public class Response {
    private boolean success;
    private String action;
    private JsonObject jsonData;

    public Response(boolean status, String action, JsonObject data) {
        this.success = status;
        this.action = action;
        this.jsonData = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAction() {
        return action;
    }

    public JsonObject getJsonData() {
        return jsonData;
    }
}
