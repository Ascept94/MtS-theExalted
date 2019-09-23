package Bromod.powers;

import Bromod.cards.Strike_Bro;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.DefaultMod;
import Bromod.cards.Default.DefaultRareAttack;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class ToxinPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("ToxinPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/ToxinPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/ToxinPower32.png");

    public ToxinPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final boolean oneturn) {
        name = NAME;

        ID = oneturn ? POWER_ID + "1" : POWER_ID;

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
        if (info.type != DamageInfo.DamageType.NORMAL){return;}
        int CHANCE = amount*10;
        while(CHANCE >= 100){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target,info.owner, new PoisonPower(target,info.owner,2),2));
            CHANCE -= 100;
        }
        if(AbstractDungeon.miscRng.random(99) <= (CHANCE-1)){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target,info.owner, new PoisonPower(target,info.owner,2),2));
        }
    }

    @Override
    public void onInitialApplication() {
        AbstractPlayer p = AbstractDungeon.player;
        int HeatAmount = p.hasPower("Bromod:HeatPower") ? p.getPower("Bromod:HeatPower").amount : 0;
        int ElectricityAmount = p.hasPower("Bromod:ElectricityPower") ? p.getPower("Bromod:ElectricityPower").amount : 0;
        int ColdAmount = p.hasPower("Bromod:ColdPower") ? p.getPower("Bromod:ColdPower").amount : 0;

        if(p.hasPower("Bromod:GasPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new GasPower(p,p,(this.amount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:CorrosivePower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new CorrosivePower(p,p,(this.amount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:ViralPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new ViralPower(p,p,(this.amount))));;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:HeatPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new GasPower(p,p,(this.amount + HeatAmount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:HeatPower"));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:ElectricityPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new CorrosivePower(p,p,(this.amount + ElectricityAmount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:ElectricityPower"));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:ColdPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new ViralPower(p,p,(this.amount + ColdAmount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:ColdPower"));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner,this.source,POWER_ID + "1"));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount*10) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ToxinPower(owner, source, amount,false);
    }
}
