package Bromod.powers;

import Bromod.actions.FoundryAction;
import Bromod.characters.TheExalted;
import Bromod.util.MyTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import java.util.ArrayList;

import static Bromod.BroMod.COLOR_BRO;
import static Bromod.BroMod.makePowerPath;

public class FoundryPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("FoundryPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("FoundryPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("FoundryPower32.png"));
    // The Name requires to be of the form: NamePower

    private ArrayList<AbstractCard> AllCards = CardLibrary.getAllCards();
    private ArrayList<AbstractCard> PossibleCards = new ArrayList<>();

    private static int IDoffset;

    public FoundryPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID + IDoffset;
        IDoffset++;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        for (AbstractCard c : AllCards){
            if (c.color == TheExalted.Enums.COLOR_BRO){
                PossibleCards.add(c);
            }
        }

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new FoundryAction(AbstractDungeon.player,AbstractDungeon.player,1, this));
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount == 0){
            AbstractDungeon.actionManager.addToBottom(new FoundryAction(AbstractDungeon.player,AbstractDungeon.player,1, this));
        }
        else{
            int i;
            for (i=0; i<this.amount;i++){
                AbstractCard c = PossibleCards.get(MathUtils.random(PossibleCards.size()-1));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
            }
            this.amount = 0;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 0){
            int i = MathUtils.random(6);
            description = DESCRIPTIONS[i];
        }
        else{
            if (this.amount == 1){
                description = DESCRIPTIONS[7] + amount + DESCRIPTIONS [8];
            }
            else{
                description = DESCRIPTIONS[7] + amount + DESCRIPTIONS [9];
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FoundryPower(owner, source, amount);
    }
}
