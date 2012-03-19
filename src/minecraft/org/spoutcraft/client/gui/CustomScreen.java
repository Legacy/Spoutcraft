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
package org.spoutcraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.spoutcraftapi.gui.*;

import org.spoutcraft.client.SpoutClient;

public class CustomScreen extends GuiScreen {
	public CustomScreen(PopupScreen screen) {
		update(screen);
		this.setWorldAndResolution(SpoutClient.getHandle(), (int) screen.getWidth(), (int) screen.getHeight());
		this.allowUserInput = true;
	}

	@Override
	public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.guiParticles = new GuiParticle(var1);
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;
		bg = (GenericGradient) new GenericGradient().setHeight(this.height).setWidth(this.width);
		this.initGui();
	}

	public void drawScreen(int x, int y, float z) {
		SpoutClient.enableSandbox();
		if (screen instanceof PopupScreen) {
			if (!((PopupScreen) screen).isTransparent()) {
				this.drawDefaultBackground();
			}
		}

		bg.setVisible(screen.isBgVisible());
		SpoutClient.disableSandbox();
		drawWidgets(x, y, z); //already sandboxed
	}

	@Override
	public boolean doesGuiPauseGame() {
		boolean oldLock = SpoutClient.enableSandbox();
		try {
			return ((PopupScreen) getScreen()).isPausingGame();
		} finally {
			SpoutClient.enableSandbox(oldLock);
		}
	}
}
