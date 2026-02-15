package de.maxhenkel.voicechat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.voicechat.events.RenderEvents;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntityRenderer.class, priority = 10000)
public abstract class PlayerRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    protected PlayerRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    // 这里我们将 "renderNameTag" 改为了 "method_3936"，这是 1.21.3 的底层通用代号，不会出错
    @Inject(method = "method_3936", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;method_3936(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V"))
    private void renderNameTag(T entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float partialTicks, CallbackInfo info) {
        if (entity instanceof AbstractClientPlayer player) {
            if (info.isCancelled()) {
                return;
            }
            if (!shouldShowName(entity)) {
                return;
            }
            RenderEvents.RENDER_NAMEPLATE.invoker().render(player, component, poseStack, multiBufferSource, light, partialTicks);
        }
    }

}
