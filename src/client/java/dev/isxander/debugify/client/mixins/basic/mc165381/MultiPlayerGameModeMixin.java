/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.mixins.basic.mc165381;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 26.3 moved dropping off LocalPlayer and onto MultiPlayerGameMode.dropItem, which is where the
 * selected stack is now taken from the inventory, so the destroy stop moved with it.
 */
@BugFix(id = "MC-165381", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Block breaking can be delayed by dropping/throwing the tool while breaking a block")
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Shadow
	public abstract void stopDestroyBlock();

	@Inject(
			method = "dropItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Inventory;removeFromSelected(Z)Lnet/minecraft/world/item/ItemStack;",
					shift = At.Shift.AFTER
			)
	)
	private void onDropItem(CallbackInfo ci) {
		this.stopDestroyBlock();
	}
}
