package Bromod.cards;

import Bromod.powers.ColdPower;
import Bromod.powers.ElectricityPower;
import Bromod.powers.HeatPower;
import Bromod.powers.ToxinPower;
import Bromod.util.MyTags;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Bromod.DefaultMod.makeCardPath;

public class DriftingContact extends AbstractComboCard {

    public static final String ID = DefaultMod.makeID(DriftingContact.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("DriftingContact.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheExalted.Enums.MOD_COLOR_RARE;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 0;
    private static final int UPGRADE_PLUS_DMG = 0;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 2;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    private static final int SECOND_AMOUNT = 0;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    private int COMBO = 0;

    // /STAT DECLARATION/


    public DriftingContact() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
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
        int i = MathUtils.random(0,3);
        AbstractPower PowerToApply;
        switch (i) {
            case 0:
                PowerToApply = new ColdPower(p, p, this.magicNumber*(int)Math.pow(2,this.COMBO), false); break;
            case 1:
                PowerToApply = new HeatPower(p, p, this.magicNumber*(int)Math.pow(2,this.COMBO), false); break;
            case 2:
                PowerToApply = new ToxinPower(p, p, this.magicNumber*(int)Math.pow(2,this.COMBO), false); break;
            case 3:
                PowerToApply = new ElectricityPower(p, p, this.magicNumber*(int)Math.pow(2,this.COMBO), false); break;
            default:
                throw new IllegalStateException("Unexpected value: " + i);
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, PowerToApply , this.magicNumber*(int)Math.pow(2,this.COMBO)));
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
