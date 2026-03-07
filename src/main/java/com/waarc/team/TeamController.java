package com.waarc.team;

import com.waarc.MiddleWare.AuthMiddleWare;
import io.javalin.Javalin;

public class TeamController {
    private final TeamService service = new TeamServiceImpl();

    public TeamController(Javalin app){
        app.before("/team", AuthMiddleWare.requireLogin);

        app.get("/getTeam",ctx -> {
           ctx.json(service.getTeam(ctx));
        });

        //to get individual team member
        app.get("/team",ctx -> {
            ctx.json(service.getTeamMember(ctx));
                });
        app.post("/team", ctx -> {
            ctx.json(service.save(ctx));
        });

        app.put("/team", ctx -> {
            ctx.json(service.updateTeam(ctx));
        });
        app.delete("/team", ctx -> {
            ctx.json(service.deleteTeam(ctx));
        });
    }
}
