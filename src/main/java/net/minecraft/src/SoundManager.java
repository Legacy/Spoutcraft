package net.minecraft.src;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
//import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

// Spout Start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.*;
import org.spoutcraft.api.sound.Music;
import thedudeguy.paulscode.sound.codecs.CodecJOrbis;
// Spout End

import de.cuina.fireandfuel.CodecJLayerMP3;

public class SoundManager {

	/** A reference to the sound system. */
	private static SoundSystem sndSystem;

	/** Sound pool containing sounds. */
	private SoundPool soundPoolSounds = new SoundPool();

	/** Sound pool containing streaming audio. */
	private SoundPool soundPoolStreaming = new SoundPool();

	/** Sound pool containing music. */
	private SoundPool soundPoolMusic = new SoundPool();

	/**
	 * The last ID used when a sound is played, passed into SoundSystem to give active sounds a unique ID
	 */
	private int latestSoundID = 0;

	/** A reference to the game settings. */
	private GameSettings options;
	private Set field_82470_g = new HashSet();

	/** Set to true when the SoundManager has been initialised. */
	private static boolean loaded = false;

	/** RNG. */
	private Random rand = new Random();
	private int ticksBeforeMusic;
	// Spout Start
	private final int SOUND_EFFECTS_PER_TICK = 3;
	private int soundEffectsLimit = SOUND_EFFECTS_PER_TICK;
	// Spout End

	public SoundManager() {
		this.ticksBeforeMusic = this.rand.nextInt(12000);
	}

	/**
	 * Used for loading sound settings from GameSettings
	 */
	public void loadSoundSettings(GameSettings par1GameSettings) {
		this.soundPoolStreaming.isGetRandomSound = false;
		this.options = par1GameSettings;

		if (!loaded && (par1GameSettings == null || par1GameSettings.soundVolume != 0.0F || par1GameSettings.musicVolume != 0.0F)) {
			this.tryToSetLibraryAndCodecs();
		}
	}

	/**
	 * Tries to add the paulscode library and the relevant codecs. If it fails, the volumes (sound and music) will be set
	 * to zero in the options file.
	 */
	private void tryToSetLibraryAndCodecs() {
		try {
			float var1 = this.options.soundVolume;
			float var2 = this.options.musicVolume;
			this.options.soundVolume = 0.0F;
			this.options.musicVolume = 0.0F;
			this.options.saveOptions();
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("mus", CodecMus.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			// Spout Start
			SoundSystemConfig.setCodec("mp3", CodecJLayerMP3.class);
			// Spout  end
			sndSystem = new SoundSystem();
			this.options.soundVolume = var1;
			this.options.musicVolume = var2;
			this.options.saveOptions();
		} catch (Throwable var3) {
			var3.printStackTrace();
			System.err.println("error linking with the LibraryJavaSound plug-in");
		}

		loaded = true;
	}

	/**
	 * Called when one of the sound level options has changed.
	 */
	public void onSoundOptionsChanged() {
		if (!loaded && (this.options.soundVolume != 0.0F || this.options.musicVolume != 0.0F)) {
			this.tryToSetLibraryAndCodecs();
		}

		if (loaded) {
			if (this.options.musicVolume == 0.0F) {
				sndSystem.stop("BgMusic");
			} else {
				sndSystem.setVolume("BgMusic", this.options.musicVolume);
			}
		}
	}

	/**
	 * Called when Minecraft is closing down.
	 */
	public void closeMinecraft() {
		if (loaded) {
			sndSystem.cleanup();
		}
	}

	/**
	 * Adds a sounds with the name from the file. Args: name, file
	 */
	public void addSound(String par1Str, File par2File) {
		this.soundPoolSounds.addSound(par1Str, par2File);
	}

	/**
	 * Adds an audio file to the streaming SoundPool.
	 */
	public void addStreaming(String par1Str, File par2File) {
		this.soundPoolStreaming.addSound(par1Str, par2File);
	}

	/**
	 * Adds an audio file to the music SoundPool.
	 */
	public void addMusic(String par1Str, File par2File) {
		this.soundPoolMusic.addSound(par1Str, par2File);
	}

	/**
	 * If its time to play new music it starts it up.
	 */
	public void playRandomMusicIfReady() {
		if (loaded && this.options.musicVolume != 0.0F) {
			if (!sndSystem.playing("BgMusic") && !sndSystem.playing("streaming")) {
				if (this.ticksBeforeMusic > 0) {
					--this.ticksBeforeMusic;
					return;
				}

				SoundPoolEntry var1 = this.soundPoolMusic.getRandomSound();

				if (var1 != null) {
					// Spout Start
					if (SpoutClient.getInstance().isSpoutEnabled()) {
						EntityPlayer player = SpoutClient.getHandle().thePlayer;
						if (player instanceof EntityClientPlayerMP) {
							if (waitingSound == null) {
								Music music = Music.getMusicFromName(var1.soundName);
								if (music != null) {
									waitingSound = var1;
									SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketMusicChange(music.getId(), (int)options.musicVolume * 100));
									return;
								}
							}
							else if (allowed) {
								var1 = waitingSound;
								waitingSound = null;
								allowed = false;
								cancelled = false;
							}
							else if (cancelled) {
								var1 = null;
								allowed = false;
								cancelled = false;
								ticksBeforeMusic = rand.nextInt(12000) + 12000;
								return;
							}
							else {
								return;
							}
						}
					}
					// Spout End
					this.ticksBeforeMusic = this.rand.nextInt(12000) + 12000;
					sndSystem.backgroundMusic("BgMusic", var1.soundUrl, var1.soundName, false);
					sndSystem.setVolume("BgMusic", this.options.musicVolume);
					sndSystem.play("BgMusic");
				}
			}
		}
	}

	/**
	 * Sets the listener of sounds
	 */
	public void setListener(EntityLiving par1EntityLiving, float par2) {
		if (loaded && this.options.soundVolume != 0.0F) {
			if (par1EntityLiving != null) {
				float var3 = par1EntityLiving.prevRotationYaw + (par1EntityLiving.rotationYaw - par1EntityLiving.prevRotationYaw) * par2;
				double var4 = par1EntityLiving.prevPosX + (par1EntityLiving.posX - par1EntityLiving.prevPosX) * (double)par2;
				double var6 = par1EntityLiving.prevPosY + (par1EntityLiving.posY - par1EntityLiving.prevPosY) * (double)par2;
				double var8 = par1EntityLiving.prevPosZ + (par1EntityLiving.posZ - par1EntityLiving.prevPosZ) * (double)par2;
				float var10 = MathHelper.cos(-var3 * 0.017453292F - (float)Math.PI);
				float var11 = MathHelper.sin(-var3 * 0.017453292F - (float)Math.PI);
				float var12 = -var11;
				float var13 = 0.0F;
				float var14 = -var10;
				float var15 = 0.0F;
				float var16 = 1.0F;
				float var17 = 0.0F;
				sndSystem.setListenerPosition((float)var4, (float)var6, (float)var8);
				sndSystem.setListenerOrientation(var12, var13, var14, var15, var16, var17);
			}
		}
	}

	public void func_82464_d() {
		Iterator var1 = this.field_82470_g.iterator();

		while (var1.hasNext()) {
			String var2 = (String)var1.next();
			sndSystem.stop(var2);
		}

		this.field_82470_g.clear();
	}

	public void playStreaming(String par1Str, float par2, float par3, float par4) {
		if (loaded && (this.options.soundVolume != 0.0F || par1Str == null)) {
			String var5 = "streaming";

			if (sndSystem.playing(var5)) {
				sndSystem.stop(var5);
			}

			if (par1Str != null) {
				SoundPoolEntry var6 = this.soundPoolStreaming.getRandomSoundFromSoundPool(par1Str);

				if (var6 != null) {
					if (sndSystem.playing("BgMusic")) {
						sndSystem.stop("BgMusic");
					}

					float var7 = 16.0F;
					sndSystem.newStreamingSource(true, var5, var6.soundUrl, var6.soundName, false, par2, par3, par4, 2, var7 * 4.0F);
					sndSystem.setVolume(var5, 0.5F * this.options.soundVolume);
					sndSystem.play(var5);
				}
			}
		}
	}

	public void func_82460_a(Entity par1Entity) {
		this.func_82462_a(par1Entity, par1Entity);
	}

	public void func_82462_a(Entity par1Entity, Entity par2Entity) {
		String var3 = "entity_" + par1Entity.entityId;

		if (this.field_82470_g.contains(var3)) {
			if (sndSystem.playing(var3)) {
				sndSystem.setPosition(var3, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ);
				sndSystem.setVelocity(var3, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
			} else {
				this.field_82470_g.remove(var3);
			}
		}
	}

	public boolean func_82465_b(Entity par1Entity) {
		if (par1Entity != null && loaded && this.options.musicVolume != 0.0F) {
			String var2 = "entity_" + par1Entity.entityId;
			return sndSystem.playing(var2);
		} else {
			return false;
		}
	}

	public void func_82469_c(Entity par1Entity) {
		if (par1Entity != null && loaded && this.options.musicVolume != 0.0F) {
			String var2 = "entity_" + par1Entity.entityId;

			if (this.field_82470_g.contains(var2)) {
				if (sndSystem.playing(var2)) {
					sndSystem.stop(var2);
				}

				this.field_82470_g.remove(var2);
			}
		}
	}

	public void func_82468_a(Entity par1Entity, float par2) {
		if (par1Entity != null && loaded && this.options.musicVolume != 0.0F) {
			if (loaded && this.options.soundVolume != 0.0F) {
				String var3 = "entity_" + par1Entity.entityId;

				if (sndSystem.playing(var3)) {
					sndSystem.setVolume(var3, par2 * this.options.soundVolume);
				}
			}
		}
	}

	public void func_82463_b(Entity par1Entity, float par2) {
		if (par1Entity != null && loaded && this.options.musicVolume != 0.0F) {
			if (loaded && this.options.soundVolume != 0.0F) {
				String var3 = "entity_" + par1Entity.entityId;

				if (sndSystem.playing(var3)) {
					sndSystem.setPitch(var3, par2);
				}
			}
		}
	}

	public void func_82467_a(String par1Str, Entity par2Entity, float par3, float par4, boolean par5) {
		if (par2Entity != null) {
			if (loaded && (this.options.soundVolume != 0.0F || par1Str == null)) {
				String var6 = "entity_" + par2Entity.entityId;

				if (this.field_82470_g.contains(var6)) {
					this.func_82460_a(par2Entity);
				} else {
					if (sndSystem.playing(var6)) {
						sndSystem.stop(var6);
					}

					if (par1Str == null) {
						return;
					}

					SoundPoolEntry var7 = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);

					if (var7 != null && par3 > 0.0F) {
						float var8 = 16.0F;

						if (par3 > 1.0F) {
							var8 *= par3;
						}

						sndSystem.newSource(par5, var6, var7.soundUrl, var7.soundName, false, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ, 2, var8);
						sndSystem.setLooping(var6, true);
						sndSystem.setPitch(var6, par4);

						if (par3 > 1.0F) {
							par3 = 1.0F;
						}

						sndSystem.setVolume(var6, par3 * this.options.soundVolume);
						sndSystem.setVelocity(var6, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
						sndSystem.play(var6);
						this.field_82470_g.add(var6);
					}
				}
			}
		}
	}

	/**
	 * Plays a sound. Args: soundName, x, y, z, volume, pitch
	 */
	// Spout Start
	public void playSound(String s, float f, float f1, float f2, float f3, float f4) {
		playSound(s, f, f1, f2, f3, f4, -1, 1.0F);
	}
	
	public void playSound(String s, float f, float f1, float f2, float f3, float f4, int soundId, float volume)
	{
		if(!loaded || options.soundVolume == 0.0F) {
			return;
		}
		if (soundEffectsLimit-- <= 0) {
			return;
		}
		
		SoundPoolEntry soundpoolentry;
		if (soundId > -1) soundpoolentry = soundPoolSounds.getSoundFromSoundPool(s, soundId);
		else soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(s);
		
		if(soundpoolentry != null && f3 > 0.0F)
		{
			latestSoundID = (latestSoundID + 1) % 256;
			String s1;
			if (soundId == -1) s1 = (new StringBuilder()).append("sound_").append(latestSoundID).toString();
			else s1 = (new StringBuilder()).append("sound_").append(soundId).toString();
			float f5 = 16F;
			if(f3 > 1.0F)
			{
				f5 *= f3;
			}
			sndSystem.newSource(f3 > 1.0F, s1, soundpoolentry.soundUrl, soundpoolentry.soundName, false, f, f1, f2, 2, f5);
			sndSystem.setPitch(s1, f4);
			if(f3 > 1.0F)
			{
				f3 = 1.0F;
			}
			f3 *= volume;
			sndSystem.setVolume(s1, f3 * options.soundVolume);
			sndSystem.play(s1);
		}
	}
	
	public void playSoundFX(String s, float f, float f1) {
		playSoundFX(s, f, f1, -1, 1.0F);
	}
	
	public void playSoundFX(String s, float f, float f1, int soundId, float volume)
	{
		if(!loaded || options.soundVolume == 0.0F) {
			return;
		}
		if (soundEffectsLimit-- <= 0) {
			return;
		}
		SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(s);
		if(soundpoolentry != null)
		{
			latestSoundID = (latestSoundID + 1) % 256;
			String s1;
			if (soundId == -1) s1 = (new StringBuilder()).append("sound_").append(latestSoundID).toString();
			else s1 = (new StringBuilder()).append("sound_").append(soundId).toString();
			sndSystem.newSource(false, s1, soundpoolentry.soundUrl, soundpoolentry.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
			if(f > 1.0F)
			{
				f = 1.0F;
			}
			f *= 0.25F;
			f *= volume;
			sndSystem.setPitch(s1, f1);
			sndSystem.setVolume(s1, f * options.soundVolume);
			sndSystem.play(s1);
		}
	}
	
	public void playMusic(String music, int id, float volume) {
		playMusic(music, id, 0, 0, 0, volume, 0F);
	}
	
	public void playMusic(String music, int id, int x, int y, int z, float volume, float distance) {
		if(!loaded || options.musicVolume == 0.0F) {
			return;
		}
		stopMusic();
		SoundPoolEntry soundpoolentry = soundPoolMusic.getSoundFromSoundPool(music, id);
		if(soundpoolentry != null) {
			ticksBeforeMusic = rand.nextInt(12000) + 12000;
			if (distance > 0F) {
				sndSystem.removeSource("BgMusic");
				sndSystem.newStreamingSource(false, "BgMusic", soundpoolentry.soundUrl, soundpoolentry.soundName, false, x, y, z, 2, distance);
			}
			else {
				sndSystem.backgroundMusic("BgMusic", soundpoolentry.soundUrl, soundpoolentry.soundName, false);
			}
			sndSystem.setVolume("BgMusic", options.musicVolume * volume);
			sndSystem.play("BgMusic");
		}
	}
	
	public void playCustomSoundEffect(String effect, float volume) {
		playCustomSoundEffect(effect, 0, 0, 0, volume, 0F);
	}
	
	public void playCustomSoundEffect(String music, int x, int y, int z, float volume, float distance) {
		if(!loaded || options.soundVolume == 0.0F) {
			return;
		}
		if (soundEffectsLimit-- <= 0) {
			return;
		}
		SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(music);
		if (soundpoolentry != null) {
			String source;
			if (distance > 0F) {
				source = sndSystem.quickStream(false, soundpoolentry.soundUrl, soundpoolentry.soundName, false, x, y, z, 2, distance);
			}
			else {
				source = sndSystem.quickStream(false, soundpoolentry.soundUrl, soundpoolentry.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
			}
			sndSystem.setVolume(source, volume * options.soundVolume);
			sndSystem.play(source);
		}
	}
	
	public void preload(String music) {
		sndSystem.loadSound(music);
	}
	
	public boolean hasMusic(String sound, int id) {
		return soundPoolMusic.getSoundFromSoundPool(sound, id) != null;
	}
	
	public boolean hasSoundEffect(String sound, int id) {
		return soundPoolSounds.getSoundFromSoundPool(sound, id) != null;
	}
	
	public void addCustomSoundEffect(String sound, File song) {
		soundPoolSounds.addCustomSound(sound, song);
	}
	
	public void addCustomMusic(String sound, File song) {
		soundPoolMusic.addCustomSound(sound, song);
	}
	
	public void stopMusic() {
		if (sndSystem != null) {
			if(sndSystem.playing("BgMusic")) {
				sndSystem.stop("BgMusic");
			}
			if(sndSystem.playing("streaming")){
				sndSystem.stop("streaming");
			}
		}
	}
	
	public void fadeOut(int time){
		if(sndSystem.playing("BgMusic")) {
			sndSystem.fadeOut("BgMusic", null, time);
		}
		if(sndSystem.playing("streaming")){
			sndSystem.fadeOut("streaming", null, time);
		}
	}
	
	public void resetTime() {
		ticksBeforeMusic = rand.nextInt(12000) + 12000;
	}
	
	public void tick() {
		soundEffectsLimit = SOUND_EFFECTS_PER_TICK;
	}
	
	public SoundPoolEntry waitingSound = null;
	public boolean allowed = false;
	public boolean cancelled = false;
	// Spout End

	public void func_82466_e() {
		Iterator var1 = this.field_82470_g.iterator();

		while (var1.hasNext()) {
			String var2 = (String)var1.next();
			sndSystem.pause(var2);
		}
	}

	public void func_82461_f() {
		Iterator var1 = this.field_82470_g.iterator();

		while (var1.hasNext()) {
			String var2 = (String)var1.next();
			sndSystem.play(var2);
		}
	}

}
