package com.waarc.about;

import com.waarc.about.pojo.AboutRequest;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AboutServiceImpl implements AboutService {
    private final AboutRepository aboutRepository = new AboutRepositoryImplementation();
    private final Logger log = LogManager.getLogger(AboutServiceImpl.class);

    @Override
    public About getAbout() {
        About about = aboutRepository.getAbout().orElseThrow(()-> new ResourceNotFoundException("About Not Found "));
        log.info("Getting About !");
        return new About(about.getId(),about.getDescription());
    }

    @Override
    public About save(AboutRequest request) {
        log.info("Creating About !");
        return aboutRepository.save(request);
    }

    @Override
    public About updateAbout(AboutRequest request ) {
        About about = aboutRepository.getAbout().orElseThrow(() -> new ResourceNotFoundException("About not found"));
        log.info("Updating About !");
        return aboutRepository.updateAbout(request);
    }
}
