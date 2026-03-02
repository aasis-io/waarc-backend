package com.waarc.team;
 
import java.util.List;


public interface TeamRepository {

    List<Team> getTeam();

    String save(TeamRequest request);

    String updateTeam(TeamRequest request);

    Team getTeamById(String id);

    String deleteTeam(String id);
}
