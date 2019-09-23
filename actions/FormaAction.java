//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Bromod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import java.util.Iterator;

public class FormaAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean endTurn;
    public static int numDiscarded;
    private static final float DURATION;

    public FormaAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom) {
        this(target, source, amount, isRandom, false);
    }

    public FormaAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean endTurn) {
        this.p = (AbstractPlayer)target;
        this.isRandom = isRandom;
        this.setValues(target, source, amount);
        this.actionType = ActionType.DISCARD;
        this.endTurn = endTurn;
        this.duration = DURATION;
    }
    @Override
    public void update() {
        AbstractCard c;
        if (this.duration == DURATION) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            int i;
            if (this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                int h = this.p.hand.size();

                for(i = 0; i < h; ++i) {
                    c = this.p.hand.getTopCard();
                    this.p.hand.moveToDiscardPile(c);
                    if (!this.endTurn) {
                        c.triggerOnManualDiscard();
                        c.modifyCostForCombat(-99);
                    }

                    GameActionManager.incrementDiscard(this.endTurn);
                }

                AbstractDungeon.player.hand.applyPowers();
                this.tickDuration();
                return;
            }

            if (!this.isRandom) {
                if (this.amount < 0) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
                    AbstractDungeon.player.hand.applyPowers();
                    this.tickDuration();
                    return;
                }

                numDiscarded = this.amount;
                if (this.p.hand.size() > this.amount) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false);
                }

                AbstractDungeon.player.hand.applyPowers();
                this.tickDuration();
                return;
            }

            for(i = 0; i < this.amount; ++i) {
                c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                this.p.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                c.modifyCostForCombat(-99);
                GameActionManager.incrementDiscard(this.endTurn);
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator var4 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while(var4.hasNext()) {
                c = (AbstractCard)var4.next();
                this.p.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                c.modifyCostForCombat(-99);
                GameActionManager.incrementDiscard(this.endTurn);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
        TEXT = uiStrings.TEXT;
        DURATION = Settings.ACTION_DUR_XFAST;
    }
}
