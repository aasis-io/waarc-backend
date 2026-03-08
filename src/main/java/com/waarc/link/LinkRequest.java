package com.waarc.link;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author <sachin.singh@moco.com.np>
 * @created on : 08-03-2026 11:08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkRequest {
    private int id;
    private String title;
    private String link;
}