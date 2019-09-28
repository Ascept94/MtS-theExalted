package Bromod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import static Bromod.BroMod.makePowerPath;

public class BerserkerPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("BerserkerPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("BerserkerPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("BerserkerPower32.png"));
    // The Name requires to be of the form: NamePower
    private static int storeAmount;
    private static int IDoffset = 0;


    public BerserkerPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID + IDoffset;
        IDoffset++;

        this.owner = owner;
        this.amount = amount;
        this.storeAmount = amount;
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
        if (this.amount == 0){return;}
        if(card.type == AbstractCard.CardType.ATTACK){
            if (this.amount == 1){
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player,1));
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
                this.amount = 0;
                updateDescription();
            }
            else{
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, this.ID, 1));
            }
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player, this, storeAmount-this.amount));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[3];
        if (this.amount > 1){
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }
        else if (amount == 1) {
            description = DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new BerserkerPower(owner, source, amount);
    }
}
