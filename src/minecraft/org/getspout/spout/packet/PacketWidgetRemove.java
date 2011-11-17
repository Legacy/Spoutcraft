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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.Widget;
import org.spoutcraft.spoutcraftapi.gui.WidgetType;

public class PacketWidgetRemove implements SpoutPacket {
	protected Widget widget;
	protected UUID screen;
	public PacketWidgetRemove() {

	}
	
	public PacketWidgetRemove(Widget widget, UUID screen) {
		this.widget = widget;
		this.screen = screen;
	}

	public int getNumBytes() {
		return widget.getNumBytes() + 20;
	}

	public void readData(DataInputStream input) throws IOException {
		int id = input.readInt();
		long msb = input.readLong();
		long lsb = input.readLong();
		screen = new UUID(msb, lsb);
		WidgetType widgetType = WidgetType.getWidgetFromId(id);
		if (widgetType != null) {
			try {
				widget = widgetType.getWidgetClass().newInstance();
				widget.readData(input);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(widget.getType().getId());
		output.writeLong(screen.getMostSignificantBits());
		output.writeLong(screen.getLeastSignificantBits());
		widget.writeData(output);
	}
	
	public void run(int playerId) {
		InGameHUD mainScreen = SpoutClient.getInstance().getActivePlayer().getMainScreen();
		PopupScreen popup = mainScreen.getActivePopup();
		Screen overlay = null;
		if(SpoutClient.getHandle().currentScreen != null) {
				overlay = SpoutClient.getHandle().currentScreen.getScreen();
		}

		//Determine if this is a popup screen and if we need to update it
		if (widget instanceof PopupScreen && popup.getId().equals(widget.getId())) {
			// Determine if this is a popup screen and if we need to update it
			mainScreen.closePopup();
		} else if (widget.getScreen() != null) {
			// Otherwise just remove it from the display
			widget.getScreen().removeWidget(widget);
		}
		//Determine if this is a widget on the overlay screen
		else if (overlay != null && screen.equals(overlay.getId())) {
				overlay.removeWidget(widget);
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketWidgetRemove;
	}
	
	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
		
	}
}
