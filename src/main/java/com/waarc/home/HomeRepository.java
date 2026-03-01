package com.waarc.home;
 
import com.waarc.home.pojo.HomeRequest;

public interface HomeRepository {

    Home getHome();

    String save(HomeRequest request);

    String updateHome(HomeRequest request, int id);
}
