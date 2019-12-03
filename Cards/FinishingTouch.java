package Bromod.cards;

import Bromod.BroMod;
import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;

import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class FinishingTouch extends AbstractComboCard {

    public static final String ID = BroMod.makeID(FinishingTouch.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("FinishingTouch.png");
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

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 0;
    private static final int UPGRADE_PLUS_AMOUNT = 0;

    private static final int SECOND_AMOUNT = 0;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    private int COMBO= 0;
    private int diff;

    // /STAT DECLARATION/


    public FinishingTouch() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
        this.tags.add(MyTags.COMBO);
        this.ComboCounter = this.COMBO;
        this.exhaust = true;
        this.ComboDamage = this.damage;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if(c.tags.contains(MyTags.COMBO)){
            this.COMBO = this.COMBO + 1;
            this.modifyCostForTurn(-1);
            this.ComboCounter = this.COMBO;
        }
        this.diff = this.damage - this.baseDamage;
        this.ComboDamage = this.baseDamage*(int)Math.pow(2,this.ComboCounter) + this.diff;
        if (TheExalted.hasAscaris()){
            this.ComboDamage = (int)(this.baseDamage*Math.pow(1.5,this.ComboCounter)) + diff;
        }
        this.rawDescription = this.DESCRIPTION; //+ this.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.diff = this.damage - this.baseDamage;
        this.ComboDamage = this.baseDamage*(int)Math.pow(2,this.ComboCounter) + diff;
        if (TheExalted.hasAscaris()){
            this.ComboDamage = (int)(this.baseDamage*Math.pow(1.5,this.ComboCounter)) + diff;
        }
        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.NONE;

        switch (this.ComboCounter){
            case 0: effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT; break;
            case 1: effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT; break;
            case 2: effect = AbstractGameAction.AttackEffect.BLUNT_HEAVY; break;
            case 3: effect = AbstractGameAction.AttackEffect.SMASH; break;
            default: AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(m.drawX, m.drawY),0.4f)); break;
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.ComboDamage, damageTypeForTurn),effect));
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
