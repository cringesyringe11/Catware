package me.earth.phobos.features.modules.client;

import java.awt.GraphicsEnvironment;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FontMod extends Module {
  private static FontMod INSTANCE = new FontMod();
  
  public Setting<String> fontName = register(new Setting("FontName", "Arial", "Name of the font."));
  
  public Setting<Integer> fontSize = register(new Setting("FontSize", Integer.valueOf(18), "Size of the font."));
  
  public Setting<Integer> fontStyle = register(new Setting("FontStyle", Integer.valueOf(0), "Style of the font."));
  
  public Setting<Boolean> antiAlias = register(new Setting("AntiAlias", Boolean.valueOf(true), "Smoother font."));
  
  public Setting<Boolean> fractionalMetrics = register(new Setting("Metrics", Boolean.valueOf(true), "Thinner font."));
  
  public Setting<Boolean> shadow = register(new Setting("Shadow", Boolean.valueOf(true), "Less shadow offset font."));
  
  public Setting<Boolean> showFonts = register(new Setting("Fonts", Boolean.valueOf(false), "Shows all fonts."));
  
  public Setting<Boolean> full = register(new Setting("Full", Boolean.valueOf(false)));
  
  private boolean reloadFont = false;
  
  public FontMod() {
    super("CustomFont", "CustomFont for all of the clients text. Use the font command.", Module.Category.CLIENT, true, false, false);
    setInstance();
  }
  
  public static FontMod getInstance() {
    if (INSTANCE == null)
      INSTANCE = new FontMod(); 
    return INSTANCE;
  }
  
  public static boolean checkFont(String font, boolean message) {
    String[] fonts;
    for (String s : fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
      if (!message && s.equals(font))
        return true; 
      if (message)
        Command.sendMessage(s); 
    } 
    return false;
  }
  
  private void setInstance() {
    INSTANCE = this;
  }
  
  @SubscribeEvent
  public void onSettingChange(ClientEvent event) {
    Setting setting;
    if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
      if (setting.getName().equals("FontName") && !checkFont(setting.getPlannedValue().toString(), false)) {
        Command.sendMessage("§cThat font doesnt exist.");
        event.setCanceled(true);
        return;
      } 
      this.reloadFont = true;
    } 
  }
  
  public void onTick() {
    if (((Boolean)this.showFonts.getValue()).booleanValue()) {
      checkFont("Hello", true);
      Command.sendMessage("Current Font: " + (String)this.fontName.getValue());
      this.showFonts.setValue(Boolean.valueOf(false));
    } 
    if (this.reloadFont) {
      Phobos.textManager.init(false);
      this.reloadFont = false;
    } 
  }
}
