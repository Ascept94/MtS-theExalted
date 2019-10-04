package Bromod.powers;

import Bromod.util.MyTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import java.util.ArrayList;

import static Bromod.BroMod.makePowerPath;

public class nerfBurnPower extends TwoAmountPower implements CloneablePowerInterface, HealthBarRenderPower, OnReceivePowerPower {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("nerfBurnPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("HeatPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("HeatPower32.png"));
    // The Name requires to be of the form: NamePower

    public nerfBurnPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;
        this.amount2 = 1;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (power.ID == this.ID && this.amount > this.amount2){
            if (stackAmount > 1){
                this.amount2 += MathUtils.ceil((float)stackAmount/2f);
                updateDescription();
                return stackAmount/2;
            }
            this.amount2 += stackAmount;
            updateDescription();
            return 0;
        }
        else if (power.ID==this.ID){
            if (stackAmount > 1){
                this.amount2 += stackAmount/2;
                updateDescription();
                return MathUtils.ceil((float)stackAmount/2f);
            }
        }
        return stackAmount;
    }

    @Override
    public void atStartOfTurn() {
        DamageInfo dmgInfo = new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.THORNS);
        int i;
        for (i=0; i < amount2; i++){
            AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner,dmgInfo, AbstractGameAction.AttackEffect.FIRE));
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner,this.source, this.POWER_ID ));
    }
    @Override
    public int getHealthBarAmount() {
        return this.amount * this.amount2;
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    @Override
    public void updateDescription() {
        if (amount2 == 1){
            description = DESCRIPTIONS[0] + (amount) + DESCRIPTIONS[1];
        }
        else{
            description = DESCRIPTIONS[0] + (amount) + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new nerfBurnPower(owner, source, amount);
    }
}
