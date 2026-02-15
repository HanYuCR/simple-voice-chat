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

// 修改目标为 LivingEntityRenderer，因为 PlayerRenderer 不再由 renderNameTag 方法
@Mixin(value = LivingEntityRenderer.class, priority = 10000)
public abstract class PlayerRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    protected PlayerRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    // 注入到父类的 renderNameTag 方法中
    @Inject(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V"))
    private void renderNameTag(T entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float partialTicks, CallbackInfo info) {
        // 关键判断：只有当实体是玩家(AbstractClientPlayer)时，才渲染语音图标
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
