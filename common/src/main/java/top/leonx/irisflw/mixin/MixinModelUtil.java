package top.leonx.irisflw.mixin;

import com.jozufozu.flywheel.core.model.Bufferable;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.util.Pair;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.leonx.irisflw.iris.BufferBuilderStateManager;

import java.util.Collection;

@Mixin(value = ModelUtil.class, remap = false)
public class MixinModelUtil {

    @Group(name = "getBufferBuilderHead", min = 1, max = 2)
    @Inject(method = "getRenderedBuffer(Lcom/jozufozu/flywheel/core/model/Bufferable;)Lcom/jozufozu/flywheel/util/Pair;", at = @At(value = "HEAD"),require = 0)
    private static void irisflw$getBufferBuilderHead(Bufferable bufferable, CallbackInfoReturnable<Pair<BufferBuilder.RenderedBuffer, Integer>> cir) {
        BufferBuilderStateManager.setAllowExtend(false);
    }

    @Group(name = "getBufferBuilderTail", min = 1, max = 2)
    @Inject(method = "getRenderedBuffer(Lcom/jozufozu/flywheel/core/model/Bufferable;)Lcom/jozufozu/flywheel/util/Pair;", at = @At(value = "TAIL"),require = 0)
    private static void irisflw$getBufferBuilderTail(Bufferable bufferable, CallbackInfoReturnable<Pair<BufferBuilder.RenderedBuffer, Integer>> cir) {
        BufferBuilderStateManager.setAllowExtend(true);
    }

    @Inject(method = "getBufferBuilderFromTemplate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/renderer/RenderType;Ljava/util/Collection;Lcom/mojang/blaze3d/vertex/PoseStack;)Lcom/jozufozu/flywheel/util/Pair;", at = @At("HEAD"),require = 0)
    private static void irisflw$getBufferBuilderFromTemplateHead(BlockAndTintGetter renderWorld, RenderType layer, Collection<StructureTemplate.StructureBlockInfo> blocks, PoseStack poseStack, CallbackInfoReturnable<Pair<BufferBuilder.RenderedBuffer, Integer>> cir) {
        BufferBuilderStateManager.setAllowExtend(false);
    }

    @Inject(method = "getBufferBuilderFromTemplate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/renderer/RenderType;Ljava/util/Collection;Lcom/mojang/blaze3d/vertex/PoseStack;)Lcom/jozufozu/flywheel/util/Pair;", at = @At("RETURN"),require = 0)
    private static void irisflw$getBufferBuilderFromTemplateTail(BlockAndTintGetter renderWorld, RenderType layer, Collection<StructureTemplate.StructureBlockInfo> blocks, PoseStack poseStack, CallbackInfoReturnable<Pair<BufferBuilder.RenderedBuffer, Integer>> cir) {
        BufferBuilderStateManager.setAllowExtend(true);
    }
}
