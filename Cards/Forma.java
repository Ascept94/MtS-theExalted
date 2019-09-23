package Bromod.cards;



import Bromod.actions.FormaAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;


import static Bromod.DefaultMod.makeCardPath;

public class Forma extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Forma.class.getSimpleName());
    public static final String IMG = makeCardPath("Forma.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 0;
    private static final int UPGRADE_COST = 0;
    private static final int AMOUNT = 1;
    private static final int UPGRADE_AMOUNT = 1;

    // /STAT DECLARATION/


    public Forma() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        baseMagicNumber = magicNumber = AMOUNT;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean isRandom = !upgraded;
        AbstractDungeon.actionManager.addToBottom(new FormaAction(p,p,magicNumber,isRandom));
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
