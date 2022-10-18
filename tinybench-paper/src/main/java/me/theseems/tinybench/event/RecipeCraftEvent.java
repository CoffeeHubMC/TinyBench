package me.theseems.tinybench.event;

import me.theseems.tinybench.Slot;
import me.theseems.tinybench.item.Item;
import me.theseems.tinybench.recipe.Recipe;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class RecipeCraftEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Recipe recipe;
    private final UUID playerUUID;
    private final Map<Slot, Item> inputs;
    private final Map<Slot, Item> rewards;
    private final Map<Slot, Item> leftovers;
    private boolean cancelled;

    public RecipeCraftEvent(Recipe recipe,
                            UUID playerUUID,
                            Map<Slot, Item> inputs,
                            Map<Slot, Item> rewards,
                            Map<Slot, Item> leftovers) {
        this.recipe = recipe;
        this.playerUUID = playerUUID;
        this.inputs = inputs;
        this.rewards = rewards;
        this.leftovers = leftovers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Map<Slot, Item> getInputs() {
        return inputs;
    }

    public Map<Slot, Item> getRewards() {
        return rewards;
    }

    public Map<Slot, Item> getLeftovers() {
        return leftovers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
