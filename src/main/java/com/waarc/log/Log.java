package com.waarc.log;

import com.google.gson.Gson;
import lombok.*;

/**
 * @author <sachin.singh@moco.com.np>
 * @created on : 15-02-2026 09:21
 */

@AllArgsConstructor
public class Log {
    private Status status;
    private Section section;
    private String description;

    private static final Gson GSON = new Gson();

    public enum Status {
        SUCCESS,FAILED,PROCESSING
    }

    public enum Section{
        CONFIG
    }



    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}