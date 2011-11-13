/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout;

import org.bukkit.ChatColor;
import org.joda.time.DateTime;

public final class EasterEggs {
	
	public static String getEasterEggTitle(String user) {
		if (user.equalsIgnoreCase("Afforess")) {
			return ChatColor.DARK_BLUE + "Afforess";
		} if (user.equalsIgnoreCase("Wulfspider")) {
			return ChatColor.BLUE + "Wulfspider";
		} if (user.equalsIgnoreCase("alta189")) {
			return ChatColor.DARK_GREEN + "alta189";
		} if (user.equalsIgnoreCase("Raphfrk")) {
			return ChatColor.GREEN + "Raphfrk";
		} if (user.equalsIgnoreCase("narrowtux")) {
			return ChatColor.GOLD + "narrowtux";
		} if (user.equalsIgnoreCase("Top_Cat")) {
			return ChatColor.RED + "T" + ChatColor.DARK_RED + "o" + ChatColor.YELLOW + "p" + ChatColor.GREEN + "_" + ChatColor.DARK_GREEN + "C" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t";
		} if (user.equalsIgnoreCase("Olloth")) {
			return ChatColor.DARK_RED + "Olloth";
		} if (user.equalsIgnoreCase("Tips48")) {
			return ChatColor.AQUA + "Tips48";
		}
		return null;
	}
}
