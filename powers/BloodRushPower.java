package Bromod.powers;

import Bromod.util.MyTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import Bromod.DefaultMod;
import Bromod.util.TextureLoader;

import java.util.ArrayList;

import static Bromod.DefaultMod.makePowerPath;

public class BloodRushPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("BloodRushPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("BloodRushPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("BloodRushPower32.png"));
    // The Name requires to be of the form: NamePower

    private int COMBO = 0;


    public BloodRushPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK){return;}
        if (COMBO < 2) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (card.hasTag(MyTags.COMBO) && c.hasTag(MyTags.COMBO)){}
                else if (c.type == AbstractCard.CardType.ATTACK) {
                    c.modifyCostForTurn(-1);
                }
            }
        }
        COMBO = COMBO >= 2 ? COMBO : COMBO + 1;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL){return damage;}
        else{return damage*((int)Math.pow(2,this.COMBO));}
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, this.POWER_ID));
    }

    @Override
    public void updateDescription() {
        if (COMBO == 1){
            description = DESCRIPTIONS[1];
        }
        else if(COMBO == 2){
            description = DESCRIPTIONS[2];
        }
        else{
            description = DESCRIPTIONS[0];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new BloodRushPower(owner, source, amount);
    }
}
