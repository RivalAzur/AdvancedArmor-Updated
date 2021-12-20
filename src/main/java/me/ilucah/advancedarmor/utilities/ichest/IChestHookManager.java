package me.ilucah.advancedarmor.utilities.ichest;

import me.ilucah.advancedarmor.AdvancedArmor;

public class IChestHookManager {

    private IHook hook;

    public IChestHookManager(AdvancedArmor plugin, HookType hook) {
        if (hook == HookType.ESSENTIALS) {
            this.hook = new IEssentialsHook(plugin);
        }
        if (hook == HookType.SHOPGUIPLUS) {
            this.hook = new IShopGUIPlusHook(plugin);
        }
    }

    public IHook getEconomyHook() {
        return this.hook;
    }

}
