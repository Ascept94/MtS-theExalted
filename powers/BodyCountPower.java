package Bromod.powers;

import Bromod.util.MyTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import Bromod.DefaultMod;
import Bromod.util.TextureLoader;
import jdk.nashorn.internal.runtime.DebugLogger;
import sun.rmi.runtime.Log;
import sun.security.ssl.Debug;

import java.util.ArrayList;

import static Bromod.DefaultMod.makePowerPath;

public class BodyCountPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("BodyCountPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("BodyCountPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("BodyCountPower32.png"));
    // The Name requires to be of the form: NamePower

    private ArrayList<AbstractCard> AllCards = CardLibrary.getAllCards();
    private ArrayList<AbstractCard> PosibleCards = new ArrayList<AbstractCard>();

    private static boolean upgraded = false;

    public BodyCountPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final boolean upgraded) {
        name = NAME;
        ID = upgraded ? POWER_ID+"U" : POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;
        this.upgraded = upgraded;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        for (AbstractCard c : AllCards){
            if (c.tags.contains(MyTags.COMBO)){
                PosibleCards.add(c);
            }
        }

        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (((AbstractMonster)target).isDying || target.currentHealth <= damageAmount) {
            int i;
            for (i = 0; i < amount; i++) {
                AbstractCard c = PosibleCards.get(MathUtils.random(PosibleCards.size()-1));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
                if (this.upgraded){
                    c.upgrade();
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (amount==1) {
            description = upgraded ? DESCRIPTIONS[0] + DESCRIPTIONS[3] : DESCRIPTIONS[0] + DESCRIPTIONS[1];
        }
        else    {
            description = upgraded ? DESCRIPTIONS[0] + amount + DESCRIPTIONS[4] : DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new BodyCountPower(owner, source, amount, upgraded);
    }
}
