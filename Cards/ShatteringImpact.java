package Bromod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.DefaultMod;
import Bromod.characters.TheExalted;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static Bromod.DefaultMod.makeCardPath;

public class ShatteringImpact extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(ShatteringImpact.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ShatteringImpact.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExalted.Enums.MOD_COLOR_COMMON;

    private static final int COST = 1;
    private int AMOUNT = 1;
    private int UPGRADE_AMOUNT = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 4;

    public ShatteringImpact() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = AMOUNT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo dmgInfo = new DamageInfo(p,this.damage, damageType.NORMAL);
        if (m.currentBlock > 0){
            AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(m,p));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, dmgInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        else{
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, dmgInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,p, new VulnerablePower(m,magicNumber,false), magicNumber));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMOUNT);
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
