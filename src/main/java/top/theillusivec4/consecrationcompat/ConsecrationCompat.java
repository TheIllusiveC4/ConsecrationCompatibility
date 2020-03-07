package top.theillusivec4.consecrationcompat;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.consecration.common.ConsecrationConfig;
import top.theillusivec4.consecration.common.ConsecrationSeed;
import top.theillusivec4.consecration.common.capability.UndyingCapability;
import top.theillusivec4.consecration.common.registry.ConsecrationRegistry;
import top.theillusivec4.consecration.common.trigger.SmiteTrigger;

@Mod(ConsecrationCompat.MODID)
public class ConsecrationCompat {

  public static final String MODID = "consecrationcompat";
  public static final Logger LOGGER = LogManager.getLogger();

  public ConsecrationCompat() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::imcEnqueue);
  }

  private void imcEnqueue(final InterModEnqueueEvent evt) {
  }
}
