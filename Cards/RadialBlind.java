package Bromod.cards;



import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;


import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class RadialBlind extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(RadialBlind.class.getSimpleName());
    public static final String IMG = makeCardPath("RadialBlind.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 2;
    private int AMOUNT = 2;
    private int SECOND_AMOUNT = 2;
    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    // /STAT DECLARATION/


    public RadialBlind() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
        this.isMultiDamage = true;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new BlurPower(p,1),1));
        for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false), magicNumber));
            if (upgraded){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber));
            }
        }

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = this.UPGRADE_DESCRIPTION;
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }


}
