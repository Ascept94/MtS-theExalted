package Bromod.cards;



import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.colorless.PanicButton;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.powers.*;


import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class Redirection extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(Redirection.class.getSimpleName());
    public static final String IMG = makeCardPath("Redirection.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;


    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private int AMOUNT = 2;
    private int UPGRADE_AMOUNT = 0;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    // /STAT DECLARATION/


    public Redirection() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        this.baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        this.rawDescription = TheExalted.hasAscaris()? this.DESCRIPTION + this.EXTENDED_DESCRIPTION[0] : this.DESCRIPTION;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int BlockNextTurn = p.currentBlock * this.magicNumber;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new NextTurnBlockPower(p, BlockNextTurn), BlockNextTurn));
        // THINK ABOUT THIS!
        if (TheExalted.hasAscaris()){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new NoBlockPower(p,2,false),1));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMOUNT);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }

    @Override
    public void update() {
        super.update();
        this.rawDescription = TheExalted.hasAscaris()? this.DESCRIPTION + this.EXTENDED_DESCRIPTION[0] : this.DESCRIPTION;
        initializeDescription();
    }
}
