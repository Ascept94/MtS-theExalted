package Bromod.potions;

import Bromod.BroMod;
import Bromod.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.unique.TransmuteAction;
import com.megacrit.cardcrawl.actions.unique.Transmutev2Action;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import static Bromod.BroMod.makePotionPath;

public class ClemClone extends AbstractPotion {


    public static final String POTION_ID = BroMod.makeID("ClemClone");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    private static final Texture containerImg = TextureLoader.getTexture(makePotionPath("ClemClone.png"));;

    public ClemClone() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main BroMod.java
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.T, PotionColor.SMOKE);

        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();

        // Initialize the Description
        description = DESCRIPTIONS[0];
        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;

        // Initialize the on-hover name + description
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            DamageInfo dmg;
            float P = 100;
            while (AbstractDungeon.cardRandomRng.random(99)<P){
                dmg = new DamageInfo(AbstractDungeon.player,AbstractDungeon.cardRandomRng.random(3)+1 + potency, DamageInfo.DamageType.THORNS);
                AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(dmg, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                P -= P / 30;
            }
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.draw(this.containerImg, this.posX - 35.0F, this.posY - 34.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale*0.9f, this.scale, 0, 0, 0, 64, 64, false, false);
    }
    @Override
    public AbstractPotion makeCopy() {
        return new ClemClone();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 0;
    }

    public void upgradePotion()
    {
        potency += 1;
        tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
