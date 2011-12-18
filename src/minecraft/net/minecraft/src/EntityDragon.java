package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockEndPortal;
import net.minecraft.src.DamageSource;
import net.minecraft.src.DragonPart;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDragonBase;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

import org.spoutcraft.client.entity.CraftEnderDragon; //Spout

public class EntityDragon extends EntityDragonBase {

	public double field_40167_a;
	public double field_40165_b;
	public double field_40166_c;
	public double[][] field_40162_d = new double[64][3];
	public int field_40164_e = -1;
	public DragonPart[] field_40176_ao;
	public DragonPart field_40177_ap;
	public DragonPart field_40171_aq;
	public DragonPart field_40170_ar;
	public DragonPart field_40169_as;
	public DragonPart field_40168_at;
	public DragonPart field_40175_au;
	public DragonPart field_40174_av;
	public float field_40173_aw = 0.0F;
	public float field_40172_ax = 0.0F;
	public boolean field_40163_ay = false;
	public boolean field_40161_az = false;
	private Entity field_40179_aC;
	public int field_40178_aA = 0;
	public EntityEnderCrystal field_41013_bH = null;


	public EntityDragon(World var1) {
		super(var1);
		this.field_40176_ao = new DragonPart[]{this.field_40177_ap = new DragonPart(this, "head", 6.0F, 6.0F), this.field_40171_aq = new DragonPart(this, "body", 8.0F, 8.0F), this.field_40170_ar = new DragonPart(this, "tail", 4.0F, 4.0F), this.field_40169_as = new DragonPart(this, "tail", 4.0F, 4.0F), this.field_40168_at = new DragonPart(this, "tail", 4.0F, 4.0F), this.field_40175_au = new DragonPart(this, "wing", 4.0F, 4.0F), this.field_40174_av = new DragonPart(this, "wing", 4.0F, 4.0F)};
		this.field_40157_aB = 200;
		this.setEntityHealth(this.field_40157_aB);
		this.texture = "/mob/enderdragon/ender.png";
		this.setSize(16.0F, 8.0F);
		this.noClip = true;
		this.isImmuneToFire = true;
		this.field_40165_b = 100.0D;
		this.ignoreFrustumCheck = true;
        //Spout start
        this.spoutEntity = new CraftEnderDragon(this);
        //Spout end
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Integer(this.field_40157_aB));
	}

	public double[] func_40160_a(int var1, float var2) {
		if(this.health <= 0) {
			var2 = 0.0F;
		}

		var2 = 1.0F - var2;
		int var3 = this.field_40164_e - var1 * 1 & 63;
		int var4 = this.field_40164_e - var1 * 1 - 1 & 63;
		double[] var5 = new double[3];
		double var6 = this.field_40162_d[var3][0];

		double var8;
		for(var8 = this.field_40162_d[var4][0] - var6; var8 < -180.0D; var8 += 360.0D) {
			;
		}

		while(var8 >= 180.0D) {
			var8 -= 360.0D;
		}

		var5[0] = var6 + var8 * (double)var2;
		var6 = this.field_40162_d[var3][1];
		var8 = this.field_40162_d[var4][1] - var6;
		var5[1] = var6 + var8 * (double)var2;
		var5[2] = this.field_40162_d[var3][2] + (this.field_40162_d[var4][2] - this.field_40162_d[var3][2]) * (double)var2;
		return var5;
	}

	public void onLivingUpdate() {
		this.field_40173_aw = this.field_40172_ax;
		if(!this.worldObj.multiplayerWorld) {
			this.dataWatcher.updateObject(16, Integer.valueOf(this.health));
		}

		float var1;
		float var3;
		float var26;
		if(this.health <= 0) {
			var1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			var26 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			var3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.worldObj.spawnParticle("largeexplode", this.posX + (double)var1, this.posY + 2.0D + (double)var26, this.posZ + (double)var3, 0.0D, 0.0D, 0.0D);
		} else {
			this.func_41011_ay();
			var1 = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
			var1 *= (float)Math.pow(2.0D, this.motionY);
			if(this.field_40161_az) {
				this.field_40172_ax += var1 * 0.5F;
			} else {
				this.field_40172_ax += var1;
			}

			while(this.rotationYaw >= 180.0F) {
				this.rotationYaw -= 360.0F;
			}

			while(this.rotationYaw < -180.0F) {
				this.rotationYaw += 360.0F;
			}

			if(this.field_40164_e < 0) {
				for(int var2 = 0; var2 < this.field_40162_d.length; ++var2) {
					this.field_40162_d[var2][0] = (double)this.rotationYaw;
					this.field_40162_d[var2][1] = this.posY;
				}
			}

			if(++this.field_40164_e == this.field_40162_d.length) {
				this.field_40164_e = 0;
			}

			this.field_40162_d[this.field_40164_e][0] = (double)this.rotationYaw;
			this.field_40162_d[this.field_40164_e][1] = this.posY;
			double var4;
			double var6;
			double var8;
			double var25;
			float var33;
			if(this.worldObj.multiplayerWorld) {
				if(this.newPosRotationIncrements > 0) {
					var25 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
					var4 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
					var6 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;

					for(var8 = this.newRotationYaw - (double)this.rotationYaw; var8 < -180.0D; var8 += 360.0D) {
						;
					}

					while(var8 >= 180.0D) {
						var8 -= 360.0D;
					}

					this.rotationYaw = (float)((double)this.rotationYaw + var8 / (double)this.newPosRotationIncrements);
					this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
					--this.newPosRotationIncrements;
					this.setPosition(var25, var4, var6);
					this.setRotation(this.rotationYaw, this.rotationPitch);
				}
			} else {
				var25 = this.field_40167_a - this.posX;
				var4 = this.field_40165_b - this.posY;
				var6 = this.field_40166_c - this.posZ;
				var8 = var25 * var25 + var4 * var4 + var6 * var6;
				if(this.field_40179_aC != null) {
					this.field_40167_a = this.field_40179_aC.posX;
					this.field_40166_c = this.field_40179_aC.posZ;
					double var10 = this.field_40167_a - this.posX;
					double var12 = this.field_40166_c - this.posZ;
					double var14 = Math.sqrt(var10 * var10 + var12 * var12);
					double var16 = 0.4000000059604645D + var14 / 80.0D - 1.0D;
					if(var16 > 10.0D) {
						var16 = 10.0D;
					}

					this.field_40165_b = this.field_40179_aC.boundingBox.minY + var16;
				} else {
					this.field_40167_a += this.rand.nextGaussian() * 2.0D;
					this.field_40166_c += this.rand.nextGaussian() * 2.0D;
				}

				if(this.field_40163_ay || var8 < 100.0D || var8 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically) {
					this.func_41006_aA();
				}

				var4 /= (double)MathHelper.sqrt_double(var25 * var25 + var6 * var6);
				var33 = 0.6F;
				if(var4 < (double)(-var33)) {
					var4 = (double)(-var33);
				}

				if(var4 > (double)var33) {
					var4 = (double)var33;
				}

				for(this.motionY += var4 * 0.10000000149011612D; this.rotationYaw < -180.0F; this.rotationYaw += 360.0F) {
					;
				}

				while(this.rotationYaw >= 180.0F) {
					this.rotationYaw -= 360.0F;
				}

				double var11 = 180.0D - Math.atan2(var25, var6) * 180.0D / 3.1415927410125732D;

				double var13;
				for(var13 = var11 - (double)this.rotationYaw; var13 < -180.0D; var13 += 360.0D) {
					;
				}

				while(var13 >= 180.0D) {
					var13 -= 360.0D;
				}

				if(var13 > 50.0D) {
					var13 = 50.0D;
				}

				if(var13 < -50.0D) {
					var13 = -50.0D;
				}

				Vec3D var15 = Vec3D.createVector(this.field_40167_a - this.posX, this.field_40165_b - this.posY, this.field_40166_c - this.posZ).normalize();
				Vec3D var40 = Vec3D.createVector((double)MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F))).normalize();
				float var17 = (float)(var40.dotProduct(var15) + 0.5D) / 1.5F;
				if(var17 < 0.0F) {
					var17 = 0.0F;
				}

				this.randomYawVelocity *= 0.8F;
				float var18 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
				double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;
				if(var19 > 40.0D) {
					var19 = 40.0D;
				}

				this.randomYawVelocity = (float)((double)this.randomYawVelocity + var13 * (0.699999988079071D / var19 / (double)var18));
				this.rotationYaw += this.randomYawVelocity * 0.1F;
				float var21 = (float)(2.0D / (var19 + 1.0D));
				float var22 = 0.06F;
				this.moveFlying(0.0F, -1.0F, var22 * (var17 * var21 + (1.0F - var21)));
				if(this.field_40161_az) {
					this.moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
				} else {
					this.moveEntity(this.motionX, this.motionY, this.motionZ);
				}

				Vec3D var23 = Vec3D.createVector(this.motionX, this.motionY, this.motionZ).normalize();
				float var24 = (float)(var23.dotProduct(var40) + 1.0D) / 2.0F;
				var24 = 0.8F + 0.15F * var24;
				this.motionX *= (double)var24;
				this.motionZ *= (double)var24;
				this.motionY *= 0.9100000262260437D;
			}

			this.renderYawOffset = this.rotationYaw;
			this.field_40177_ap.width = this.field_40177_ap.height = 3.0F;
			this.field_40170_ar.width = this.field_40170_ar.height = 2.0F;
			this.field_40169_as.width = this.field_40169_as.height = 2.0F;
			this.field_40168_at.width = this.field_40168_at.height = 2.0F;
			this.field_40171_aq.height = 3.0F;
			this.field_40171_aq.width = 5.0F;
			this.field_40175_au.height = 2.0F;
			this.field_40175_au.width = 4.0F;
			this.field_40174_av.height = 3.0F;
			this.field_40174_av.width = 4.0F;
			var26 = (float)(this.func_40160_a(5, 1.0F)[1] - this.func_40160_a(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
			var3 = MathHelper.cos(var26);
			float var28 = -MathHelper.sin(var26);
			float var5 = this.rotationYaw * 3.1415927F / 180.0F;
			float var27 = MathHelper.sin(var5);
			float var7 = MathHelper.cos(var5);
			this.field_40171_aq.onUpdate();
			this.field_40171_aq.setLocationAndAngles(this.posX + (double)(var27 * 0.5F), this.posY, this.posZ - (double)(var7 * 0.5F), 0.0F, 0.0F);
			this.field_40175_au.onUpdate();
			this.field_40175_au.setLocationAndAngles(this.posX + (double)(var7 * 4.5F), this.posY + 2.0D, this.posZ + (double)(var27 * 4.5F), 0.0F, 0.0F);
			this.field_40174_av.onUpdate();
			this.field_40174_av.setLocationAndAngles(this.posX - (double)(var7 * 4.5F), this.posY + 2.0D, this.posZ - (double)(var27 * 4.5F), 0.0F, 0.0F);
			if(!this.worldObj.multiplayerWorld) {
				this.func_41007_az();
			}

			if(!this.worldObj.multiplayerWorld && this.maxHurtTime == 0) {
				this.func_41008_a(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.field_40175_au.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				this.func_41008_a(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.field_40174_av.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				this.func_41009_b(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.field_40177_ap.boundingBox.expand(1.0D, 1.0D, 1.0D)));
			}

			double[] var30 = this.func_40160_a(5, 1.0F);
			double[] var9 = this.func_40160_a(0, 1.0F);
			var33 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F - this.randomYawVelocity * 0.01F);
			float var32 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F - this.randomYawVelocity * 0.01F);
			this.field_40177_ap.onUpdate();
			this.field_40177_ap.setLocationAndAngles(this.posX + (double)(var33 * 5.5F * var3), this.posY + (var9[1] - var30[1]) * 1.0D + (double)(var28 * 5.5F), this.posZ - (double)(var32 * 5.5F * var3), 0.0F, 0.0F);

			for(int var29 = 0; var29 < 3; ++var29) {
				DragonPart var31 = null;
				if(var29 == 0) {
					var31 = this.field_40170_ar;
				}

				if(var29 == 1) {
					var31 = this.field_40169_as;
				}

				if(var29 == 2) {
					var31 = this.field_40168_at;
				}

				double[] var35 = this.func_40160_a(12 + var29 * 2, 1.0F);
				float var34 = this.rotationYaw * 3.1415927F / 180.0F + this.func_40159_b(var35[0] - var30[0]) * 3.1415927F / 180.0F * 1.0F;
				float var37 = MathHelper.sin(var34);
				float var38 = MathHelper.cos(var34);
				float var36 = 1.5F;
				float var39 = (float)(var29 + 1) * 2.0F;
				var31.onUpdate();
				var31.setLocationAndAngles(this.posX - (double)((var27 * var36 + var37 * var39) * var3), this.posY + (var35[1] - var30[1]) * 1.0D - (double)((var39 + var36) * var28) + 1.5D, this.posZ + (double)((var7 * var36 + var38 * var39) * var3), 0.0F, 0.0F);
			}

			if(!this.worldObj.multiplayerWorld) {
				this.field_40161_az = this.func_40158_a(this.field_40177_ap.boundingBox) | this.func_40158_a(this.field_40171_aq.boundingBox);
			}

		}
	}

	private void func_41011_ay() {
		if(this.field_41013_bH != null) {
			if(this.field_41013_bH.isDead) {
				if(!this.worldObj.multiplayerWorld) {
					this.func_40156_a(this.field_40177_ap, DamageSource.explosion, 10);
				}

				this.field_41013_bH = null;
			} else if(this.ticksExisted % 10 == 0 && this.health < this.field_40157_aB) {
				++this.health;
			}
		}

		if(this.rand.nextInt(10) == 0) {
			float var1 = 32.0F;
			List var2 = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.boundingBox.expand((double)var1, (double)var1, (double)var1));
			EntityEnderCrystal var3 = null;
			double var4 = Double.MAX_VALUE;
			Iterator var6 = var2.iterator();

			while(var6.hasNext()) {
				Entity var7 = (Entity)var6.next();
				double var8 = var7.getDistanceSqToEntity(this);
				if(var8 < var4) {
					var4 = var8;
					var3 = (EntityEnderCrystal)var7;
				}
			}

			this.field_41013_bH = var3;
		}

	}

	private void func_41007_az() {
		if(this.ticksExisted % 20 == 0) {
			Vec3D var1 = this.getLook(1.0F);
			double var2 = 0.0D;
			double var4 = -1.0D;
			double var6 = 0.0D;
		}

	}

	private void func_41008_a(List var1) {
		double var2 = (this.field_40171_aq.boundingBox.minX + this.field_40171_aq.boundingBox.maxX) / 2.0D;
		double var4 = (this.field_40171_aq.boundingBox.minZ + this.field_40171_aq.boundingBox.maxZ) / 2.0D;
		Iterator var6 = var1.iterator();

		while(var6.hasNext()) {
			Entity var7 = (Entity)var6.next();
			if(var7 instanceof EntityLiving) {
				double var8 = var7.posX - var2;
				double var10 = var7.posZ - var4;
				double var12 = var8 * var8 + var10 * var10;
				var7.addVelocity(var8 / var12 * 4.0D, 0.20000000298023224D, var10 / var12 * 4.0D);
			}
		}

	}

	private void func_41009_b(List var1) {
		for(int var2 = 0; var2 < var1.size(); ++var2) {
			Entity var3 = (Entity)var1.get(var2);
			if(var3 instanceof EntityLiving) {
				var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10);
			}
		}

	}

	private void func_41006_aA() {
		this.field_40163_ay = false;
		if(this.rand.nextInt(2) == 0 && this.worldObj.playerEntities.size() > 0) {
			this.field_40179_aC = (Entity)this.worldObj.playerEntities.get(this.rand.nextInt(this.worldObj.playerEntities.size()));
		} else {
			boolean var1 = false;

			do {
				this.field_40167_a = 0.0D;
				this.field_40165_b = (double)(70.0F + this.rand.nextFloat() * 50.0F);
				this.field_40166_c = 0.0D;
				this.field_40167_a += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
				this.field_40166_c += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
				double var2 = this.posX - this.field_40167_a;
				double var4 = this.posY - this.field_40165_b;
				double var6 = this.posZ - this.field_40166_c;
				var1 = var2 * var2 + var4 * var4 + var6 * var6 > 100.0D;
			} while(!var1);

			this.field_40179_aC = null;
		}

	}

	private float func_40159_b(double var1) {
		while(var1 >= 180.0D) {
			var1 -= 360.0D;
		}

		while(var1 < -180.0D) {
			var1 += 360.0D;
		}

		return (float)var1;
	}

	private boolean func_40158_a(AxisAlignedBB var1) {
		int var2 = MathHelper.floor_double(var1.minX);
		int var3 = MathHelper.floor_double(var1.minY);
		int var4 = MathHelper.floor_double(var1.minZ);
		int var5 = MathHelper.floor_double(var1.maxX);
		int var6 = MathHelper.floor_double(var1.maxY);
		int var7 = MathHelper.floor_double(var1.maxZ);
		boolean var8 = false;
		boolean var9 = false;

		for(int var10 = var2; var10 <= var5; ++var10) {
			for(int var11 = var3; var11 <= var6; ++var11) {
				for(int var12 = var4; var12 <= var7; ++var12) {
					int var13 = this.worldObj.getBlockId(var10, var11, var12);
					if(var13 != 0) {
						if(var13 != Block.obsidian.blockID && var13 != Block.whiteStone.blockID && var13 != Block.bedrock.blockID) {
							var9 = true;
							this.worldObj.setBlockWithNotify(var10, var11, var12, 0);
						} else {
							var8 = true;
						}
					}
				}
			}
		}

		if(var9) {
			double var16 = var1.minX + (var1.maxX - var1.minX) * (double)this.rand.nextFloat();
			double var17 = var1.minY + (var1.maxY - var1.minY) * (double)this.rand.nextFloat();
			double var14 = var1.minZ + (var1.maxZ - var1.minZ) * (double)this.rand.nextFloat();
			this.worldObj.spawnParticle("largeexplode", var16, var17, var14, 0.0D, 0.0D, 0.0D);
		}

		return var8;
	}

	public boolean func_40156_a(DragonPart var1, DamageSource var2, int var3) {
		if(var1 != this.field_40177_ap) {
			var3 = var3 / 4 + 1;
		}

		float var4 = this.rotationYaw * 3.1415927F / 180.0F;
		float var5 = MathHelper.sin(var4);
		float var6 = MathHelper.cos(var4);
		this.field_40167_a = this.posX + (double)(var5 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
		this.field_40165_b = this.posY + (double)(this.rand.nextFloat() * 3.0F) + 1.0D;
		this.field_40166_c = this.posZ - (double)(var6 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
		this.field_40179_aC = null;
		if(var2.getSourceOfDamage() instanceof EntityPlayer || var2 == DamageSource.explosion) {
			this.func_40155_e(var2, var3);
		}

		return true;
	}

	protected void func_40120_m_() {
		++this.field_40178_aA;
		if(this.field_40178_aA >= 180 && this.field_40178_aA <= 200) {
			float var1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			float var2 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			float var3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.worldObj.spawnParticle("hugeexplosion", this.posX + (double)var1, this.posY + 2.0D + (double)var2, this.posZ + (double)var3, 0.0D, 0.0D, 0.0D);
		}

		int var4;
		int var5;
		if(!this.worldObj.multiplayerWorld && this.field_40178_aA > 150 && this.field_40178_aA % 5 == 0) {
			var4 = 1000;

			while(var4 > 0) {
				var5 = EntityXPOrb.getXPSplit(var4);
				var4 -= var5;
				this.worldObj.entityJoinedWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
			}
		}

		this.moveEntity(0.0D, 0.10000000149011612D, 0.0D);
		this.renderYawOffset = this.rotationYaw += 20.0F;
		if(this.field_40178_aA == 200) {
			var4 = 10000;

			while(var4 > 0) {
				var5 = EntityXPOrb.getXPSplit(var4);
				var4 -= var5;
				this.worldObj.entityJoinedWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
			}

			var5 = 5 + this.rand.nextInt(2) * 2 - 1;
			int var6 = 5 + this.rand.nextInt(2) * 2 - 1;
			if(this.rand.nextInt(2) == 0) {
				boolean var7 = false;
			} else {
				boolean var8 = false;
			}

			this.func_41012_a(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
			this.onEntityDeath();
			this.setEntityDead();
		}

	}

	private void func_41012_a(int var1, int var2) {
		int var3 = this.worldObj.field_35472_c / 2;
		BlockEndPortal.field_41051_a = true;
		byte var4 = 4;

		for(int var5 = var3 - 1; var5 <= var3 + 32; ++var5) {
			for(int var6 = var1 - var4; var6 <= var1 + var4; ++var6) {
				for(int var7 = var2 - var4; var7 <= var2 + var4; ++var7) {
					double var8 = (double)(var6 - var1);
					double var10 = (double)(var7 - var2);
					double var12 = (double)MathHelper.sqrt_double(var8 * var8 + var10 * var10);
					if(var12 <= (double)var4 - 0.5D) {
						if(var5 < var3) {
							if(var12 <= (double)(var4 - 1) - 0.5D) {
								this.worldObj.setBlockWithNotify(var6, var5, var7, Block.bedrock.blockID);
							}
						} else if(var5 > var3) {
							this.worldObj.setBlockWithNotify(var6, var5, var7, 0);
						} else if(var12 > (double)(var4 - 1) - 0.5D) {
							this.worldObj.setBlockWithNotify(var6, var5, var7, Block.bedrock.blockID);
						} else {
							this.worldObj.setBlockWithNotify(var6, var5, var7, Block.endPortal.blockID);
						}
					}
				}
			}
		}

		this.worldObj.setBlockWithNotify(var1, var3 + 0, var2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(var1, var3 + 1, var2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(var1, var3 + 2, var2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(var1 - 1, var3 + 2, var2, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(var1 + 1, var3 + 2, var2, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(var1, var3 + 2, var2 - 1, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(var1, var3 + 2, var2 + 1, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(var1, var3 + 3, var2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(var1, var3 + 4, var2, Block.field_41050_bK.blockID);
		BlockEndPortal.field_41051_a = false;
	}

	protected void despawnEntity() {}

	public Entity[] func_40048_X() {
		return this.field_40176_ao;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public int func_41010_ax() {
		return this.dataWatcher.getWatchableObjectInt(16);
	}
}
