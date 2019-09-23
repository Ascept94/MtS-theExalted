package Bromod.cards;



import Bromod.powers.ChromaticPower;
import Bromod.powers.ExaltedPower;
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

public class ExaltedBlade extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(ExaltedBlade.class.getSimpleName());
    public static final String IMG = makeCardPath("ExaltedBlade.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int UPGRADE_COST = 1;

    private static final int AMOUNT = 1;
    private static final int UPGRADE_AMOUNT = 0;

    // /STAT DECLARATION/


    public ExaltedBlade() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = AMOUNT;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard c = new ExaltedCancel();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new ExaltedPower(p,p,0),0));
        if (upgraded){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new ChromaticPower(p,p,0),0));
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
