package me.earth.phobos.mixin.mixins;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.modules.client.FontMod;
import me.earth.phobos.features.modules.client.HUD;
import me.earth.phobos.features.modules.client.Media;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FontRenderer.class})
public abstract class MixinFontRenderer {
  @Shadow
  protected abstract int renderString(String paramString, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean);
  
  @Shadow
  protected abstract void renderStringAtPos(String paramString, boolean paramBoolean);
  
  @Inject(method = {"drawString(Ljava/lang/String;FFIZ)I"}, at = {@At("HEAD")}, cancellable = true)
  public void renderStringHook(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> info) {
    if (FontMod.getInstance().isOn() && ((Boolean)(FontMod.getInstance()).full.getValue()).booleanValue() && Phobos.textManager != null) {
      float result = Phobos.textManager.drawString(text, x, y, color, dropShadow);
      info.setReturnValue(Integer.valueOf((int)result));
    } 
  }
  
  @Redirect(method = {"drawString(Ljava/lang/String;FFIZ)I"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I"))
  public int renderStringHook(FontRenderer fontrenderer, String text, float x, float y, int color, boolean dropShadow) {
    if (Phobos.moduleManager != null && ((Boolean)(HUD.getInstance()).shadow.getValue()).booleanValue() && dropShadow)
      return renderString(text, x - 0.5F, y - 0.5F, color, true); 
    return renderString(text, x, y, color, dropShadow);
  }
  
  @Redirect(method = {"renderString(Ljava/lang/String;FFIZ)I"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
  public void renderStringAtPosHook(FontRenderer renderer, String text, boolean shadow) {
    if (Media.getInstance().isOn() && ((Boolean)(Media.getInstance()).changeOwn.getValue()).booleanValue()) {
      renderStringAtPos(text.replace(Media.getPlayerName(), (CharSequence)(Media.getInstance()).ownName.getValue()), shadow);
    } else {
      renderStringAtPos(text, shadow);
    } 
  }
}
