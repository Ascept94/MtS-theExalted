package Bromod.actions;

import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class LethalTorrentAction extends AbstractGameAction {
    private boolean upgraded;
    private int amount;
    public LethalTorrentAction(boolean upgraded, int amount) {
        actionType = ActionType.SPECIAL;
        this.upgraded = upgraded;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.isEmpty()) {
            this.isDone = true;
        } else {
            AbstractCard c = AbstractDungeon.player.drawPile.getTopCard();
            int i;
            for (i=0;i<this.amount;i++){
                if(this.upgraded){
                    AbstractDungeon.actionManager.addToTop(new PlayTopCardAction(AbstractDungeon.getRandomMonster(),false));
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(c,1,false,true,false));
                }
                else{
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c,1));
                }
            }
            this.isDone = true;
        }
    }
}
