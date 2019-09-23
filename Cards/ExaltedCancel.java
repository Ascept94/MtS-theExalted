package Bromod.cards;



import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;


import static Bromod.DefaultMod.makeCardPath;

public class ExaltedCancel extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(ExaltedCancel.class.getSimpleName());
    public static final String IMG = makeCardPath("ExaltedCancel.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 0;
    private static final int UPGRADE_COST = 0;

    private static final int AMOUNT = 1;
    private static final int UPGRADE_AMOUNT = 1;

    // /STAT DECLARATION/


    public ExaltedCancel() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = AMOUNT;
        this.retain = true;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard c = new ExaltedBlade();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p, "Bromod:ExaltedPower"));
        if (upgraded){
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:ChromaticPower"));
            c.upgrade();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }


}
