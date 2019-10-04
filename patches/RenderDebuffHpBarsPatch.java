package Bromod.patches;

import Bromod.BroMod;
import Bromod.characters.TheExalted;
import Bromod.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.powerInterfaces.HealthBarRenderPowerPatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "renderHealth"
)

public class RenderDebuffHpBarsPatch {

    private static final float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    private static final float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;

    private static final Color redHpBarColor = new Color(0.8F, 0.05F, 0.05F, 1.0F);
    private static final Color blueHpBarColor = Color.valueOf("31568c00");

    private static Texture BleedHpMarker = TextureLoader.getTexture("BromodResources/images/ui/BleedHpMarker.png");
    private static final Texture BurnHpMarker = TextureLoader.getTexture("BromodResources/images/ui/BurnHpMarker.png");

    private static final String burnID = TheExalted.hasAscaris() ? "Bromod:nerfBurnPower" : "Bromod:BurnPower";

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"x", "y"}
    )

    // you can add a private variable of the method by adding it to the patch like AbstractCard ___card.
    public static void RenderGasHealthBar(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
        if(!BroMod.enableDebuffHealthBars){return;}

        if (__instance.hasPower("Bromod:IntoxicationPower")){
            renderGasHpBar(__instance,sb,x,y);
        }
        if (__instance.hasPower(burnID)){
            renderBurnHpBar(__instance,sb,x,y);
        }
        if (__instance.hasPower("Bromod:BleedPower")){
            renderBleedHpBar(__instance,sb,x,y);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {

            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "renderHealthText");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    static private void renderGasHpBar(AbstractCreature creature,SpriteBatch sb, float x, float y) {
        float targetHealthBarWidth = creature.hb.width * (float)creature.currentHealth / (float)creature.maxHealth;

        int gasAmt = creature.getPower("Bromod:IntoxicationPower").amount;
        if (gasAmt > 0 && creature.hasPower("Intangible")) {
            gasAmt = 1;
        }
        float g = (gasAmt) / (float)creature.currentHealth * targetHealthBarWidth;
        float p = 0;

        if (creature.hasPower("Poison")) {
            int poisonAmt = creature.getPower("Poison").amount;
            if (poisonAmt > 0 && creature.hasPower("Intangible")) {
                poisonAmt = 1;
            }
            p = (poisonAmt) / (float)creature.currentHealth * targetHealthBarWidth;
        }

        // At the start of the Health Bar (x) plus the HealthBarWidth, which is the end of the Health Bar. (Minus the offset for the curved end/start (HEALTH_BAR_HEIGHT))
        // Minus the poison and gas amounts, previously scaled to the healthBarWidth (g and p). The end goes in L + g
        float L = x + (targetHealthBarWidth - g - p);
        float R = x + (targetHealthBarWidth - p);
        if (L < x){L = x; g = R-x;} //Prevent's clipping when on low HP
        if (R < x){R = x; g = 0;}

        sb.setColor(Color.OLIVE);
        sb.draw(ImageMaster.HEALTH_BAR_B, L, y + HEALTH_BAR_OFFSET_Y, g, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, R, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);

        if (L != x) { //draw this last so that the red HP bar ends up like usual
            sb.setColor(redHpBarColor);
            if (creature.currentBlock > 0){sb.setColor(blueHpBarColor);}
            sb.draw(ImageMaster.HEALTH_BAR_R, L, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }
    }

    static private void renderBurnHpBar(AbstractCreature creature,SpriteBatch sb, float x, float y) {
        float targetHealthBarWidth = creature.hb.width * (float)creature.currentHealth / (float)creature.maxHealth;
        sb.setColor(Color.OLIVE);

        int BurnAmt = creature.getPower(burnID).amount;
        if (BurnAmt > 0 && creature.hasPower("Intangible")) {
            BurnAmt = 1;
        }

        float b = (BurnAmt*creature.getPower(burnID).amount) / (float)creature.currentHealth * targetHealthBarWidth;//Burn applies multiple times equal to the amount.
        if(TheExalted.hasAscaris()){b = (BurnAmt*((TwoAmountPower)creature.getPower(burnID)).amount2) / (float)creature.currentHealth * targetHealthBarWidth;}
        float g = 0;
        float p = 0;

        if (creature.hasPower("Poison")) {
            int poisonAmt = creature.getPower("Poison").amount;
            if (poisonAmt > 0 && creature.hasPower("Intangible")) {
                poisonAmt = 1;
            }
            p = (poisonAmt) / (float)creature.currentHealth * targetHealthBarWidth;
        }

        if (creature.hasPower("Bromod:IntoxicationPower")) {
            int gasAmt = creature.getPower("Bromod:IntoxicationPower").amount;
            if (gasAmt > 0 && creature.hasPower("Intangible")) {
                gasAmt = 1;
            }
            g = (gasAmt) / (float)creature.currentHealth * targetHealthBarWidth; //Burn applies multiple times equal to the amount.
        }

        float L = x + (targetHealthBarWidth - b - g - p);
        float R = x + (targetHealthBarWidth - g - p);
        if (L < x){L = x; b = R-x;}
        if (R < x){R = x; b = 0;}

        sb.setColor(Color.ORANGE);
        sb.draw(BurnHpMarker, R- HEALTH_BAR_HEIGHT/3, y + HEALTH_BAR_OFFSET_Y + HEALTH_BAR_HEIGHT/6f, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT*1.1f);
        sb.draw(ImageMaster.HEALTH_BAR_B, L, y + HEALTH_BAR_OFFSET_Y, b, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, R, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);

        if (L != x) { //draw this last so that the red HP bar ends up like usual
            sb.setColor(redHpBarColor);
            if (creature.currentBlock > 0){sb.setColor(blueHpBarColor);}
            sb.draw(ImageMaster.HEALTH_BAR_R, L, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }
    }

    static private void renderBleedHpBar(AbstractCreature creature,SpriteBatch sb, float x, float y) {
        float targetHealthBarWidth = creature.hb.width * (float)creature.currentHealth / (float)creature.maxHealth;
        int bleedAmt = creature.getPower("Bromod:BleedPower").amount * creature.maxHealth/100;
        bleedAmt = bleedAmt == 0 ? 1 : bleedAmt;
        if (bleedAmt > 0 && creature.hasPower("Intangible")) {
            bleedAmt = 1;
        }

        float s = (bleedAmt) / (float)creature.currentHealth * targetHealthBarWidth;
        float b = 0;
        float g = 0;
        float p = 0;

        if (creature.hasPower("Poison")) {
            int poisonAmt = creature.getPower("Poison").amount;
            if (poisonAmt > 0 && creature.hasPower("Intangible")) {
                poisonAmt = 1;
            }
            p = (poisonAmt) / (float)creature.currentHealth * targetHealthBarWidth;
        }

        if (creature.hasPower("Bromod:IntoxicationPower")) {
            int gasAmt = creature.getPower("Bromod:IntoxicationPower").amount;
            if (gasAmt > 0 && creature.hasPower("Intangible")) {
                gasAmt = 1;
            }
            g = (gasAmt) / (float)creature.currentHealth * targetHealthBarWidth;
        }

        if (creature.hasPower(burnID)) {
            int BurnAmt = creature.getPower(burnID).amount;
            if (BurnAmt > 0 && creature.hasPower("Intangible")) {
                BurnAmt = 1;
            }
            b = BurnAmt*creature.getPower(burnID).amount / (float)creature.currentHealth * targetHealthBarWidth; //Burn applies multiple times equal to the amount.
            if(TheExalted.hasAscaris()){b = (BurnAmt*((TwoAmountPower)creature.getPower(burnID)).amount2) / (float)creature.currentHealth * targetHealthBarWidth;}
        }

        // At the start of the Health Bar (x) plus the HealthBarWidth, which is the end of the Health Bar. (Minus the offset for the curved end/start (HEALTH_BAR_HEIGHT))
        // Minus the poison and gas amounts, previously scaled to the healthBarWidth (g and p). The end goes in L + g
        float L = x + (targetHealthBarWidth - s - b - g - p);
        float R = x + (targetHealthBarWidth - b - g - p);

        BleedHpMarker = TextureLoader.getTexture("BromodResources/images/ui/BleedHpMarker.png");
        if (L <= x){
            L = x;
            s = R-x;
            BleedHpMarker = TextureLoader.getTexture("BromodResources/images/ui/BleedHpMarkerDie.png");
        }
        if (R < x){R = x; s = 0;}

        sb.setColor(Color.RED);
        sb.draw(ImageMaster.HEALTH_BAR_B, L, y + HEALTH_BAR_OFFSET_Y, s, HEALTH_BAR_HEIGHT);

        if (creature.currentHealth > 0) { //draw this last so that the red HP bar ends up like usual
            sb.draw(ImageMaster.HEALTH_BAR_R, R, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.setColor(redHpBarColor);
            if (creature.currentBlock > 0){sb.setColor(blueHpBarColor);}
            sb.setColor(Color.WHITE);
            sb.draw(BleedHpMarker, L - HEALTH_BAR_HEIGHT *0.8f / 2f, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT* 0.8f, HEALTH_BAR_HEIGHT);
        }
    }
    
}
