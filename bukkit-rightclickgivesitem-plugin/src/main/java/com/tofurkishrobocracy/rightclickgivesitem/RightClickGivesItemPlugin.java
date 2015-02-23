/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.rightclickgivesitem;

import com.tofurkishrobocracy.rightclickgivesitem.persistance.PlayerPersistence;
import com.tofurkishrobocracy.rightclickgivesitem.persistance.RightClickGivesItemPersistence;
import com.tofurkishrobocracy.rightclickgivesitem.persistance.impl.RightClickGivesItemPersistenceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cody
 */
public class RightClickGivesItemPlugin extends JavaPlugin {

    public static RightClickGivesItemPlugin instance;
    public Material drop = Material.getMaterial("GOLD_ORE");
    public Material block = Material.getMaterial("YELLOW_FLOWER");
    public boolean destroy = false;
    public boolean cancel = true;
    public int delayInSeconds = 0;
    public RightClickGivesItemPersistence persistence;

    @Override
    public void onEnable() {
        instance = this;
        persistence = new RightClickGivesItemPersistenceImpl(this);
        setupDatabase();
        getConfig().options().copyDefaults(true);
        saveConfig();
        try {
            validateConfigYml();
            block = Material.getMaterial(getConfig().getString("block_type_that_gives_items"));
            drop = Material.getMaterial(getConfig().getString("item_to_give"));
            destroy = getConfig().getBoolean("destroy_block_on_right_click");
            cancel = getConfig().getBoolean("cancel_normal_right_click_event");
            delayInSeconds = Math.max(getConfig().getInt("delay_between_clicks_in_seconds"), 0);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to Enable RightClickGivesItem", ex);
        }
        getCommand("rightclickgivesitem").setExecutor(new CommandExecutor(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void validateConfigYml() throws Exception {
        String blockTypeThatGivesItems = getConfig().getString("block_type_that_gives_items");
        String itemToGive = getConfig().getString("item_to_give");
        if (blockTypeThatGivesItems == null || blockTypeThatGivesItems.isEmpty()) {
            this.getLogger().log(Level.SEVERE, "'block_type_that_gives_items' could not be parsed; null or blank");
            throw new Exception("'block_type_that_gives_items' could not be parsed; null or blank");
        }
        if (itemToGive == null || itemToGive.isEmpty()) {
            this.getLogger().log(Level.SEVERE, "'item_to_give' could not be parsed; is null or blank");
            throw new Exception("'item_to_give' could not be parsed; is null or blank");
        }
        Material bttgi = Material.getMaterial(blockTypeThatGivesItems);
        Material itg = Material.getMaterial(itemToGive);
        if (bttgi == null) {
            this.getLogger().log(Level.SEVERE, "'block_type_that_gives_items' item named '" + blockTypeThatGivesItems + "' does not exist!");
            throw new Exception("'block_type_that_gives_items' item named '" + blockTypeThatGivesItems + "' does not exist!");
        }
        if (itg == null) {
            this.getLogger().log(Level.SEVERE, "'item_to_give' item named '" + itemToGive + "' does not exist!");
            throw new Exception("'item_to_give' item named '" + itemToGive + "' does not exist!");
        }
    }

    private void setupDatabase() {
        try {
            if (getDatabase() != null) {
                getDatabase().find(PlayerPersistence.class).findRowCount();
                return;
            }
        } catch (PersistenceException ex) {
            //Install db, below
        }
        System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
        installDDL();
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(PlayerPersistence.class);
        return list;
    }
}
