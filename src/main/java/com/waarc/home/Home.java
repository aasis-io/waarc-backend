package com.waarc.home;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Home {

    private String bannerImage;
    private String title;
    private String description;
    private String metaTitle;
    private String metaKeywords;
    private String metaDescription;
}
