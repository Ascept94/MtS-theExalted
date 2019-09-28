package Bromod.cards;



import Bromod.BroMod;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.characters.TheExalted;


import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class FastHands extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(FastHands.class.getSimpleName());
    public static final String IMG = makeCardPath("FastHands.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 0;

    // /STAT DECLARATION/


    public FastHands() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);

    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean israndom = !upgraded;
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p,p,1,israndom));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p,2));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }


}
