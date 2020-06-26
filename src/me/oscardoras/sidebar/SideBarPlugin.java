package me.oscardoras.sidebar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.oscardoras.spigotutils.BukkitPlugin;
import me.oscardoras.spigotutils.PlayerReloader;
import me.oscardoras.spigotutils.PlayerReloader.PlayerReloaderRunnable;
import me.oscardoras.spigotutils.PlayerReloader.Type;
import me.oscardoras.spigotutils.io.ConfigurationFile;

public final class SideBarPlugin extends BukkitPlugin {
	
	public static SideBarPlugin plugin;
	
	public SideBarPlugin() {
		plugin = this;
	}
	
	
	@Override
	public void onLoad() {
		SideBarCommand.list();
		SideBarCommand.add();
		SideBarCommand.remove();
		SideBarCommand.title();
		SideBarCommand.enable();
		SideBarCommand.disable();
	}
	
	protected ConfigurationFile config;
	public PlayerReloaderRunnable reloader;
	
	public SideBar mainSideBar = new SideBar();
	
	@Override
	public void onEnable() {
		config = new ConfigurationFile(Bukkit.getWorlds().get(0).getWorldFolder() + "/data/sidebar.yml");
		
		loadSideBar();
		
		reloader = PlayerReloader.register(this, (player, location, type) -> {
			if (type == Type.JOIN || type == Type.TIMER || type == Type.RESPAWN || type == Type.MANUAL)
				if (mainSideBar.enabled) {
					Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
					Objective objective = scoreboard.registerNewObjective(player.getName(), "dummy", mainSideBar.title);
					objective.setDisplaySlot(DisplaySlot.SIDEBAR);
					for (Objective o : mainSideBar.objectives) {
						try {
							Score score = o.getScore(player.getName());
							if (score.isScoreSet()) objective.getScore(o.getDisplayName()).setScore(score.getScore());
						} catch (IllegalStateException e) {}
					}
					player.setScoreboard(scoreboard);
				} else player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}, 10L);
	}
	
	@Override
	public void onDisable() {
		saveSideBar();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(WorldLoadEvent e) {
		if (e.getWorld().equals(Bukkit.getWorlds().get(0))) loadSideBar();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(WorldSaveEvent e) {
		if (e.getWorld().equals(Bukkit.getWorlds().get(0))) saveSideBar();
	}
	
	
	private void loadSideBar() {
		mainSideBar = new SideBar();
		if (config.contains("title")) mainSideBar.title = config.getString("title");
		if (config.contains("active")) mainSideBar.enabled = config.getBoolean("active");
		if (config.contains("objectives")) {
			Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
			for (String name : config.getStringList("objectives")) {
				Objective objective = scoreboard.getObjective(name);
				if (objective != null && !mainSideBar.objectives.contains(objective)) mainSideBar.objectives.add(objective);
			}
		}
	}
	
	private void saveSideBar() {
		config.set("title", mainSideBar.title);
		config.set("active", mainSideBar.enabled);
		List<String> list = new ArrayList<String>();
		for (Objective objective : mainSideBar.objectives) {
			try {
				String name = objective.getName();
				if (!list.contains(name)) list.add(name);
			} catch (IllegalStateException e) {}
		}
		config.set("objectives", list);
		config.save();
	}
	
}