package org.getspout.spout.gui;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiCrafting;
import net.minecraft.src.GuiDispenser;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiFurnace;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiStats;
import net.minecraft.src.GuiVideoSettings;

public enum ScreenType {
	GAME_SCREEN(0),
	CHAT_SCREEN(1),
	CUSTOM_SCREEN(2),
	PLAYER_INVENTORY(3),
	CHEST_INVENTORY(4),
	DISPENSER_INVENTORY(5),
	FURNACE_INVENTORY(6),
	INGAME_MENU(7),
	OPTIONS_MENU(8),
	VIDEO_SETTINGS_MENU(9),
	CONTROLS_MENU(10),
	ACHIEVEMENTS_SCREEN(11),
	STATISTICS_SCREEN(12),
	WORKBENCH_INVENTORY(13),
	SIGN_SCREEN(14),
	GAME_OVER_SCREEN(15),
	SLEEP_SCREEN(16),
	UNKNOWN(-1);
	
	
	private final int code;
	private static Map<Integer, ScreenType> lookup = new HashMap<Integer, ScreenType>();
	private ScreenType(int code){
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
	
	public static ScreenType getType(int code){
		return lookup.get(code);
	}
	
        public static ScreenType getType(GuiScreen gui) {
            ScreenType screen = ScreenType.UNKNOWN;
            if (gui instanceof GuiChat) {
                screen = ScreenType.CHAT_SCREEN;
            }
            if (gui instanceof GuiSleepMP) {
                screen = ScreenType.SLEEP_SCREEN;
            }
            if (gui instanceof CustomScreen) {
                screen = ScreenType.CUSTOM_SCREEN;
            }
            if (gui instanceof GuiInventory) {
                screen = ScreenType.PLAYER_INVENTORY;
            }
            if (gui instanceof GuiChest) {
                screen = ScreenType.CHEST_INVENTORY;
            }
            if (gui instanceof GuiDispenser) {
                screen = ScreenType.DISPENSER_INVENTORY;
            }
            if (gui instanceof GuiFurnace) {
                screen = ScreenType.FURNACE_INVENTORY;
            }
            if (gui instanceof GuiIngameMenu) {
                screen = ScreenType.INGAME_MENU;
            }
            if (gui instanceof GuiOptions) {
                screen = ScreenType.OPTIONS_MENU;
            }
            if (gui instanceof GuiVideoSettings) {
                screen = ScreenType.VIDEO_SETTINGS_MENU;
            }
            if (gui instanceof GuiControls) {
                screen = ScreenType.CONTROLS_MENU;
            }
            if (gui instanceof GuiAchievements) {
                screen = ScreenType.ACHIEVEMENTS_SCREEN;
            }
            if (gui instanceof GuiCrafting) {
                screen = ScreenType.WORKBENCH_INVENTORY;
            }
            if (gui instanceof GuiGameOver) {
                screen = ScreenType.GAME_OVER_SCREEN;
            }
            if (gui instanceof GuiEditSign) {
                screen = ScreenType.SIGN_SCREEN;
            }
            if (gui instanceof GuiStats) {
                screen = ScreenType.STATISTICS_SCREEN;
            }
            return screen;
        }
        
	static {
		for(ScreenType type:values()){
			lookup.put(type.code, type);
		}
	}
}