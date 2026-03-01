package com.waarc.home;


import com.waarc.home.pojo.HomeRequest;
import io.javalin.http.Context;

public interface HomeService {
    String getHome(Context context);
    String save(Context context);
    String updateHome(Context context);
}
