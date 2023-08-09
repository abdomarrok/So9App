package com.marrok.so9app.ui.Models;
import com.google.gson.annotations.SerializedName;

public class TokenCheckResponse {
    private String message;

    public TokenCheckResponse(String message) {
        this.message = message;
    }

    @SerializedName("")
    public String getMessage() {
        return message;
    }
}
