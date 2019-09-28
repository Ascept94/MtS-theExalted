package Bromod.powers;

import Bromod.BroMod;
import Bromod.util.MyTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.util.TextureLoader;
import java.util.ArrayList;

import static Bromod.BroMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class ComboPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("ComboPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static ArrayList<AbstractCard> AFFECTED_CARDS = new ArrayList<AbstractCard>();
    public static ArrayList<Integer> ORIGINAL_DAMAGE = new ArrayList<Integer>();

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ComboPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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

    // On use card, apply (amount) of Dexterity. (Go to the actual power card for the amount.)

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.ID == this.POWER_ID ){
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, this.POWER_ID));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new Combo2Power(target, source, 1), 0, true));
        }
        int i;
        for(i = 0; i < AbstractDungeon.player.hand.group.size(); ++i) {
            AbstractCard c = AbstractDungeon.player.hand.group.get(i);
            AFFECTED_CARDS.add(c);
            ORIGINAL_DAMAGE.add(c.baseDamage);
            if (c.tags.contains(MyTags.COMBO)){
                c.modifyCostForTurn(-1);
                c.baseDamage = AbstractDungeon.player.hand.group.get(i).baseDamage*2;
                c.isDamageModified = true;
            }
        }
    }

    // Note: If you want to apply an effect when a power is being applied you have 3 options:
    //onInitialApplication is "When THIS power is first applied for the very first time only."
    //onApplyPower is "When the owner applies a power to something else (only used by Sadistic Nature)."
    //onReceivePowerPower from StSlib is "When any (including this) power is applied to the owner."


    // At the end of the turn, remove gained Dexterity.
    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, this.POWER_ID));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ComboPower(owner, source, amount);
    }
}
