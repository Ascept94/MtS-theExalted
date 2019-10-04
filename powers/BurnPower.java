package Bromod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

public class BurnPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("BurnPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/HeatPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/HeatPower32.png");

    public BurnPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;

        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        DamageInfo dmgInfo = new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.THORNS);
        int i;
        for (i=0; i < amount; i++){
            AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner,dmgInfo, AbstractGameAction.AttackEffect.FIRE));
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner,this.source, this.POWER_ID ));
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount * this.amount;
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    @Override
    public void updateDescription() {
        if (amount == 1){
            description = DESCRIPTIONS[0] + (amount) + DESCRIPTIONS[1];
        }
        else{
            description = DESCRIPTIONS[0] + (amount) + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new BurnPower(owner, source, amount);
    }
}
