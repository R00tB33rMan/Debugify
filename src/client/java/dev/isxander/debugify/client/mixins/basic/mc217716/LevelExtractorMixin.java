/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.mixins.basic.mc217716;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-217716", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "The green nausea overlay isn't removed when switching into spectator mode")
@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {
	@Shadow @Final
	private Minecraft minecraft;

	@Definition(id = "getEffectBlendFactor", method = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F")
	@Definition(id = "NAUSEA", field = "Lnet/minecraft/world/effect/MobEffects;NAUSEA:Lnet/minecraft/core/Holder;")
	@Expression("?.getEffectBlendFactor(NAUSEA, ?)")
	@ModifyExpressionValue(method = "extractPlayerState", at = @At("MIXINEXTRAS:EXPRESSION"))
	private float shouldShowNauseaOverlay(float original) {
		return minecraft.player.isSpectator() ? 0.0f : original;
	}
}
