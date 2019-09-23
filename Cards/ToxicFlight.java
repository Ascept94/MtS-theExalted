package Bromod.cards;

import Bromod.powers.FlyPower;
import Bromod.powers.ToxinPower;
import Bromod.util.MyTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static Bromod.DefaultMod.makeCardPath;

public class ToxicFlight extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ToxicFlight.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("ToxicFlight.png");
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

    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    private static final int AMOUNT = 2;
    private static final int UPGRADE_PLUS_AMOUNT = 2;

    private static final int SECOND_AMOUNT = 1;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    // /STAT DECLARATION/


    public ToxicFlight() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
        this.tags.add(MyTags.BULLET_JUMP);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new FlyPower(p,p,1,1),1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new ToxinPower(p,p,magicNumber,false),magicNumber));
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
