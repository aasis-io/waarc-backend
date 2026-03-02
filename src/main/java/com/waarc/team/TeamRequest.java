package com.waarc.team;

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
public class TeamRequest {
private int id;
  private String image;
  private String name;
  private String position;
  private String location;

}