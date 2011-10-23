package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PlayerControllerSP;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.WorldSettings;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen {

	private GuiScreen parentGuiScreen;
	private GuiTextField textboxWorldName;
	private GuiTextField textboxSeed;
	private String folderName;
	private String field_35364_f = "survival";
	private boolean field_35365_g = true;
	private boolean createClicked;
	private boolean field_35368_i;
	private GuiButton field_35366_j;
	private GuiButton field_35367_k;
	private GuiButton field_35372_s;
	private GuiButton field_35371_t;
	private String field_35370_u;
	private String field_35369_v;
	
	public static boolean normalWorld = true; //Spout world type


	public GuiCreateWorld(GuiScreen var1) {	
		this.parentGuiScreen = var1;
	}

	public void updateScreen() {
		this.textboxWorldName.updateCursorCounter();
		this.textboxSeed.updateCursorCounter();
	}

	public void initGui() {
		//Spout start
		Keyboard.enableRepeatEvents(true);
		normalWorld = true;
		//Spout end
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, var1.translateKey("selectWorld.create")));
		this.controlList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, var1.translateKey("gui.cancel")));
		this.controlList.add(this.field_35366_j = new GuiButton(2, this.width / 2 - 75, 100, 150, 20, var1.translateKey("selectWorld.gameMode")));
		this.controlList.add(this.field_35367_k = new GuiButton(3, this.width / 2 - 75, 172, 150, 20, var1.translateKey("selectWorld.moreWorldOptions")));
		this.controlList.add(this.field_35372_s = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, var1.translateKey("selectWorld.mapFeatures")));
		this.field_35372_s.enabled2 = false;
		this.controlList.add(this.field_35371_t = new GuiButton(5, this.width / 2 + 5, 100, 150, 20, var1.translateKey("selectWorld.mapType")));
		this.field_35371_t.enabled2 = false;
		this.field_35371_t.enabled = true; //Spout false->true
		this.textboxWorldName = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, var1.translateKey("selectWorld.newWorld"));
		this.textboxWorldName.isFocused = true;
		this.textboxWorldName.setMaxStringLength(32);
		this.textboxSeed = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, "");
		this.func_22129_j();
		this.func_35363_g();
	}

	private void func_22129_j() {
		this.folderName = this.textboxWorldName.getText().trim();
		char[] var1 = ChatAllowedCharacters.allowedCharactersArray;
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			char var4 = var1[var3];
			this.folderName = this.folderName.replace(var4, '_');
		}

		if(MathHelper.stringNullOrLengthZero(this.folderName)) {
			this.folderName = "World";
		}

		this.folderName = generateUnusedFolderName(this.mc.getSaveLoader(), this.folderName);
	}

	private void func_35363_g() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.field_35366_j.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.field_35364_f);
		this.field_35370_u = var1.translateKey("selectWorld.gameMode." + this.field_35364_f + ".line1");
		this.field_35369_v = var1.translateKey("selectWorld.gameMode." + this.field_35364_f + ".line2");
		this.field_35372_s.displayString = var1.translateKey("selectWorld.mapFeatures") + " ";
		if(this.field_35365_g) {
			this.field_35372_s.displayString = this.field_35372_s.displayString + var1.translateKey("options.on");
		} else {
			this.field_35372_s.displayString = this.field_35372_s.displayString + var1.translateKey("options.off");
		}

		//Spout start
		if (normalWorld) {
			this.field_35371_t.displayString = StringTranslate.getInstance().translateKey("selectWorld.mapType") + " " + StringTranslate.getInstance().translateKey("selectWorld.mapType.normal");
		}
		else {
			this.field_35371_t.displayString = StringTranslate.getInstance().translateKey("selectWorld.mapType") + " " + "Skylands";
		}
		//Spout end
	}

	public static String generateUnusedFolderName(ISaveFormat var0, String var1) {
		while(var0.getWorldInfo(var1) != null) {
			var1 = var1 + "-";
		}

		return var1;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 1) {
				this.mc.displayGuiScreen(this.parentGuiScreen);
			} else if(var1.id == 0) {
				this.mc.displayGuiScreen((GuiScreen)null);
				if(this.createClicked) {
					return;
				}

				this.createClicked = true;
				long var2 = (new Random()).nextLong();
				String var4 = this.textboxSeed.getText();
				if(!MathHelper.stringNullOrLengthZero(var4)) {
					try {
						long var5 = Long.parseLong(var4);
						if(var5 != 0L) {
							var2 = var5;
						}
					} catch (NumberFormatException var7) {
						var2 = (long)var4.hashCode();
					}
				}

				byte var9 = 0;
				if(this.field_35364_f.equals("creative")) {
					var9 = 1;
					this.mc.playerController = new PlayerControllerTest(this.mc);
				} else {
					this.mc.playerController = new PlayerControllerSP(this.mc);
				}

				this.mc.startWorld(this.folderName, this.textboxWorldName.getText(), new WorldSettings(var2, var9, this.field_35365_g));
				this.mc.displayGuiScreen((GuiScreen)null);
			} else if(var1.id == 3) {
				this.field_35368_i = !this.field_35368_i;
				this.field_35366_j.enabled2 = !this.field_35368_i;
				this.field_35372_s.enabled2 = this.field_35368_i;
				this.field_35371_t.enabled2 = this.field_35368_i;
				StringTranslate var8;
				if(this.field_35368_i) {
					var8 = StringTranslate.getInstance();
					this.field_35367_k.displayString = var8.translateKey("gui.done");
				} else {
					var8 = StringTranslate.getInstance();
					this.field_35367_k.displayString = var8.translateKey("selectWorld.moreWorldOptions");
				}
			} else if(var1.id == 2) {
				if(this.field_35364_f.equals("survival")) {
					this.field_35364_f = "creative";
				} else {
					this.field_35364_f = "survival";
				}

				this.func_35363_g();
			} else if(var1.id == 4) {
				this.field_35365_g = !this.field_35365_g;
				this.func_35363_g();
			}
			//Spout start
			else if (var1.id == 5) {
				normalWorld = !normalWorld;
				if (normalWorld) {
					this.field_35371_t.displayString = StringTranslate.getInstance().translateKey("selectWorld.mapType") + " " + StringTranslate.getInstance().translateKey("selectWorld.mapType.normal");
				}
				else {
					this.field_35371_t.displayString = StringTranslate.getInstance().translateKey("selectWorld.mapType") + " " + "Skylands";
				}
			}
			//Spout end

		}
	}

	protected void keyTyped(char var1, int var2) {
		if(this.textboxWorldName.isFocused && !this.field_35368_i) {
			this.textboxWorldName.textboxKeyTyped(var1, var2);
		} else if(this.textboxSeed.isFocused && this.field_35368_i) {
			this.textboxSeed.textboxKeyTyped(var1, var2);
		}

		if(var1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		((GuiButton)this.controlList.get(0)).enabled = this.textboxWorldName.getText().length() > 0;
		this.func_22129_j();
	}

	protected void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		if(!this.field_35368_i) {
			this.textboxWorldName.mouseClicked(var1, var2, var3);
		} else {
			this.textboxSeed.mouseClicked(var1, var2, var3);
		}

	}

	public void drawScreen(int var1, int var2, float var3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create"), this.width / 2, 20, 16777215);
		if(!this.field_35368_i) {
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.resultFolder") + " " + this.folderName, this.width / 2 - 100, 85, 10526880);
			this.textboxWorldName.drawTextBox();
			this.drawString(this.fontRenderer, this.field_35370_u, this.width / 2 - 100, 122, 10526880);
			this.drawString(this.fontRenderer, this.field_35369_v, this.width / 2 - 100, 134, 10526880);
		} else {
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterSeed"), this.width / 2 - 100, 47, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.seedInfo"), this.width / 2 - 100, 85, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, 10526880);
			this.textboxSeed.drawTextBox();
		}

		super.drawScreen(var1, var2, var3);
	}

	public void selectNextField() {
		if(this.textboxWorldName.isFocused) {
			this.textboxWorldName.setFocused(false);
			this.textboxSeed.setFocused(true);
		} else {
			this.textboxWorldName.setFocused(true);
			this.textboxSeed.setFocused(false);
		}

	}
}
