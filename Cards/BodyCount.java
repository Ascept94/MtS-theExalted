package Bromod.cards;

import Bromod.powers.BodyCountPower;
import Bromod.util.MyTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static Bromod.DefaultMod.makeCardPath;

public class BodyCount extends AbstractComboCard {

    public static final String ID = DefaultMod.makeID(BodyCount.class.getSimpleName());
    public static final String IMG = makeCardPath("BodyCount.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //
    private static final CardTarget TARGET = CardTarget.SELF;  //
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = TheExalted.Enums.MOD_COLOR_COMMON;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 0;
    private static final int UPGRADE_PLUS_DMG = 0;

    private static final int AMOUNT = 1;
    private static final int UPGRADE_PLUS_AMOUNT = 0;

    private static final int BLOCK = 1;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int SECOND_AMOUNT = 1;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    private int COMBO = 0;

    // /STAT DECLARATION/


    public BodyCount() {
        super(ID,IMG,COST,TYPE,COLOR,RARITY,TARGET);
    baseDamage = DAMAGE;
    baseBlock = BLOCK;
    baseMagicNumber = magicNumber = AMOUNT;
    defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
    this.tags.add(MyTags.COMBO);
}

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (c.tags.contains(MyTags.COMBO) && this.COMBO <= 2) {
            this.COMBO = this.COMBO < 2 ? this.COMBO + 1 : 2;
            this.modifyCostForTurn(-1);
            this.ComboCounter = this.COMBO;
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new BodyCountPower(p,p,magicNumber, upgraded), magicNumber));
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
            //this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
