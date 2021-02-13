package systems.conduit.versions.v116.common.mixins.server;

import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.ScoreboardEvents;

import java.util.Optional;

/**
 * @author Innectic
 * @since 1/3/2021
 */
@Mixin(value = ServerScoreboard.class, remap = false)
public class ServerScoreboardMixin {

    @Inject(method = "onScoreChanged", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onScoreChanged(Score score, CallbackInfo ci) {
        ScoreboardEvents.ScoreChangedEvent event = new ScoreboardEvents.ScoreChangedEvent((ServerScoreboard) ((Object) this), score);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onPlayerRemoved", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onPlayerRemoved(String player, CallbackInfo ci) {
        ScoreboardEvents.PlayerRemovedFromScoreboardEvent event = new ScoreboardEvents.PlayerRemovedFromScoreboardEvent((ServerScoreboard) ((Object) this), player);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onPlayerScoreRemoved", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onPlayerScoreRemoved(String player, Objective objective, CallbackInfo ci) {
        ScoreboardEvents.PlayerScoreRemovedEvent event = new ScoreboardEvents.PlayerScoreRemovedEvent((ServerScoreboard) ((Object) this), objective, player);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "setDisplayObjective", at = @At(value = "TAIL", target = "Lnet/minecraft/world/scores/Scoreboard;setDisplayObjective(ILnet/minecraft/world/scores/Objective;)V"))
    public void setDisplayObjective(int slot, Objective objective, CallbackInfo ci) {
        ScoreboardEvents.ScoreboardSetDisplayObjectiveEvent event = new ScoreboardEvents.ScoreboardSetDisplayObjectiveEvent((ServerScoreboard) ((Object) this), slot, Optional.ofNullable(objective));
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "addPlayerToTeam", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void addPlayerToTeam(String player, PlayerTeam playerTeam, CallbackInfoReturnable<Boolean> cir) {
        ScoreboardEvents.AddPlayerToTeamEvent event = new ScoreboardEvents.AddPlayerToTeamEvent((ServerScoreboard) ((Object) this), player, playerTeam);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "removePlayerFromTeam", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void removePlayerFromTeam(String player, PlayerTeam playerTeam, CallbackInfo ci) {
        ScoreboardEvents.RemovePlayerFromTeamEvent event = new ScoreboardEvents.RemovePlayerFromTeamEvent((ServerScoreboard) ((Object) this), player, playerTeam);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onObjectiveAdded", at = @At(value = "HEAD", target = "Lnet/minecraft/world/scores/Scoreboard;onObjectiveAdded(Lnet/minecraft/world/scores/Objective;)V"))
    public void onObjectiveAdded(Objective objective, CallbackInfo ci) {
        ScoreboardEvents.ObjectiveAddedEvent event = new ScoreboardEvents.ObjectiveAddedEvent((ServerScoreboard) ((Object) this), objective);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onObjectiveChanged", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onObjectiveChanged(Objective objective, CallbackInfo ci) {
        ScoreboardEvents.ObjectiveChangedEvent event = new ScoreboardEvents.ObjectiveChangedEvent((ServerScoreboard) ((Object) this), objective);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onObjectiveRemoved", at = @At(value = "HEAD", target = "Lnet/minecraft/server/ServerScoreboard;stopTrackingObjective(Lnet/minecraft/world/scores/Objective;)V"))
    public void onObjectiveRemoved(Objective objective, CallbackInfo ci) {
        ScoreboardEvents.ObjectiveRemovedEvent event = new ScoreboardEvents.ObjectiveRemovedEvent((ServerScoreboard) ((Object) this), objective);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onTeamAdded", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onTeamAdded(PlayerTeam playerTeam, CallbackInfo ci) {
        ScoreboardEvents.TeamAddedEvent event = new ScoreboardEvents.TeamAddedEvent((ServerScoreboard) ((Object) this), playerTeam);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onTeamChanged", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onTeamChanged(PlayerTeam playerTeam, CallbackInfo ci) {
        ScoreboardEvents.TeamChangedEvent event = new ScoreboardEvents.TeamChangedEvent((ServerScoreboard) ((Object) this), playerTeam);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onTeamRemoved", at = @At(value = "HEAD", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onTeamRemoved(PlayerTeam playerTeam, CallbackInfo ci) {
        ScoreboardEvents.TeamRemovedEvent event = new ScoreboardEvents.TeamRemovedEvent((ServerScoreboard) ((Object) this), playerTeam);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
