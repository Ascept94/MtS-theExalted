package Bromod.cards;



import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;


import static Bromod.DefaultMod.makeCardPath;

public class Vitality extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Vitality.class.getSimpleName());
    public static final String IMG = makeCardPath("Vitality.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExalted.Enums.MOD_COLOR_COMMON;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private int AMOUNT = 6;
    private int UPGRADE_AMOUNT = 4;
    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    // /STAT DECLARATION/


    public Vitality() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int HP = p.maxHealth * this.magicNumber / 100;
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p,p, HP));

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMOUNT);
            initializeDescription();
        }
    }


}
