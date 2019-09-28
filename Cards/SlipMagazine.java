package Bromod.cards;

import Bromod.powers.SlipMagazinePower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class SlipMagazine extends AbstractDynamicCard {

    public static final String ID = BroMod.makeID(SlipMagazine.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("SlipMagazine.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 1;
    private static final int UPGRADE_PLUS_DMG = 1;

    private static final int AMOUNT = 1;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    private static final int BLOCK = 1;
    private static final int UPGRADE_PLUS_BLOCK = 1;

    private static final int SECOND_AMOUNT = 1;
    private static final int UPGRADE_SECOND_AMOUNT = 1;

    // /STAT DECLARATION/


    public SlipMagazine(){
        super(ID,IMG,COST,TYPE,COLOR,RARITY,TARGET);
    baseDamage =DAMAGE;
    baseBlock =BLOCK;
    baseMagicNumber =magicNumber =AMOUNT;
    defaultBaseSecondMagicNumber =defaultSecondMagicNumber =SECOND_AMOUNT;
    setModBackground(this);
}


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new SlipMagazinePower(p,p,magicNumber) , magicNumber));
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
            this.rawDescription = this.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
