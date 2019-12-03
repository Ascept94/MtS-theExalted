package Bromod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import javax.swing.*;


public class NarrowMindedAction extends AbstractGameAction {

    private static int step;

    public NarrowMindedAction(int step){
        this.step = step;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.source = AbstractDungeon.player;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;

        if (p.powers.size() >= 1 && this.step==1){
            for (AbstractPower po : p.powers){
                if (po.type == AbstractPower.PowerType.DEBUFF || po.amount == 0){
                    this.amount += po.amount;
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,po.ID));
                }
            }
            AbstractDungeon.actionManager.addToBottom(new NarrowMindedAction(2));
            this.tickDuration();
        }
        else if (p.powers.size() >= 1 && this.step == 2){
            for (AbstractPower po : p.powers){
                int i = MathUtils.random(p.powers.size()-1);
                if (po.ID != p.powers.get(i).ID) {
                    this.amount += po.amount;
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,po.ID));
                }
            }
            AbstractDungeon.actionManager.addToBottom(new NarrowMindedAction(3));
            this.tickDuration();
        }
        else if (p.powers.size() == 1 && this.step == 3){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, p.powers.get(0), this.amount));
            AbstractDungeon.actionManager.addToBottom(new NarrowMindedAction(0));
            this.tickDuration();
        }

        else{
            this.amount = 0;
            this.isDone = true;
        }
    }
}