package Bromod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static Bromod.DefaultMod.makeCardPath;

public class QuickDraw extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(QuickDraw.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("QuickDraw.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheExalted.Enums.MOD_COLOR_COMMON;

    private static final int COST = -2;
    private static final int UPGRADED_COST = -2;

    private static final int DAMAGE = 0;
    private static final int UPGRADE_PLUS_DMG = 0;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 1;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    private static final int SECOND_AMOUNT = 0;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    // /STAT DECLARATION/


    public QuickDraw() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;

    }


    // Actions the card should do.


    @Override
    public void triggerWhenDrawn() {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
    }

    @Override
    public void triggerOnManualDiscard() {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
    }

    @Override
    public void triggerOnExhaust() {
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ExhaustAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
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
