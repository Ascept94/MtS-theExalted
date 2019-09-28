package Bromod.cards;



import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;


import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class RadialJavelin extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(RadialJavelin.class.getSimpleName());
    public static final String IMG = makeCardPath("RadialJavelin.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 0;
    private static final int AMOUNT = 0;
    private static final int UPGRADE_AMOUNT = 2;
    private static final int SECOND_AMOUNT = 1;
    private static final int UPGRADE_SECOND_AMOUNT = 1;

    // /STAT DECLARATION/


    public RadialJavelin() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = AMOUNT;
        baseDamage = DAMAGE;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new DaggerSprayEffect(false)));
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p,this.multiDamage,damageType.NORMAL, AbstractGameAction.AttackEffect.NONE,true));
        if (upgraded){
            for (AbstractCreature mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (mo.currentHealth > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
                }
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_AMOUNT);
            upgradeDefaultSecondMagicNumber(UPGRADE_SECOND_AMOUNT);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }


}
