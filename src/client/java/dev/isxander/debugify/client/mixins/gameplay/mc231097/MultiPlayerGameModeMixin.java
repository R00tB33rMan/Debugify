/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.mixins.gameplay.mc231097;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 26.3 moved dropping off LocalPlayer and onto MultiPlayerGameMode.dropItem, which hands the
 * player over as an argument, so the release reads it from there rather than from the mixin.
 */
@BugFix(id = "MC-231097", category = FixCategory.GAMEPLAY, env = BugFix.Env.CLIENT, description = "Holding the \"Use\" button continues to slow down the player even after the used item has been dropped")
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Shadow
	public abstract void releaseUsingItem(Player player);

	@Inject(
			method = "dropItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Inventory;removeFromSelected(Z)Lnet/minecraft/world/item/ItemStack;",
					shift = At.Shift.AFTER
			)
	)
	private void onDropItem(LocalPlayer player, boolean all, CallbackInfo ci) {
		if (Debugify.isGameplayFixesEnabled() && player.isUsingItem()) {
			this.releaseUsingItem(player);
		}
	}
}
