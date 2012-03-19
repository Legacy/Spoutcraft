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

import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;

public class SoundEffectsSlider extends GenericSlider {
	public SoundEffectsSlider() {
		super("Sound");
		setSliderPosition(Minecraft.theMinecraft.gameSettings.soundVolume);
		setTooltip("Sound Volume\nControls the volume of all the sound effects in game.");
	}

	@Override
	public void onSliderDrag(SliderDragEvent event) {
		Minecraft.theMinecraft.gameSettings.soundVolume = event.getNewPosition();
		Minecraft.theMinecraft.gameSettings.saveOptions();
		Minecraft.theMinecraft.sndManager.onSoundOptionsChanged();
	}

	public String getText() {
		return "Sound: " + (this.getSliderPosition() > 0F ? (int) (this.getSliderPosition() * 100) + "%" : "OFF");
	}
}
