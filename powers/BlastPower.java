package Bromod.powers;

import Bromod.BroMod;
import Bromod.characters.TheExalted;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.util.TextureLoader;

public class BlastPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("BlastPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/BlastPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/BlastPower32.png");

    private static boolean Happened = false;

    public BlastPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL || Happened){return;}
        int CHANCE = amount*10;
        int monstercount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        int[] multidamage = new int[monstercount];
        int damage = 0;
        int i;
        while(CHANCE >= 100){
            damage += 5;
            CHANCE -= 100;
        }
        if(AbstractDungeon.miscRng.random(99) <= (CHANCE-1)){
            damage += 5;
        }
        for (i=0; i < monstercount; i++){
            multidamage[i] = damage;
        }
        if (multidamage[0] == 0){return;}
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(info.owner,multidamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
        if (info.owner.hasPower("Bromod:ThunderboltPower") && AbstractDungeon.miscRng.random(99) <= 10){
            DamageInfo dmgInfo = new DamageInfo(info.owner,damage, DamageInfo.DamageType.THORNS);
            AbstractDungeon.actionManager.addToBottom(new DamageAction(info.owner,dmgInfo, AbstractGameAction.AttackEffect.FIRE));
        }
        Happened = true;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        Happened = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount*10) + DESCRIPTIONS[1];
        description = TheExalted.hasAscaris() ? description + DESCRIPTIONS[2] : description;
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlastPower(owner, source, amount);
    }
}
