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
package org.spoutcraft.client.gui.chat;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.ChatTextBox;
import org.spoutcraft.spoutcraftapi.gui.CheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.Label;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.gui.GuiSpoutScreen;

public class GuiChatSettings extends GuiSpoutScreen {
	private GuiScreen parent;
	private CheckBox checkShowMentions, checkShowJoins, checkShowColors, checkCloseOnDamage, checkGrabMouse, checkIgnorePeople, checkParseRegex;
	private Button buttonAdvancedMentions, buttonConfigureIgnores, buttonDone;
	private Label title;

	public GuiChatSettings(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void createInstances() {
		title = new GenericLabel("Chat Options");

		checkShowMentions = new GenericCheckBox("Show Highlights/Mentions");
		checkShowMentions.setTooltip("Highlight chat messages where your name or words you specified are mentioned.");
		checkShowMentions.setChecked(ConfigReader.highlightMentions);

		checkShowJoins = new GenericCheckBox("Show Joins");
		checkShowJoins.setTooltip("Show player join messages when a new player logs in.");
		checkShowJoins.setChecked(ConfigReader.showJoinMessages);

		checkShowColors = new GenericCheckBox("Show Color Help/Guide");
		checkShowColors.setTooltip("Shows an on-screen guide for chat colors.");
		checkShowColors.setChecked(ConfigReader.showChatColors);

		checkCloseOnDamage = new GenericCheckBox("Close Chat Window on Damage");
		checkCloseOnDamage.setTooltip("Close the chat screen if your player is damaged.\nYour message will be saved.");
		checkCloseOnDamage.setChecked(ConfigReader.showDamageAlerts);

		checkGrabMouse = new GenericCheckBox("Chat Window Grabs Mouse");
		checkGrabMouse.setTooltip("Opening the chat screen grabs the mouse.\nON, players can not look around - default behavior\nOFF, players can look and pan the screen during chat.");
		checkGrabMouse.setChecked(ConfigReader.chatGrabsMouse);

		checkIgnorePeople = new GenericCheckBox("Ignore List");
		checkIgnorePeople.setTooltip("Prevents displaying chat messages from players in your ignore list.");
		checkIgnorePeople.setChecked(ConfigReader.ignorePeople);

		checkParseRegex = new GenericCheckBox("Enable Regex Usage");
		checkParseRegex.setTooltip("Parse highlighted words and ignored players using regular expression syntax.");
		checkParseRegex.setChecked(ConfigReader.chatUsesRegex);

		buttonAdvancedMentions = new GenericButton("Advanced Options");
		buttonAdvancedMentions.setTooltip("Configure words to be highlighted.");

		buttonConfigureIgnores = new GenericButton("Configure");
		buttonConfigureIgnores.setTooltip("Configure people to ignore messages from.");

		buttonDone = new GenericButton("Done");

		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, title, checkShowColors, checkShowJoins, checkShowMentions, checkCloseOnDamage, checkGrabMouse, checkIgnorePeople, buttonAdvancedMentions, buttonConfigureIgnores, buttonDone, checkParseRegex);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(12);

		int top = title.getY() + 13;

		checkShowMentions.setGeometry(5, top, 200, 20);
		buttonAdvancedMentions.setGeometry(width - 205, top, 150, 20);
		top += 22;
		checkShowJoins.setGeometry(5, top, 200, 20);
		top += 22;
		checkShowColors.setGeometry(5, top, 200, 20);
		top += 22;
		checkCloseOnDamage.setGeometry(5, top, 200, 20);
		top += 22;
		checkGrabMouse.setGeometry(5, top, 200, 20);
		top += 22;
		checkIgnorePeople.setGeometry(5, top, 200, 20);
		buttonConfigureIgnores.setGeometry(width - 205, top, 150, 20);
		top += 22;
		checkParseRegex.setGeometry(5, top, 200, 20);
		top += 22;

		buttonDone.setGeometry(width - 205, height - 25, 200, 20);
	}

	@Override
	protected void buttonClicked(Button btn) {
		final ChatTextBox chat;
		if (Spoutcraft.getActivePlayer() != null) {
			chat = Spoutcraft.getActivePlayer().getMainScreen().getChatTextBox();
		} else {
			chat = null;
		}
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
			return;
		}
		Runnable save = new Runnable() {
			@Override
			public void run() {
				SpoutClient.getInstance().getChatManager().save();
				if (chat != null) {
					chat.reparse();
				}
			}
		};
		boolean regex = ConfigReader.chatUsesRegex;
		if (btn == buttonAdvancedMentions) {
			GuiListEdit editor = new GuiListEdit(save, "Highlight List", regex ? "You can use regular expressions." : "", this, SpoutClient.getInstance().getChatManager().wordHighlight);
			mc.displayGuiScreen(editor);
			return;
		}
		if (btn == buttonConfigureIgnores) {
			GuiListEdit editor = new GuiListEdit(save, "Ignore List", regex ? "You can use regular expressions." : "", this, SpoutClient.getInstance().getChatManager().ignorePeople);
			mc.displayGuiScreen(editor);
			return;
		}
		if (btn == checkShowMentions) {
			ConfigReader.highlightMentions = checkShowMentions.isChecked();
		}
		if (btn == checkShowJoins) {
			ConfigReader.showJoinMessages = checkShowJoins.isChecked();
			if (chat != null) {
				chat.reparse();
			}
		}
		if (btn == checkShowColors) {
			ConfigReader.showChatColors = checkShowColors.isChecked();
		}
		if (btn == checkCloseOnDamage) {
			ConfigReader.showDamageAlerts = checkCloseOnDamage.isChecked();
		}
		if (btn == checkIgnorePeople) {
			ConfigReader.ignorePeople = checkIgnorePeople.isChecked();
		}
		if (btn == checkGrabMouse) {
			ConfigReader.chatGrabsMouse = checkGrabMouse.isChecked();
		}
		if (btn == checkParseRegex) {
			ConfigReader.chatUsesRegex = checkParseRegex.isChecked();
			if (chat != null) {
				chat.reparse();
			}
		}
		ConfigReader.write();
	}
}
