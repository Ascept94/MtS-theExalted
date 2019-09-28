package Bromod.cards;



import Bromod.BroMod;
import Bromod.powers.BleedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.powers.WeakPower;


import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class RendingStrike extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(RendingStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("RendingStrike.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
//    private static final int UPGRADE_COST = 1;

    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int AMOUNT = 5;
    private static final int UPGRADE_AMOUNT = 3;
    private static final int SECOND_AMOUNT = 5;
    private static final int UPGRADE_SECOND_AMOUNT = 3;

    // /STAT DECLARATION/


    public RendingStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseMagicNumber = magicNumber = AMOUNT;
        baseDamage = DAMAGE;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,p, new BleedPower(m,p,magicNumber),magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,p, new WeakPower(m,defaultSecondMagicNumber,false), defaultSecondMagicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_AMOUNT);
            upgradeDefaultSecondMagicNumber(UPGRADE_SECOND_AMOUNT);
            initializeDescription();
        }
    }


}
