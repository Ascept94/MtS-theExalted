package Bromod.powers;

import Bromod.BroMod;
import Bromod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class CorrosivePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("CorrosivePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/CorrosivePower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/CorrosivePower32.png");

    public CorrosivePower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;

        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL){return;}
        int CHANCE = amount*10;
        while(CHANCE >= 100){
            AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(target, info.owner));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target,info.owner, new VulnerablePower(target,1,false)));
            CHANCE -= 100;
        }
        if(AbstractDungeon.miscRng.random(99) <= (CHANCE-1)){
            AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(target, info.owner));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target,info.owner, new VulnerablePower(target,1,false)));
        }
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner,this.source,POWER_ID + "1"));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount*10) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CorrosivePower(owner, source, amount);
    }
}
