package Bromod.cards;

import Bromod.actions.KillingBlowAction;
import Bromod.util.MyTags;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class KillingBlow extends AbstractComboCard {

    public static final String ID = BroMod.makeID(KillingBlow.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("KillingBlow.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final int COST = 4;
    private static final int UPGRADED_COST = 4;

    private static final int DAMAGE = 9;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 0;
    private static final int UPGRADE_PLUS_AMOUNT = 0;

    private static final int SECOND_AMOUNT = 0;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    private int COMBO = 0;
    private int diff;
    // /STAT DECLARATION/


    public KillingBlow() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
        this.tags.add(MyTags.COMBO);
        this.ComboCounter = this.COMBO;
        this.exhaust = true;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if(c.tags.contains(MyTags.COMBO) && this.COMBO < 2){
            this.COMBO = this.COMBO + 1;
            this.modifyCostForTurn(-1);
            this.ComboCounter = this.COMBO;
        }
        this.diff = this.damage - this.baseDamage;
        this.ComboDamage = this.baseDamage*(int)Math.pow(2,this.ComboCounter) + diff;
        this.rawDescription = this.DESCRIPTION; //+ this.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        this.COMBO = 0;
        this.ComboCounter = this.COMBO;
        this.rawDescription = this.DESCRIPTION;
        this.initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.diff = this.damage - this.baseDamage;
        if (diff > 0){
            this.ComboDamage = this.baseDamage*(int)Math.pow(2,this.ComboCounter) + diff;
        }
        else{
            this.ComboDamage = this.damage*(int)Math.pow(2,this.ComboCounter);
        }
        AbstractDungeon.actionManager.addToBottom(new KillingBlowAction(m, new DamageInfo(p, this.ComboDamage, damageTypeForTurn),this.costForTurn,this));
        this.COMBO = 0;
        this.ComboCounter = this.COMBO;
        this.rawDescription = this.DESCRIPTION;
        this.initializeDescription();
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
}
