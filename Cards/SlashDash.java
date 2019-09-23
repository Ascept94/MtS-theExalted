package Bromod.cards;



import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;


import static Bromod.DefaultMod.makeCardPath;

public class SlashDash extends AbstractComboCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(SlashDash.class.getSimpleName());
    public static final String IMG = makeCardPath("SlashDashA.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private int AMOUNT = 4;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;

    private int COMBO = 0;

    private int diff;

    // /STAT DECLARATION/


    public SlashDash() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = AMOUNT;
        this.baseDamage = DAMAGE ;

        this.tags.add(MyTags.COMBO);
        this.ComboCounter = this.COMBO;
        this.ComboDamage = damage;
    }

    // Actions the card should do

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if(c.tags.contains(MyTags.COMBO) && this.baseDamage < DAMAGE*4){
            this.COMBO = this.COMBO  < 2 ? this.COMBO + 1 : 2;
            this.modifyCostForTurn(-1);
            this.ComboCounter = this.COMBO;
        }
        this.diff = this.damage - this.baseDamage;
        this.ComboDamage = this.baseDamage*(int)Math.pow(2,this.ComboCounter) + diff;
        this.rawDescription = this.DESCRIPTION; //+ this.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.diff = this.damage - this.baseDamage;
        if (diff > 0){
            this.ComboDamage = this.baseDamage*(int)Math.pow(2,this.ComboCounter) + diff;
        }
        else{
            this.ComboDamage = this.damage*(int)Math.pow(2,this.ComboCounter);
        }
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.ComboDamage, damageTypeForTurn),AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.COMBO = 0;
        this.ComboCounter = this.COMBO;
        this.rawDescription = this.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        this.COMBO = 0;
        this.ComboCounter = this.COMBO;
        this.rawDescription = this.DESCRIPTION;
        this.initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }


}
