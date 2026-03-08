package com.waarc.link;

import io.javalin.http.Context;


public interface LinkService {

    String getLink(Context ctx);
    String save(Context ctx);
    String updateLink(Context ctx);
    String getIndividualLink( Context ctx);
    String deleteLink(Context ctx);
}
