package Bromod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Bromod.BroMod;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.Iterator;

import static Bromod.BroMod.*;

public class DragonsCurse extends CustomRelic implements OnAfterUseCardRelic {

    // ID, images, text.
    public static final String ID = BroMod.makeID("DragonsCurse");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DragonsCurse.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DragonsCurse.png"));

    private static int choice;
    private static int healthDamage;

    public DragonsCurse() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 0;
        this.choice = -1;
    }

    // Flash at the start of Battle.


    @Override
    public void atBattleStart() {
        this.flash();
        this.choice = MathUtils.random(0,3);
        changeTips();
        if (this.choice == 0){
            this.counter=0;
        }
        else if (this.choice == 3){
            this.healthDamage = (int) ((float) AbstractDungeon.player.maxHealth * 0.75);
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false); // Shake the screen
            CardCrawlGame.sound.play("BLUNT_FAST");  // Play a hit sound
            AbstractDungeon.player.decreaseMaxHealth(healthDamage);
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard abstractCard, UseCardAction useCardAction) {
        if (this.choice == 0){
            this.counter++;
            if (this.counter >= 4) {
                this.counter = 0;
                this.flash();
                this.playLandingSFX();
                AbstractDungeon.actionManager.cardQueue.clear();
                Iterator var3 = AbstractDungeon.player.limbo.group.iterator();

                while(var3.hasNext()) {
                    AbstractCard c = (AbstractCard)var3.next();
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                }

                AbstractDungeon.player.limbo.group.clear();
                AbstractDungeon.player.releaseCard();
                AbstractDungeon.overlayMenu.endTurnButton.disable(true);
            }
        }
    }

    @Override
    public void onPlayerEndTurn() {
        this.counter = 0;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (choice != 1){
            return damageAmount;
        }
        return (int)((float)damageAmount*0.5);
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        if (choice != 2){
            return (int)blockAmount;
        }
        return (int)(blockAmount* 0.5f);
    }

    @Override
    public void onVictory() {
        if (this.choice == 3){
            AbstractDungeon.player.increaseMaxHp(this.healthDamage, true);
        }
        this.choice = -1;
        changeTips();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
            switch (this.choice){
                case 0:
                    return DESCRIPTIONS[4];
                case 1:
                    return DESCRIPTIONS[3];
                case 2:
                    return DESCRIPTIONS[2];
                case 3:
                    return DESCRIPTIONS[1];

                default:
                    return DESCRIPTIONS[0];
            }
    }

    private void changeTips(){
        this.tips.clear();
        logger.info("Taken choice: " + this.choice);
        switch (this.choice){
            case 0:
                this.tips.add(new PowerTip(this.name,DESCRIPTIONS[4]));
                break;
            case 1:
                this.tips.add(new PowerTip(this.name,DESCRIPTIONS[3]));
                break;
            case 2:
                this.tips.add(new PowerTip(this.name,DESCRIPTIONS[2]));
                break;
            case 3:
                this.tips.add(new PowerTip(this.name,DESCRIPTIONS[1]));
                break;

            default:
                this.tips.add(new PowerTip(this.name,DESCRIPTIONS[0]));
                break;
        }
    }
}
