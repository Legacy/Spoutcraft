package org.getspout.spout.entity;

import java.util.List;
import java.util.UUID;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.property.PropertyObject;
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class CraftEntity extends PropertyObject implements Entity {
	protected net.minecraft.src.Entity handle = null;
	
	public CraftEntity(net.minecraft.src.Entity handle)
	{
		this.handle = handle;
		addProperty("location", new Property() {
			public void set(Object value) {
				teleport((Location)value);
			}
			public Object get() {
				return getLocation();
			}
		});
		addProperty("velocity", new Property() {
			public void set(Object value) {
				setVelocity((Vector)value);
			}
			public Object get() {
				return getVelocity();
			}
		});
	}
	
	public Location getLocation() {
		double X = handle.posX;
		double Y = handle.posY;
		double Z = handle.posZ;
		Location loc = null;
		loc.setX(X);
		loc.setY(Y);
		loc.setZ(Z);
		return loc;
	}

	public void setVelocity(Vector velocity) {
		handle.motionX = velocity.getX();
		handle.motionY = velocity.getY();
		handle.motionZ = velocity.getZ();
	}

	public Vector getVelocity() {
		double X = handle.motionX;
		double Y = handle.motionY;
		double Z = handle.motionX;
		Vector v = null;
		v.setX(X);
		v.setY(Y);
		v.setZ(Z);
		return v;
	}

	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean teleport(Location location) {
		handle.setPosition(location.getX(), location.getY(), location.getZ());
		return false;
	}

	public boolean teleport(Entity destination) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Entity> getNearbyEntities(double x, double y, double z) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getEntityId() {
		int id = handle.entityId;
		return id;
	}

	public int getFireTicks() {
		int fire = handle.fire;
		return fire;
	}

	public int getMaxFireTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setFireTicks(int ticks) {
		handle.fire = ticks;
	}

	public void remove() {
		handle.setEntityDead();
	}

	public boolean isDead() {
		boolean dead = handle.isDead;
		return dead;
	}

	public Entity getPassenger() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean setPassenger(Entity passenger) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean eject() {
		// TODO Auto-generated method stub
		return false;
	}

	public float getFallDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setFallDistance(float distance) {
		// TODO Auto-generated method stub

	}

	public UUID getUniqueId() {
		return handle.uniqueId;
	}
}
