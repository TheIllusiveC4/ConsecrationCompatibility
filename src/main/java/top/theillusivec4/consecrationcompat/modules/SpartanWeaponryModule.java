package top.theillusivec4.consecrationcompat.modules;

import com.oblivioussp.spartanweaponry.api.IWeaponTraitContainer;
import com.oblivioussp.spartanweaponry.entity.projectile.ArrowBaseEntity;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.consecration.api.ConsecrationAPI;
import top.theillusivec4.consecrationcompat.ConsecrationCompat;

public class SpartanWeaponryModule extends AbstractModule {

  private final static Method GET_ARROW_STACK = ObfuscationReflectionHelper
      .findMethod(AbstractArrowEntity.class, "func_184550_j");

  @Override
  public void enqueueImc() {
    InterModComms.sendTo("consecration", "holy_attack",
        () -> (BiFunction<LivingEntity, DamageSource, Boolean>) (livingEntity, damageSource) -> {
          Entity source = damageSource.getImmediateSource();

          if (source instanceof LivingEntity) {
            ItemStack stack = ((LivingEntity) source).getHeldItemMainhand();

            if (stack.getItem() instanceof IWeaponTraitContainer) {
              String name = ((IWeaponTraitContainer<?>) stack.getItem()).getMaterial()
                  .getUnlocName();
              return ConsecrationAPI.getHolyMaterials().contains(name);
            }
          } else if (source instanceof AbstractArrowEntity) {
            ItemStack stack = ItemStack.EMPTY;

            try {
              stack = (ItemStack) GET_ARROW_STACK.invoke(source);
            } catch (IllegalAccessException | InvocationTargetException e) {
              ConsecrationCompat.LOGGER.error("Error invoking getArrowStack for " + source);
            }
            ResourceLocation rl = stack.getItem().getRegistryName();

            if (rl != null) {
              String name = rl.getPath();
              return containsHolyMaterial(name);
            }
          }
          return false;
        });
  }

  public static boolean containsHolyMaterial(String value) {

    for (String mat : ConsecrationAPI.getHolyMaterials()) {
      String pattern = "arrow_" + mat + "(\\b|[_-]\\w*)";

      if (value.matches(pattern)) {
        return true;
      }
    }
    return false;
  }
}
