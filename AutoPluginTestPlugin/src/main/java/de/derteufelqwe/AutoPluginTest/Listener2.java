package de.derteufelqwe.AutoPluginTest;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@MCListener
public class Listener2 implements Listener {

    @EventHandler
    public void onTest(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.GRAY + event.getPlayer().getDisplayName() + " hat den Server verlassen.");
    }

}
