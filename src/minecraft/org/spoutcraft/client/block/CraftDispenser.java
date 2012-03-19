/*
 * This file is part of Bukkit (http://bukkit.org/).
 *
 * Bukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
package org.spoutcraft.client.block;

import java.util.Random;

import net.minecraft.src.BlockDispenser;
import net.minecraft.src.TileEntityDispenser;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Dispenser;
import org.spoutcraft.spoutcraftapi.inventory.Inventory;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.inventory.CraftInventory;

public class CraftDispenser extends CraftBlockState implements Dispenser {
	private final SpoutcraftWorld world;
	private final TileEntityDispenser dispenser;

	public CraftDispenser(final Block block) {
		super(block);

		world = (SpoutcraftWorld) block.getWorld();
		dispenser = (TileEntityDispenser) world.getHandle().getBlockTileEntity(getX(), getY(), getZ());
	}

	public Inventory getInventory() {
		return new CraftInventory(dispenser);
	}

	public boolean dispense() {
		Block block = getBlock();

		synchronized (block) {
			if (block.getType() == MaterialData.dispenser) {
				BlockDispenser dispense = (BlockDispenser) net.minecraft.src.Block.dispenser;

				dispense.randomDisplayTick(world.getHandle(), getX(), getY(), getZ(), new Random());
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean update(boolean force) {
		boolean result = super.update(force);

		if (result) {
			dispenser.updateEntity();
		}

		return result;
	}
}
