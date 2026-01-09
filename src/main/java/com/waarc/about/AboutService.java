package com.waarc.about;

import com.waarc.about.pojo.AboutRequest;

import java.util.List;

public interface AboutService {

    About getAbout();
    About save(AboutRequest request);
    About updateAbout(AboutRequest request);



}
