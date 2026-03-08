package com.waarc.about.pojo;

import com.waarc.about.WhyUs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author <sachin.singh@moco.com.np>
 * @created on : 01-03-2026 12:57
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AboutRequest {
    private int id;
    private String title;
    private String description;
    private String image;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private List<WhyUs> whyUs;

}