package Bromod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import java.util.ArrayList;

public class MagneticPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("MagneticPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/MagneticPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/MagneticPower32.png");

    public MagneticPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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

        ArrayList<AbstractPower> possiblePowers = new ArrayList<>();
        for (AbstractPower po : target.powers){
            if (po.type == AbstractPower.PowerType.BUFF && po.amount >= 1 && po.ID != "Fading" && po.ID != "Minion" && po.ID != "Life Link" && po.ID != "BeatOfDeath"){
                possiblePowers.add(po);
            }
        }
        if (possiblePowers.size() == 0){return;}

        AbstractPower powerToRemove = possiblePowers.get(MathUtils.random(0,possiblePowers.size()-1));

        int CHANCE = amount*10;
        while(CHANCE >= 100){
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(target,info.owner,powerToRemove.ID,1));

            possiblePowers.clear();
            for (AbstractPower po : target.powers){
                if (po.type == AbstractPower.PowerType.BUFF && po.amount >= 1){
                    possiblePowers.add(po);
                }
            }
            if (possiblePowers.size() == 0){return;}

            CHANCE -= 100;
        }
        if(AbstractDungeon.miscRng.random(99) <= (CHANCE-1)){
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(target,info.owner,powerToRemove.ID,1));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount*10) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MagneticPower(owner, source, amount);
    }
}
