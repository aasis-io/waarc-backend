package com.waarc.siteSetting;

import com.waarc.siteSetting.pojo.SiteSettingRequest;

import java.util.List;
import java.util.Optional;

public interface SiteSettingRepository {

    Optional<SiteSetting> getSiteSetting();
    SiteSetting updateSiteSetting(SiteSettingRequest request);

}