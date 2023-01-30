package me.quesia.peepopractice.mixin.structure;

import me.quesia.peepopractice.PeepoPractice;
import me.quesia.peepopractice.core.ExecuteAtIndexArrayList;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StructureStart.class)
public class StructureStartMixin {
    @SuppressWarnings("unused") @Mutable @Shadow @Final protected List<StructurePiece> children;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void customChildrenListType(StructureFeature<?> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed, CallbackInfo ci) {
        this.children = new ExecuteAtIndexArrayList<>(piece -> {
            if (PeepoPractice.CURRENT_ORIENTATION != null) {
                piece.setOrientation(PeepoPractice.CURRENT_ORIENTATION);
                PeepoPractice.CURRENT_ORIENTATION = null;
            }
        }, 0);
    }
}
