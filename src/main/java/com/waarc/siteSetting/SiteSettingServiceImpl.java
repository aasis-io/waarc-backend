package com.waarc.siteSetting;

import com.waarc.exception.ResourceNotFoundException;
import com.waarc.siteSetting.pojo.SiteSettingRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class SiteSettingServiceImpl implements SiteSettingService {
    private final SiteSettingRepository siteSettingRepository = new SiteSettingRepositoryImplementation();
    private final Logger log = LogManager.getLogger(SiteSettingServiceImpl.class);

    @Override
    public SiteSetting getSiteSetting() {

        SiteSetting siteSetting = siteSettingRepository.getSiteSetting()
                .orElseThrow(() -> new ResourceNotFoundException("Site Setting Not Found   "));
        log.info("Getting Site Setting ");
        return new SiteSetting(siteSetting.getId(), siteSetting.getPhone(), siteSetting.getEmail(),
                siteSetting.getLocation(), siteSetting.getFacebook(),
                siteSetting.getInstagram(), siteSetting.getLinkedin(), siteSetting.getYoutube());
    }


    @Override
    public SiteSetting updateSiteSetting(SiteSettingRequest request) {
        SiteSetting siteSetting = siteSettingRepository.getSiteSetting().orElseThrow(()-> new ResourceNotFoundException("Site Setting Not Found!"));
        log.info("Updating Site Setting");
        return siteSettingRepository.updateSiteSetting(request);
    }

}