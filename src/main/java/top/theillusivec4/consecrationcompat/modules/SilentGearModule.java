/*
 * Copyright (c) 2020 C4
 *
 * This file is part of Consecration - Compatibility Add-on, a mod
 * made for Minecraft.
 *
 * Consecration - Compatibility Add-on is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Consecration - Compatibility Add-on is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR PARTICULAR PURPOSE.  See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Consecration - Compatibility
 * Add-on. If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.consecrationcompat.modules;

import java.util.function.BiFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.util.Const;
import net.silentchaos512.gear.util.TraitHelper;
import top.theillusivec4.consecration.api.ConsecrationAPI;
import top.theillusivec4.consecration.api.ConsecrationAPI.IMC;
import top.theillusivec4.consecrationcompat.modules.base.Module;

public class SilentGearModule extends Module {

  @Override
  public void enqueueImc() {
    InterModComms.sendTo("consecration", IMC.HOLY_ATTACK,
        () -> (BiFunction<LivingEntity, DamageSource, Boolean>) (livingEntity, damageSource) -> {

          if (damageSource.getImmediateSource() instanceof LivingEntity) {
            ItemStack stack = ((LivingEntity) damageSource.getImmediateSource())
                .getHeldItemMainhand();
            return containsHolyMaterial(stack);
          }
          return false;
        });
    InterModComms.sendTo("consecration", IMC.HOLY_PROTECTION,
        () -> (BiFunction<LivingEntity, DamageSource, Integer>) (livingEntity, damageSource) -> {
          int level = 0;

          for (ItemStack stack : livingEntity.getArmorInventoryList()) {

            if (containsHolyMaterial(stack)) {
              level++;
            }
          }
          return level;
        });
  }

  private static boolean containsHolyMaterial(ItemStack stack) {
    if (stack.getItem() instanceof ICoreItem) {
      return TraitHelper.getTraitLevel(stack, Const.Traits.HOLY) > 0;
    }
    return false;
  }
}
