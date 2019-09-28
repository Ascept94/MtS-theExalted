package Bromod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class ElectricityPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("ElectricityPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BromodResources/images/powers/ElectricityPower84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BromodResources/images/powers/ElectricityPower32.png");

    public ElectricityPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final boolean oneturn) {
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
        DamageInfo dmgInfo = new DamageInfo(AbstractDungeon.player, 3, DamageInfo.DamageType.THORNS);
        while(CHANCE >= 100){
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(target.drawX, target.drawY), 0.2f));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, dmgInfo, true));
            CHANCE -= 100;
            AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        }
        if(AbstractDungeon.miscRng.random(99) <= (CHANCE-1)){
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(target.drawX, target.drawY), 0.2f));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, dmgInfo, true));
            AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        }
    }

    @Override
    public void onInitialApplication() {
        AbstractPlayer p = AbstractDungeon.player;
        int HeatAmount = p.hasPower("Bromod:HeatPower") ? p.getPower("Bromod:HeatPower").amount : 0;
        int ColdAmount = p.hasPower("Bromod:ColdPower") ? p.getPower("Bromod:ColdPower").amount : 0;
        int ToxinAmount = p.hasPower("Bromod:ToxinPower") ? p.getPower("Bromod:ToxinPower").amount : 0;

        if(p.hasPower("Bromod:RadiationPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new RadiationPower(p,p,(this.amount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:MagneticPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new MagneticPower(p,p,(this.amount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:CorrosivePower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new CorrosivePower(p,p,(this.amount))));;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:HeatPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new RadiationPower(p,p,(this.amount + HeatAmount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:HeatPower"));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:ColdPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new MagneticPower(p,p,(this.amount + ColdAmount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:ColdPower"));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,this.ID));
        }

        else if(p.hasPower("Bromod:ToxinPower")){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new CorrosivePower(p,p,(this.amount + ToxinAmount))));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,"Bromod:ToxinPower"));
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
        return new ElectricityPower(owner, source, amount,false);
    }
}
