package Bromod.cards;



import Bromod.BroMod;
import Bromod.powers.FlyPower;
import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Bromod.characters.TheExalted;


import static Bromod.BroMod.makeCardPath;
import static Bromod.BroMod.setModBackground;

public class BulletJump extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = BroMod.makeID(BulletJump.class.getSimpleName());
    public static final String IMG = makeCardPath("BulletJump.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExalted.Enums.COLOR_BRO;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private int AMOUNT = 1;
    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    // /STAT DECLARATION/


    public BulletJump() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
        baseMagicNumber = magicNumber = AMOUNT;
        this.tags.add(MyTags.BULLET_JUMP);
    }

    // Actions the card should do

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new FlyPower(p,p,1,1),1));

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }


}
