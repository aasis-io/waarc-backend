package com.waarc.siteSetting;

import com.waarc.siteSetting.pojo.SiteSettingRequest;

import java.util.List;
import java.util.Optional;

public interface SiteSettingService {

    SiteSetting getSiteSetting();

    SiteSetting updateSiteSetting(SiteSettingRequest request);
}