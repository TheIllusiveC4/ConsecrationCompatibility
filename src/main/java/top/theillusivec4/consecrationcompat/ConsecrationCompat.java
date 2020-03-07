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

package top.theillusivec4.consecrationcompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.consecrationcompat.modules.base.Module;
import top.theillusivec4.consecrationcompat.modules.SilentGearModule;
import top.theillusivec4.consecrationcompat.modules.SpartanWeaponryModule;
import top.theillusivec4.consecrationcompat.modules.TetraModule;

@Mod(ConsecrationCompat.MODID)
public class ConsecrationCompat {

  public static final String MODID = "consecrationcompat";
  public static final Logger LOGGER = LogManager.getLogger();

  public static final Map<String, Class<? extends Module>> MODULES = new HashMap<>();
  public static final List<Module> ACTIVE_MODULES = new ArrayList<>();

  static {
    MODULES.put("tetra", TetraModule.class);
    MODULES.put("spartanweaponry", SpartanWeaponryModule.class);
    MODULES.put("silentgear", SilentGearModule.class);
  }

  public ConsecrationCompat() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::imcEnqueue);
    MODULES.forEach((id, module) -> {
      if (ModList.get().isLoaded(id)) {
        try {
          ACTIVE_MODULES.add(module.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
          LOGGER.error("Error adding module for mod " + id);
        }
      }
    });
  }

  private void imcEnqueue(final InterModEnqueueEvent evt) {
    ACTIVE_MODULES.forEach(Module::enqueueImc);
  }
}
