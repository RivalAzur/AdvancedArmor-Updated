package me.ilucah.advancedarmor.boosting;

import org.bukkit.event.Event;

import java.util.function.Consumer;

/**
 * @param <T> The dominant Event.
 */
public interface TypeProvider<T extends Event> {

    default void registerAndThen(T t, Consumer<T> consumer) {
        register();
        consumer.accept(t);
    }

    void register();

}
