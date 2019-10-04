package Bromod.powers;

import Bromod.BroMod;
import Bromod.characters.TheExalted;
import Bromod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BleedPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("BleedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/BleedPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/BleedPower32.png");

    public BleedPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;

        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    @Override
    public void atStartOfTurn() {
        int dmgAmt = amount*owner.maxHealth/100;
        dmgAmt = dmgAmt==0? 1:dmgAmt;
        DamageInfo dmgInfo = new DamageInfo(AbstractDungeon.player, dmgAmt, DamageInfo.DamageType.HP_LOSS);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(owner, dmgInfo, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (TheExalted.hasAscaris()){
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner,owner,this,1));
        }
    }

    @Override
    public int getHealthBarAmount() {
        int dmgAmt = amount*owner.maxHealth/100;
        dmgAmt = dmgAmt==0? 1:dmgAmt;
        return dmgAmt;
    }

    @Override
    public Color getColor() {
        return Color.SCARLET;
    }

    @Override
    public void updateDescription() {
        int dmgAmt = amount*owner.maxHealth/100;
        dmgAmt = dmgAmt==0? 1:dmgAmt;
        description = DESCRIPTIONS[0] + (dmgAmt) + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        description = TheExalted.hasAscaris()? description + DESCRIPTIONS[3] : description;
    }

    @Override
    public AbstractPower makeCopy() {
        return new BleedPower(owner, source, amount);
    }
}
