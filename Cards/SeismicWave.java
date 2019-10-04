package Bromod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.BroMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import java.util.Iterator;

import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;
import static Bromod.BroMod.setModBackground;

public class SeismicWave extends AbstractDynamicCard {

    public static final String ID = BroMod.makeID(SeismicWave.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("SeismicWave.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int BLOCK = 0;
    private static final int UPGRADE_PLUS_BLOCK = 0;

    private static final int AMOUNT = 8;
    private static final int UPGRADE_PLUS_AMOUNT = 4;

    private static final int SECOND_AMOUNT = 0;
    private static final int UPGRADE_SECOND_AMOUNT = 0;

    private static int flyAmount = 0;
    // /STAT DECLARATION/


    public SeismicWave() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET); setModBackground(this);
        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_AMOUNT;
        this.isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        flyAmount = AbstractDungeon.player.hasPower("Bromod:FlyPower") ? AbstractDungeon.player.getPower("Bromod:FlyPower").amount : 0;
        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + (this.damage + magicNumber* flyAmount) + EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        flyAmount = p.hasPower("Bromod:FlyPower") ? p.getPower("Bromod:FlyPower").amount : 0;
        int monsters = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        int[] dmg = new int[monsters];
        int i;
        for (i = 0; i < monsters; i++){
            dmg[i] = this.damage + (magicNumber*flyAmount);
        }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new CleaveEffect(), 0.4f));
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, dmg,damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:FlyPower"));

        this.rawDescription = DESCRIPTION;
        initializeDescription();
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
