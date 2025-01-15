package com.stardevllc.itembuilder.material;

import com.stardevllc.config.Section;
import com.stardevllc.itembuilder.ItemBuilder;
import com.stardevllc.itembuilder.XMaterial;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.HashMap;
import java.util.Map;

import static com.stardevllc.mcwrappers.MCWrappers.ENCHANT_WRAPPER;

public class EnchantedBookBuilder extends ItemBuilder {
    
    static {
        ItemBuilder.META_TO_BUILDERS.put(EnchantmentStorageMeta.class, EnchantedBookBuilder.class);
    }

    protected Map<Enchantment, Integer> storedEnchants = new HashMap<>();
    
    public EnchantedBookBuilder() {
        super(XMaterial.ENCHANTED_BOOK);
    }

    public EnchantedBookBuilder(Enchantment enchantment, int level) {
        addStoredEnchant(enchantment, level);
    }
    
    protected static EnchantedBookBuilder createFromItemStack(ItemStack itemStack) {
        EnchantedBookBuilder itemBuilder = new EnchantedBookBuilder();
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
        itemBuilder.setStoredEnchants(itemMeta.getStoredEnchants());
        return itemBuilder;
    }

    protected static EnchantedBookBuilder createFromConfig(Section section) {
        EnchantedBookBuilder builder = new EnchantedBookBuilder();
        Section storedEnchantsSection = section.getSection("storedenchantments");
        if (storedEnchantsSection != null) {
            for (Object enchantName : storedEnchantsSection.getKeys()) {
                Enchantment enchantment = ENCHANT_WRAPPER.getEnchantmentByKey(enchantName.toString());
                int level = storedEnchantsSection.getInt(enchantName.toString());
                builder.addStoredEnchant(enchantment, level);
            }
        }
        return builder;
    }

    @Override
    public void saveToConfig(Section section) {
        super.saveToConfig(section);
        storedEnchants.forEach((enchant, level) -> section.set("storedenchantments." + ENCHANT_WRAPPER.getEnchantmentKey(enchant), level));
    }

    public EnchantedBookBuilder addStoredEnchant(Enchantment enchantment, int level) {
        this.storedEnchants.put(enchantment, level);
        return this;
    }
    
    public EnchantedBookBuilder setStoredEnchants(Map<Enchantment, Integer> enchants) {
        this.storedEnchants.clear();
        this.storedEnchants.putAll(enchants);
        return this;
    }
    
    @Override
    protected EnchantmentStorageMeta createItemMeta() {
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) super.createItemMeta();
        this.storedEnchants.forEach((enchant, level) -> itemMeta.addStoredEnchant(enchant, level, true));
        return itemMeta;
    }

    @Override
    public EnchantedBookBuilder clone() {
        EnchantedBookBuilder clone = (EnchantedBookBuilder) super.clone();
        clone.storedEnchants.putAll(this.storedEnchants);
        return clone;
    }
}
