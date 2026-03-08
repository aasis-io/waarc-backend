package com.waarc.about;

import com.waarc.about.pojo.AboutRequest;
import io.javalin.http.Context;

import java.util.List;

public interface AboutService {

    String getAbout(Context ctx);
    String save(Context ctx);
    String updateAbout(Context ctx);



}
