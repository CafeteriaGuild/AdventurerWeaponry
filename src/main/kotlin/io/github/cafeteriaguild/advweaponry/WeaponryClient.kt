package io.github.cafeteriaguild.advweaponry

import net.fabricmc.api.ClientModInitializer

object WeaponryClient : ClientModInitializer {
    override fun onInitializeClient() {
        println("Hello Client World!")
    }
}