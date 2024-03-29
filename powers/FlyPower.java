package Bromod.powers;

import Bromod.BroMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.powers.FlightPower;

import static Bromod.BroMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class FlyPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("FlyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("FlyPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("FlyPower32.png"));

    private static int storedAmount;
    private static int superstoredAmount;


    public FlyPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final int storedAmount, final int superstoredAmount) {
        name = NAME;
        description = DESCRIPTIONS[0];
        this.ID = POWER_ID;

        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.storedAmount = storedAmount > this.storedAmount ? storedAmount : this.storedAmount;
        this.superstoredAmount = superstoredAmount > this.superstoredAmount ? superstoredAmount : this.superstoredAmount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void atStartOfTurn() {
        this.amount = storedAmount + superstoredAmount;
        this.storedAmount = 0;
        this.superstoredAmount = this.amount == 0? 0: this.superstoredAmount;
        if (amount == 0){
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner,owner,this));
        }
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (amount < 2){
            this.superstoredAmount = 0;
            this.updateDescription();
        }
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return this.calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type) {
        return type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS ? damage / 2.0F : damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        Boolean willLive = this.calculateDamageTakenAmount((float)damageAmount, info.type) < (float)this.owner.currentHealth;
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0 && willLive) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
        return damageAmount;
    }

    @Override
    public void onRemove() {
        this.storedAmount = 0;
        this.superstoredAmount = 0;
    }

    @Override
    public void onVictory() {
        this.storedAmount = 0;
        this.superstoredAmount = 0;
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1){
            description = DESCRIPTIONS[0];
        }
        else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
        if (this.storedAmount + this.superstoredAmount == 0){
            description += DESCRIPTIONS[3] + DESCRIPTIONS[5];
        }
        else{
            description += DESCRIPTIONS[4] + (storedAmount + superstoredAmount) + DESCRIPTIONS[5];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FlyPower(owner, source, amount, storedAmount, superstoredAmount);
    }
}
