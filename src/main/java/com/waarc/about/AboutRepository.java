package com.waarc.about;

import com.waarc.about.pojo.AboutRequest;

import java.util.List;
import java.util.Optional;

public interface AboutRepository {

    Optional<About> getAbout();
    About save(AboutRequest request);
    About updateAbout(AboutRequest request);
}
