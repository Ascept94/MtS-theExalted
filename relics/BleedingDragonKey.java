package Bromod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Bromod.BroMod;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.helpers.ScreenShake;

import static Bromod.BroMod.makeRelicOutlinePath;
import static Bromod.BroMod.makeRelicPath;

public class BleedingDragonKey extends CustomRelic {

    // ID, images, text.
    public static final String ID = BroMod.makeID("BleedingDragonKey");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BleedingDragonKey.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BleedingDragonKey.png"));


    public BleedingDragonKey() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }


    @Override
    public void onEquip() {
        int healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * 0.75);
        this.counter = healthdamage;
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false); // Shake the screen
        CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
        AbstractDungeon.player.decreaseMaxHealth(healthdamage);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
