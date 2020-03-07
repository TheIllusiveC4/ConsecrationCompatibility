package top.theillusivec4.consecrationcompat.modules;

import java.util.function.BiFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.silentchaos512.gear.api.parts.PartDataList;
import net.silentchaos512.gear.parts.PartData;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gear.util.GearHelper;
import top.theillusivec4.consecration.api.ConsecrationAPI;
import top.theillusivec4.consecration.api.ConsecrationAPI.IMC;

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
    if (GearHelper.isGear(stack)) {
      PartDataList data = GearData.getConstructionParts(stack);

      for (PartData partData : data) {
        ItemStack[] stacks = partData.getPart().getMaterials().getNormal().getMatchingStacks();

        for (ItemStack mat : stacks) {
          ResourceLocation resourceLocation = mat.getItem().getRegistryName();

          if (resourceLocation != null && containsHolyMaterial(resourceLocation)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static boolean containsHolyMaterial(ResourceLocation resourceLocation) {

    for (String mat : ConsecrationAPI.getHolyMaterials()) {
      String pattern = "^" + mat + "(\\b|[_-]\\w*)";
      if (resourceLocation.getPath().matches(pattern)) {
        return true;
      }
    }
    return false;
  }
}
