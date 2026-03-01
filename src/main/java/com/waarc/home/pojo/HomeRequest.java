package com.waarc.home.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author <sachin.singh@moco.com.np>
 * @created on : 01-03-2026 12:12
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeRequest {

  private String bannerImage;
  private String title;
  private String description;
  private String metaTitle;
  private String metaKeywords;
  private String metaDescription;
}