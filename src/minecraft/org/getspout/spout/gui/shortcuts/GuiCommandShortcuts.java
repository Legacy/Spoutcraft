package org.getspout.spout.gui.shortcuts;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.Shortcut;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiScreen;

public class GuiCommandShortcuts extends GuiScreen {
	private GuiControls parentScreen;
	private Button doneButton;
	private Button addButton, removeButton, editButton;
	private Label titleLabel;
	private GuiShortcutsSlot slot;
	private SimpleKeyBindingManager manager = (SimpleKeyBindingManager)SpoutClient.getInstance().getKeyBindingManager();
	
	public GuiCommandShortcuts(GuiControls guiControls) {
		parentScreen = guiControls;
	}
	
	@Override
	public void initGui() {
		doneButton = new GenericButton("Done");
		doneButton.setHeight(20).setWidth(50);
		doneButton.setX(10).setY((int) (height-30));
		getScreen().attachWidget("Spoutcraft", doneButton);
		
		titleLabel = new GenericLabel("Command Shortcuts");
		titleLabel.setHeight(20).setWidth(200);
		titleLabel.setAlign(WidgetAnchor.TOP_CENTER);
		titleLabel.setX((int) (getScreen().getWidth()/2-100)).setY(10);
		getScreen().attachWidget("Spoutcraft", titleLabel);
		
		addButton = new GenericButton("Add Shortcut");
		addButton.setWidth(100).setHeight(20);
		addButton.setX((int) (doneButton.getX()+doneButton.getWidth()+10)).setY(doneButton.getY());
		getScreen().attachWidget("Spoutcraft", addButton);
		
		editButton = new GenericButton("Edit Shortcut");
		editButton.setWidth(100).setHeight(20);
		editButton.setX((int) (addButton.getX()+addButton.getWidth()+10)).setY(addButton.getY());
		getScreen().attachWidget("Spoutcraft", editButton);
		
		removeButton = new GenericButton("Remove Shortcut");
		removeButton.setWidth(100).setHeight(20);
		removeButton.setX((int) (editButton.getX()+editButton.getWidth()+10)).setY(editButton.getY());
		getScreen().attachWidget("Spoutcraft", removeButton);

		slot = new GuiShortcutsSlot(this);
		
		updateButtons();	
	}

	public void updateButtons() {
		Shortcut item = slot.getSelection();
		editButton.setEnabled(item != null);
		removeButton.setEnabled(item != null);
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
		slot.drawScreen(var1, var2, var3);
		super.drawScreen(var1, var2, var3);
	}
	
	@Override
	protected void buttonClicked(Button btn) {
		if(btn.equals(doneButton)){
			mc.displayGuiScreen(parentScreen);
			return;
		}
		if(btn.equals(addButton)){
			Shortcut item = new Shortcut();
			item.setTitle("");
			item.setKey(Keyboard.KEY_UNKNOWN.getKeyCode());
			editItem(item);
		}
		if(btn.equals(editButton)){
			editItem(slot.getSelection());
		}
		if(btn.equals(removeButton)){
			manager.unregisterShortcut(slot.getSelection());
			slot.updateSelection();
		}
	}

	public void editItem(Shortcut item) {
		GuiEditShortcut gui = new GuiEditShortcut(this, item);
		mc.displayGuiScreen(gui);
	}

	public SimpleKeyBindingManager getManager() {
		return manager;
	}
	
}
