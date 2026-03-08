package com.waarc.link;



import java.util.List;

public interface LinkRepository {

    List<Link> getlink();
    String save(LinkRequest request);
    String updatelink(LinkRequest request,int id);

    Link getlinkById(String number);
    String deletelinkById(int number);
}
