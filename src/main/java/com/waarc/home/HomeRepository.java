package com.waarc.home;
 
import com.waarc.home.pojo.HomeRequest;

import java.util.Optional;

public interface HomeRepository {

    Home getHome();

    String save(HomeRequest request);

    String updateHome(HomeRequest request);

    Optional<Home> getHomeById(String id);

}
