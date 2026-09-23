/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.mixins.basic.mc118740;

import dev.isxander.debugify.client.helpers.mc118740.LocalPlayerDuck;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SwingAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Taken from <a href="https://github.com/Moulberry/MoulberrysTweaks">MoulberrysTweaks</a>
 * under MIT license
 *
 * <p>26.3 removed LocalPlayer's swing override, which only sent the swing packet, and left the
 * swing itself on LivingEntity, so the reset hooks the base method and narrows to the local
 * player the same way the sibling Player mixin does.</p>
 *
 * @author Moulberry
 */
@BugFix(id = "MC-118740", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, modConflicts = "moulberrystweaks", description = "Performing any right-click action silently resets the attack cooldown")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "swing", at = @At("HEAD"))
	public void swing(InteractionHand hand, SwingAnimation animation, boolean sendToSwingingEntity, CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof LocalPlayerDuck localPlayerExt) {
			localPlayerExt.debugify$resetVisualAttackStrengthScale();
		}
	}
}
