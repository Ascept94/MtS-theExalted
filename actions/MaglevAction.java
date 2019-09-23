package Bromod.actions;

import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class MaglevAction extends AbstractGameAction {
    public MaglevAction() {
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.isEmpty()) {
            this.isDone = true;
        } else {
            AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
            if (card.hasTag(MyTags.BULLET_JUMP)) {
                AbstractDungeon.actionManager.addToBottom(new PlayTopCardAction(AbstractDungeon.getRandomMonster(), false));
            }
            else{
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
            }

            this.isDone = true;
        }
    }
}
