package Bromod.cards;

import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class OrganShatter extends AbstractComboCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(OrganShatter.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("OrganShatter.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final int COST = 1;
    private int AMOUNT = 1;
    private int UPGRADE_AMOUNT = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 3;
    private int COMBO = 0;

    public OrganShatter() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = AMOUNT;
        this.tags.add(MyTags.COMBO);
        this.ComboCounter = this.COMBO;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if(c.tags.contains(MyTags.COMBO) && this.COMBO < 2){
            this.COMBO = this.COMBO + 1;
            this.modifyCostForTurn(-1);
            this.ComboCounter = this.COMBO;
        }
        this.rawDescription = this.DESCRIPTION; //+ this.EXTENDED_DESCRIPTION[0] + this.COMBO*2 + this.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo dmgInfo = new DamageInfo(p,this.damage, damageType.NORMAL);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, dmgInfo, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        int mgn = this.magicNumber*(int)Math.pow(2,this.COMBO);
        if (TheExalted.hasAscaris()){
            mgn = this.ComboDamage = this.magicNumber*(int)Math.pow(1.5,this.ComboCounter);
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,p, new VulnerablePower(m,mgn, false), mgn));
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

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMOUNT);
            upgradeDamage(UPGRADE_PLUS_DMG);
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
