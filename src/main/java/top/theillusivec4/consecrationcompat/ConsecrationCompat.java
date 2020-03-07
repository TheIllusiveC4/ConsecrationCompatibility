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
import top.theillusivec4.consecrationcompat.modules.Module;
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
