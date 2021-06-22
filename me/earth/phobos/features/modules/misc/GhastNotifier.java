package me.earth.phobos.features.modules.misc;

import java.util.HashSet;
import java.util.Set;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;

public class GhastNotifier extends Module {
  public Setting<Boolean> Chat = register(new Setting("Chat", Boolean.valueOf(true)));
  
  public Setting<Boolean> Sound = register(new Setting("Sound", Boolean.valueOf(true)));
  
  private final Set<Entity> ghasts = new HashSet<>();
  
  public GhastNotifier() {
    super("GhastNotifier", "Helps you find ghasts", Module.Category.MISC, true, true, false);
  }
  
  public void onEnable() {
    this.ghasts.clear();
  }
  
  public void onUpdate() {
    for (Entity entity : mc.world.getLoadedEntityList()) {
      if (!(entity instanceof net.minecraft.entity.monster.EntityGhast) || this.ghasts.contains(entity))
        continue; 
      if (((Boolean)this.Chat.getValue()).booleanValue())
        Command.sendMessage("Ghast Detected at: " + entity.getPosition().getX() + "x, " + entity.getPosition().getY() + "y, " + entity.getPosition().getZ() + "z."); 
      this.ghasts.add(entity);
      if (!((Boolean)this.Sound.getValue()).booleanValue())
        continue; 
      mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
    } 
  }
}
