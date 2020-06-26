package me.oscardoras.sidebar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.scoreboard.Objective;

import me.oscardoras.spigotutils.PlayerReloader.Type;
import me.oscardoras.spigotutils.command.v1_16_1_V1.Argument;
import me.oscardoras.spigotutils.command.v1_16_1_V1.CommandRegister;
import me.oscardoras.spigotutils.command.v1_16_1_V1.LiteralArgument;
import me.oscardoras.spigotutils.command.v1_16_1_V1.CommandRegister.CommandExecutorType;
import me.oscardoras.spigotutils.command.v1_16_1_V1.arguments.GreedyStringArgument;
import me.oscardoras.spigotutils.command.v1_16_1_V1.arguments.ScoreboardObjectiveArgument;

public final class SideBarCommand {
	private SideBarCommand() {}
	
	
	public static void list() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("list", new LiteralArgument("list"));
		CommandRegister.register("sidebar", arguments, new Permission("sidebar.command.sidebar"), CommandExecutorType.ALL, (cmd) -> {
			SideBar sideBar = SideBarPlugin.plugin.mainSideBar;
			List<String> list = new ArrayList<String>();
			for (Objective objective : sideBar.objectives) {
				try {
					list.add(objective.getName());
				} catch (Exception ex) {}
			}
			cmd.sendListMessage(list, new Object[] {new Message("command.sidebar.list.list")}, new Object[] {new Message("command.sidebar.list.empty")});
			return list.size();
		});
	}
	
	public static void add() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("add", new LiteralArgument("add"));
		arguments.put("objective", new ScoreboardObjectiveArgument());
		CommandRegister.register("sidebar", arguments, new Permission("sidebar.command.sidebar"), CommandExecutorType.ALL, (cmd) -> {
			SideBar sideBar = SideBarPlugin.plugin.mainSideBar;
			Objective objective = (Objective)  cmd.getArg(0);
			if (!sideBar.objectives.contains(objective)) sideBar.objectives.add(objective);
			for (Player player : Bukkit.getOnlinePlayers()) SideBarPlugin.plugin.reloader.onReload(player, player.getLocation(), Type.MANUAL);
			cmd.broadcastMessage(new Message("command.sidebar.add", objective.getName()));
			return 1;
		});
	}
	
	public static void remove() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("remove", new LiteralArgument("remove"));
		arguments.put("objective", new ScoreboardObjectiveArgument());
		CommandRegister.register("sidebar", arguments, new Permission("sidebar.command.sidebar"), CommandExecutorType.ALL, (cmd) -> {
			SideBar sideBar = SideBarPlugin.plugin.mainSideBar;
			Objective objective = (Objective)  cmd.getArg(0);
			if (sideBar.objectives.contains(objective)) sideBar.objectives.remove(objective);
			for (Player player : Bukkit.getOnlinePlayers()) SideBarPlugin.plugin.reloader.onReload(player, player.getLocation(), Type.MANUAL);
			cmd.broadcastMessage(new Message("command.sidebar.remove", objective.getName()));
			return 1;
		});
	}
	
	public static void title() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("title_literal", new LiteralArgument("title"));
		CommandRegister.register("sidebar", arguments, new Permission("sidebar.command.sidebar"), CommandExecutorType.ALL, (cmd) -> {
			cmd.sendMessage(new Message("command.sidebar.title.get", SideBarPlugin.plugin.mainSideBar.title));
			return 1;
		});
		
		arguments.put("title", new GreedyStringArgument());
		CommandRegister.register("sidebar", arguments, new Permission("sidebar.command.sidebar"), CommandExecutorType.ALL, (cmd) -> {
			SideBar sideBar = SideBarPlugin.plugin.mainSideBar;
			sideBar.title = (String) cmd.getArg(0);
			for (Player player : Bukkit.getOnlinePlayers()) SideBarPlugin.plugin.reloader.onReload(player, player.getLocation(), Type.MANUAL);
			cmd.broadcastMessage(new Message("command.sidebar.title.set", sideBar.title));
			return 1;
		});
	}
	
	public static void enable() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("enable", new LiteralArgument("enable"));
		CommandRegister.register("sidebar", arguments, new Permission("sidebar.command.sidebar"), CommandExecutorType.ALL, (cmd) -> {
			SideBarPlugin.plugin.mainSideBar.enabled = true;
			for (Player player : Bukkit.getOnlinePlayers()) SideBarPlugin.plugin.reloader.onReload(player, player.getLocation(), Type.MANUAL);
			cmd.broadcastMessage(new Message("command.sidebar.enable"));
			return 1;
		});
	}
	
	public static void disable() {
		LinkedHashMap<String, Argument<?>> arguments = new LinkedHashMap<>();
		arguments.put("disable", new LiteralArgument("disable"));
		CommandRegister.register("sidebar", arguments, new Permission("sidebar.command.sidebar"), CommandExecutorType.ALL, (cmd) -> {
			SideBarPlugin.plugin.mainSideBar.enabled = false;
			for (Player player : Bukkit.getOnlinePlayers()) SideBarPlugin.plugin.reloader.onReload(player, player.getLocation(), Type.MANUAL);
			cmd.broadcastMessage(new Message("command.sidebar.disable"));
			return 1;
		});
	}
	
}