package Bromod.powers;

import Bromod.BroMod;
import Bromod.cards.AbstractComboCard;
import Bromod.util.MyTags;
import Bromod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class ExaltedPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("ExaltedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/ExaltedPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/ExaltedPower32.png");

    public ExaltedPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;

        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && card.damage > 0){
            int monstercount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
            int[] multidamage = new int[monstercount];
            int i;
            for (i=0; i < monstercount; i++){
                if(card.hasTag(MyTags.COMBO)){
                    AbstractComboCard c = (AbstractComboCard) card;
                    multidamage[i] = c.ComboDamage / 2;
                }
                else{
                    multidamage[i] = card.damage / 2;
                }
            }
            AbstractDungeon.actionManager.addToBottom(new VFXAction( new CleaveEffect()));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, multidamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
        }
    }

    /*@Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL){return;}
        int monstercount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        int[] multidamage = new int[monstercount];
        int i;
        for (i=0; i < monstercount; i++){
            multidamage[i] = info.output / 2;
        }

        AbstractDungeon.actionManager.addToBottom(new VFXAction( new CleaveEffect()));
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(info.owner, multidamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
    }
    */

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(1));
    }


    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExaltedPower(owner, source, amount);
    }
}
