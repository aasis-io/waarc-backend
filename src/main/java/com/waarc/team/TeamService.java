package com.waarc.team;


import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public interface TeamService {
    String getTeam(Context context);
    String save(Context context);
    String updateTeam(Context context);
    String deleteTeam(Context context);

    String getTeamMember(@NotNull Context ctx);
}
