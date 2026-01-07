package me.nihilanth.nocollision.team;

import me.nihilanth.nocollision.config.CollisionConfig;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class NoCollisionTeams {

    private static final String TEAM_NAME = "nocollision_players";

    public static void update(MinecraftServer server) {
        if (server == null) return;

        Scoreboard scoreboard = server.getScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);

        if (!CollisionConfig.playersCanCollide) {

            if (team == null) {
                team = scoreboard.addTeam(TEAM_NAME);
                team.setCollisionRule(Team.CollisionRule.NEVER);
                team.setFriendlyFireAllowed(true);
                team.setShowFriendlyInvisibles(false);
            }

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                String name = player.getNameForScoreboard();
                scoreboard.addScoreHolderToTeam(name, team);
            }

        } else {

            if (team != null) {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    String name = player.getNameForScoreboard();
                    if (scoreboard.getScoreHolderTeam(name) == team) {
                        scoreboard.removeScoreHolderFromTeam(name, team);
                    }
                }
            }
        }
    }
}
