package me.ilucah.advancedarmor.api;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.Armor;
import me.ilucah.advancedarmor.armor.ArmorColor;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import me.ilucah.advancedarmor.boosting.model.BoostService;
import me.ilucah.advancedarmor.boosting.model.TypeProvider;
import me.ilucah.advancedarmor.handler.Handler;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.List;

public class AdvancedArmorAPI {

    private static AdvancedArmorAPI instance;

    public static AdvancedArmorAPI getInstance() {
        return instance;
    }

    private final Handler handler;

    public AdvancedArmorAPI(Handler handler) {
        instance = this;
        this.handler = handler;
    }

    public <T extends Event> void registerBoostProvider(Class<T> clazz, BoostProvider<T> provider) {
        handler.getProviders().putIfAbsent(clazz, provider);
        provider.register();
    }

    public void registerProviders(TypeProvider provider, Class<? extends Event>... typeListeners) {
        for (Class<? extends Event> clazz : typeListeners) {
            handler.getProviders().putIfAbsent(clazz, provider);
        }
        provider.register();
    }

    @Nullable
    public TypeProvider getProvider(Class<? extends Event> clazz) {
        return handler.getProviders().get(clazz);
    }

    public void removeProvider(Class<? extends Event> clazz) {
        handler.getProviders().remove(clazz);
    }

    public void registerNewArmorSet(Armor armor) {
        handler.addArmor(armor);
    }

    public void removeArmorSet(Armor armor) {
        handler.removeArmor(armor);
    }

    public Handler getHandler() {
        return handler;
    }

    public Armor getArmor(String name) {
        return handler.getArmorFromString(name);
    }

    public BoostService getBoostService() {
        return handler.getBoostService();
    }

    public List<Armor> getArmors() {
        return handler.getArmor();
    }

    public List<ArmorColor> getArmorColors() {
        return handler.getArmorColors();
    }

    public AdvancedArmor getPlugin() {
        return handler.getPlugin();
    }

}
