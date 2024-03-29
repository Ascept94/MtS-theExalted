package Bromod.cards;

import Bromod.BroMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class ConditionOverload extends AbstractDynamicCard {

    public static final String ID = BroMod.makeID(ConditionOverload.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ConditionOverload.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 10;
    private static final int UPGRADE_PLUS_AMOUNT = 4;

    private static final int SECOND_AMOUNT = 0;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    // /STAT DECLARATION/


    public ConditionOverload() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
        this.exhaust = true;
        this.rawDescription = TheExalted.hasAscaris()? this.EXTENDED_DESCRIPTION[0] : this.DESCRIPTION;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int i;
        int dmg = TheExalted.hasAscaris() ? this.magicNumber:this.damage;
        for (i=0; i<m.powers.size();i++)
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p,dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));
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
            //this.rawDescription = this.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
    @Override
    public void update() {
        super.update();
        this.rawDescription = TheExalted.hasAscaris()? this.EXTENDED_DESCRIPTION[0] : this.DESCRIPTION;
        initializeDescription();
    }
}
