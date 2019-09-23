package Bromod.cards;

import Bromod.powers.ToxinPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static Bromod.DefaultMod.makeCardPath;

public class ToxicBarrage extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ToxicBarrage.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ToxicBarrage.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheExalted.Enums.MOD_COLOR_RARE;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 0;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 3;
    private static final int UPGRADE_PLUS_AMOUNT = 0;

    private static final int SECOND_AMOUNT = 0;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    // /STAT DECLARATION/


    public ToxicBarrage() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(m, this.multiDamage, DamageInfo.DamageType.NORMAL,AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        for (AbstractCreature mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo,p, new PoisonPower(mo,p,this.damage), this.damage));
        }
        if (upgraded){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new ToxinPower(p,p,magicNumber,false),magicNumber));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            upgradeBaseCost(UPGRADED_COST);
            upgradeDefaultSecondMagicNumber(UPGRADE_SECOND_AMOUNT);
            this.rawDescription = this.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
