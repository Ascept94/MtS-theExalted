package Bromod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class EmperorAction extends AbstractGameAction {
    private int magicNumber;
    private AbstractPlayer p;

    public EmperorAction(final AbstractPlayer p, final int magicNumber) {

        this.p = p;
        this.magicNumber = magicNumber;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo != null && mo.intent != AbstractMonster.Intent.ATTACK && mo.intent != AbstractMonster.Intent.ATTACK_BUFF && mo.intent != AbstractMonster.Intent.ATTACK_DEBUFF && mo.intent != AbstractMonster.Intent.ATTACK_DEFEND){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p,
                        new VulnerablePower(mo, magicNumber, false), magicNumber));
            }

        }
        isDone = true;
    }
}
