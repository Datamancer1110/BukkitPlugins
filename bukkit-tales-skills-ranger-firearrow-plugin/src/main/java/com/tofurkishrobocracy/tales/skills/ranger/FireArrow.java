package com.tofurkishrobocracy.tales.skills.ranger;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.Skill;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author Cody
 */
public class FireArrow extends ActiveSkill {

    private final HashSet<Projectile> arrows = new HashSet<Projectile>();
    private static final int fireTicksPerSkillLevel = 10;
    private static final double flatArrowDamageMultiplierPerSkillLevel = 1;

    public FireArrow(Heroes plugin) {
        super(plugin, "FireArrow");
        setDescription("You launch a flaming arrow that deals damage to your target and sets them on fire.");
        setUsage("/skill firearrow");
        setArgumentRange(0, 0);
        setIdentifiers("skill firearrow");
        setTypes(SkillType.FIRE, SkillType.SILENCABLE, SkillType.DAMAGING, SkillType.HARMFUL);
        Bukkit.getServer().getPluginManager().registerEvents(new SkillEntityListener(this), plugin);
    }

    @Override
    public ConfigurationSection getDefaultConfig() {
        ConfigurationSection node = super.getDefaultConfig();
        node.set(SkillSetting.DAMAGE.node(), 3);
        node.set(SkillSetting.APPLY_TEXT.node(), "%target% has been ignited by %hero%!");
        node.set(SkillSetting.EXPIRE_TEXT.node(), "%target% is no longer on fire!");
        node.set(SkillSetting.REAGENT.node(), Material.ARROW.getId());
        node.set(SkillSetting.REAGENT_COST.node(), 1);
        return node;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public SkillResult use(Hero hero, String[] args) {
        Player player = hero.getPlayer();
        if (player.getItemInHand().getType() != Material.BOW) {
            return SkillResult.CANCELLED;
        }
        Location location = player.getEyeLocation();
        float pitch = location.getPitch() / 180.0F * 3.1415927F;
        float yaw = location.getYaw() / 180.0F * 3.1415927F;
        double motX = -Math.sin(yaw) * Math.cos(pitch);
        double motZ = Math.cos(yaw) * Math.cos(pitch);
        double motY = -Math.sin(pitch);
        Vector velocity = new Vector(motX, motY, motZ);
        Projectile arrow = player.launchProjectile(Arrow.class, velocity);
        arrows.add(arrow);
        broadcastExecuteText(hero);
        return SkillResult.NORMAL;
    }

    @Override
    public String getDescription(Hero hero) {
        return super.getDescription();
    }

    public class SkillEntityListener implements Listener {

        private final Skill skill;

        public SkillEntityListener(Skill skill) {
            this.skill = skill;
        }

        @EventHandler()
        public void onEntityDamage(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Projectile) || !(((Projectile) event.getDamager()).getShooter() instanceof Player) || !arrows.contains((Projectile) event.getDamager())) {
                return;
            }
            arrows.remove((Projectile) event.getDamager());
            Hero hero = plugin.getCharacterManager().getHero((Player) ((Projectile) event.getDamager()).getShooter());
            int skillLevel = hero.getSkillLevel(skill);
            event.setDamage(event.getDamage() * (skillLevel * flatArrowDamageMultiplierPerSkillLevel));
            event.getEntity().setFireTicks(fireTicksPerSkillLevel * skillLevel);
        }
    }
}
