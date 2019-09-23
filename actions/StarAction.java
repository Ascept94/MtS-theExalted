package Bromod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class StarAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private AbstractPlayer p;
    private boolean upgraded;

    public StarAction(final AbstractPlayer p, final boolean upgraded) {

        this.p = p;
        actionType = ActionType.SPECIAL;
        this.upgraded = upgraded;
        this.amount = 1;

        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (upgraded) {
            if (this.duration == Settings.ACTION_DUR_FAST) {
                if (this.p.hand.isEmpty()) {
                    this.isDone = true;
                } else if (this.p.hand.size() == 1) {
                    AbstractCard c = this.p.hand.getTopCard();
                    if (c.cost > 0) {
                        ModifyCard(c);
                    }

                    this.p.hand.moveToDeck(c, false);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    this.isDone = true;
                } else {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                    this.tickDuration();
                }
            } else {
                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    AbstractCard c;
                    for (Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator(); var1.hasNext(); this.p.hand.moveToDeck(c, false)) {
                        c = (AbstractCard) var1.next();
                        if (c.cost > 0) {
                            ModifyCard(c);
                        }
                    }

                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                }
            }
        }
        else {
            if (this.duration == Settings.ACTION_DUR_FAST) {
                if (this.p.hand.size() < this.amount) {
                    this.amount = this.p.hand.size();
                }

                int i;

                if (this.p.hand.group.size() > this.amount) {
                    int numPlaced = this.amount;
                    AbstractDungeon.handCardSelectScreen.open("put on the bottom of your draw pile", this.amount, false);
                    this.tickDuration();
                    return;
                }

                for (i = 0; i < this.p.hand.size(); ++i) {
                    this.p.hand.moveToBottomOfDeck(this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
                }
            }


            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                Iterator var3 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

                while (var3.hasNext()) {
                    AbstractCard c = (AbstractCard) var3.next();
                    ModifyCard(c);
                    this.p.hand.moveToBottomOfDeck(c);
                }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
        }

        this.tickDuration();

    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("SetupAction");
        TEXT = uiStrings.TEXT;
    }

    private void ModifyCard(AbstractCard c) {
        if (c.costForTurn > 0) {
            c.cost = 0;
            c.costForTurn = 0;
            c.isCostModified = true;
            c.superFlash(Color.GOLD.cpy());
            }
        else{
            return;
        }

    }
}
