/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.addon;

import org.spoutcraft.spoutcraftapi.addon.Addon;

import org.spoutcraft.client.SpoutClient;

public class AddonInfo {
	private Addon addon;
	private int databaseId = -1;
	private boolean internetAccess;
	private boolean enabled = true;
	private long quota = 0;
	private boolean updateAvailable = false;
	private String name = "";

	public AddonInfo(String name) {
		this.name = name;
	}

	public Addon getAddon() {
		return addon;
	}

	public void setAddon(Addon addon) {
		this.addon = addon;
		SpoutClient.getInstance().getAddonStore().save();
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
		SpoutClient.getInstance().getAddonStore().save();
	}

	public boolean hasInternetAccess() {
		return internetAccess;
	}

	public void setHasInternetAccess(boolean mayAccessInternet) {
		this.internetAccess = mayAccessInternet;
		SpoutClient.getInstance().getAddonStore().save();
	}

	public long getQuota() {
		return quota;
	}

	public void setQuota(long quota) {
		this.quota = quota;
		SpoutClient.getInstance().getAddonStore().save();
	}

	public boolean hasUpdate() {
		return updateAvailable;
	}

	public void setHasUpdate(boolean updateAvailable) {
		this.updateAvailable = updateAvailable;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		SpoutClient.getInstance().getAddonStore().save();
	}

	public boolean isEnabled() {
		return enabled;
	}
}
