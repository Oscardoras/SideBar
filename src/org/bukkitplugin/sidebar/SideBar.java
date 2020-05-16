package org.bukkitplugin.sidebar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scoreboard.Objective;

public class SideBar {
	
	public boolean enabled = false;
	public String title = "Stats";
	public List<Objective> objectives = new ArrayList<Objective>();
	
	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof SideBar) {
			SideBar sideBar = (SideBar) object;
			return enabled == sideBar.enabled && title.equals(sideBar.title) && objectives.equals(sideBar.objectives);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
		hash *= 13 + new Boolean(enabled).hashCode();
		hash *= 27 + title.hashCode();
		hash *= 15 + objectives.hashCode();
		return hash;
	}
	
}