package com.waarc.team;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Team {
    private int id;
    private String image;
    private String name;
    private String position;
    private String location;
}
