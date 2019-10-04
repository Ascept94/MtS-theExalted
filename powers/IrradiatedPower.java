package Bromod.powers;

import Bromod.BroMod;
import Bromod.characters.TheExalted;
import Bromod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class IrradiatedPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("IrradiatedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/RadiationPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/RadiationPower32.png");

    public IrradiatedPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;

        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = true;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }



    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL){return;}
        DamageInfo dmgInfo = new DamageInfo(AbstractDungeon.player, ((AbstractMonster)owner).getIntentDmg(), DamageInfo.DamageType.THORNS);
        AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(dmgInfo, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        if (TheExalted.hasAscaris()){
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(owner,owner,this.ID, 1));
        }
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner,owner,this.ID, 1));
    }

    @Override
    public void updateDescription() {
        if (((AbstractMonster)owner).intent == AbstractMonster.Intent.ATTACK || ((AbstractMonster)owner).intent == AbstractMonster.Intent.ATTACK_DEFEND || ((AbstractMonster)owner).intent == AbstractMonster.Intent.ATTACK_DEBUFF || ((AbstractMonster)owner).intent == AbstractMonster.Intent.ATTACK_BUFF){
            description = DESCRIPTIONS[0] + ((AbstractMonster)owner).getIntentDmg() + DESCRIPTIONS[1];
        }
        else{
            description = DESCRIPTIONS[2];
        }
        description = TheExalted.hasAscaris()? description + DESCRIPTIONS[3] + DESCRIPTIONS[4] : description + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new IrradiatedPower(owner, source, amount);
    }
}
