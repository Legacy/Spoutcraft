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
package org.spoutcraft.client.gui.settings;

import net.minecraft.client.Minecraft;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;

import org.spoutcraft.client.config.ConfigReader;

public class ServerLightButton extends AutomatedCheckBox {
	public ServerLightButton() {
		super("Client Light");
		setChecked(ConfigReader.clientLight);
		setTooltip("Recalculates the light from servers in multiplayer.\n\nDisabling the recalculation is faster, but may result in odd\nlight patterns or light holes.");
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.clientLight = !ConfigReader.clientLight;
		ConfigReader.write();
	}

	@Override
	public String getTooltip() {
		if (Minecraft.theMinecraft.theWorld == null || Minecraft.theMinecraft.theWorld.isRemote) {
			return super.getTooltip();
		}
		return "Has no effect in Single Player";
	}
}
