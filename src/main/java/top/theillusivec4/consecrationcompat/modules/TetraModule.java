package top.theillusivec4.consecrationcompat.modules;

import java.util.function.BiFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.InterModComms;
import se.mickelus.tetra.items.modular.IItemModular;
import top.theillusivec4.consecration.api.ConsecrationAPI;

public class TetraModule extends Module {

  @Override
  public void enqueueImc() {
    InterModComms.sendTo("consecration", "holy_attack",
        () -> (BiFunction<LivingEntity, DamageSource, Boolean>) (livingEntity, damageSource) -> {

          if (damageSource.getImmediateSource() instanceof LivingEntity) {
            ItemStack stack = ((LivingEntity) damageSource.getImmediateSource())
                .getHeldItemMainhand();

            if (stack.getItem() instanceof IItemModular && stack.hasTag()) {
              CompoundNBT compound = stack.getTag();
              assert compound != null;
              for (String key : compound.keySet()) {

                if (key.contains("_material")) {
                  String value = compound.getString(key);

                  if (containsHolyMaterial(value)) {
                    return true;
                  }
                }
              }
            }
          }
          return false;
        });
  }

  private static boolean containsHolyMaterial(String value) {

    for (String mat : ConsecrationAPI.getHolyMaterials()) {
      String pattern = "\\w*/" + mat + "(\\b|[_-]\\w*)";

      if (value.matches(pattern)) {
        return true;
      }
    }
    return false;
  }
}
