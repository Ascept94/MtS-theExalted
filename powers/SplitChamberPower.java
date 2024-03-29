package Bromod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import static Bromod.BroMod.makePowerPath;

public class SplitChamberPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("SplitChamberPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SplitChamberPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SplitChamberPower32.png"));
    // The Name requires to be of the form: NamePower
    private AbstractCard copyCard;


    public SplitChamberPower(final AbstractCreature owner, final AbstractCreature source, final int amount, AbstractCard CopyCard) {
        name = NAME;
        this.ID = POWER_ID + CopyCard.uuid;

        this.owner = owner;
        this.amount = amount;
        this.source = source;
        this.copyCard = CopyCard.makeStatEquivalentCopy();
        this.copyCard.resetAttributes();

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(this.copyCard, 1));
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner,owner,this.ID,1));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + FontHelper.colorString(this.copyCard.name, "y") + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SplitChamberPower(owner, source, amount, this.copyCard);
    }
}
