package Bromod.powers;

import Bromod.util.MyTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import java.util.Iterator;

import static Bromod.BroMod.makePowerPath;

public class RelentlessCombinationPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("RelentlessCombinationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("RelentlessCombinationPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("RelentlessCombinationPower32.png"));
    // The Name requires to be of the form: NamePower


    public RelentlessCombinationPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.tags.contains(MyTags.COMBO)){
            DrawCardWithTag(MyTags.COMBO);
        }
    }

    void DrawCardWithTag(AbstractCard.CardTags tag){
        Iterator var1 = AbstractDungeon.player.drawPile.group.iterator();
        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard) var1.next();
            if (c.tags.contains(tag)){
                AbstractDungeon.player.drawPile.removeCard(c);
                AbstractDungeon.player.hand.addToTop(c);
                break;
            }
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, this.POWER_ID));
    }

    @Override
    public void updateDescription() {
        if (amount == 1){
            description = DESCRIPTIONS[0] +  DESCRIPTIONS[1];
        }
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RelentlessCombinationPower(owner, source, amount);
    }
}
