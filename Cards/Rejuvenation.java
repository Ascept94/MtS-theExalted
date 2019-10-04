package Bromod.cards;

import Bromod.powers.TempRegenPower;
import Bromod.relics.AscarisDevice;
import Bromod.util.MyTags;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RelicGetSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import javax.smartcardio.Card;

import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class Rejuvenation extends AbstractDynamicCard {

    public static final String ID = BroMod.makeID(Rejuvenation.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Rejuvenation.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 0;
    private static final int UPGRADE_PLUS_DMG = 0;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 3;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    private static final int SECOND_AMOUNT = 1;
    private static final int UPGRADE_SECOND_AMOUNT = 1;

    // /STAT DECLARATION/


    public Rejuvenation() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
        this.tags.add(MyTags.NERF);
        this.rawDescription = TheExalted.hasAscaris()? this.EXTENDED_DESCRIPTION[0] : this.DESCRIPTION;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (TheExalted.hasAscaris()){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new TempRegenPower(p,p,defaultSecondMagicNumber),defaultSecondMagicNumber));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new RegenPower(p,magicNumber),magicNumber));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            upgradeBaseCost(UPGRADED_COST);
            upgradeDefaultSecondMagicNumber(UPGRADE_SECOND_AMOUNT);
            //this.rawDescription = this.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void update() {
        super.update();
        this.rawDescription = TheExalted.hasAscaris()? this.EXTENDED_DESCRIPTION[0] : this.DESCRIPTION;
        initializeDescription();
    }
}
