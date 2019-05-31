package me.rainny.reaper.factionutils.args;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.doctordark.utils.other.command.CommandArgument;

import me.rainny.reaper.HCF;
import me.rainny.reaper.factionutils.struct.Relation;
import me.rainny.reaper.factionutils.struct.Role;
import me.rainny.reaper.factionutils.type.PlayerFaction;

import java.util.UUID;

public class FactionLeaveArgument extends CommandArgument {

    private final HCF plugin;

    public FactionLeaveArgument(HCF plugin) {
        super("leave", "Leave your current faction.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can leave faction.");
            return true;
        }

        Player player = (Player) sender;
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        if (playerFaction.getMember(uuid).getRole() == Role.LEADER) {
            sender.sendMessage(ChatColor.RED + "You cannot leave factions as a leader. Either use " + ChatColor.GOLD + '/' + label + " disband" + ChatColor.RED + " or " + ChatColor.GOLD + '/' + label
                    + " leader" + ChatColor.RED + '.');

            return true;
        }

        if (playerFaction.removeMember(player, player, player.getUniqueId(), false, false)) {
            sender.sendMessage(ChatColor.GRAY + "Successfully left the faction.");
            playerFaction.broadcast(Relation.ENEMY.toChatColour() + sender.getName() + ChatColor.GRAY + " has left the faction.");
        }

        return true;
    }
}
