package Bromod.cards;



import Bromod.powers.SlashPower;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;


import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class JaggedEdge extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(JaggedEdge.class.getSimpleName());
    public static final String IMG = makeCardPath("JaggedEdge.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 0;
    private static final int UPGRADE_COST = 0;

    private static final int AMOUNT = 1;
    private static final int UPGRADE_AMOUNT = 0;

    // /STAT DECLARATION/


    public JaggedEdge() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseMagicNumber = magicNumber = AMOUNT;
        this.isInnate=false;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new SlashPower(p,p,magicNumber),magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            upgradeMagicNumber(UPGRADE_AMOUNT);
            this.isInnate=true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }


}
