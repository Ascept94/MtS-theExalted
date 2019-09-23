package Bromod.actions;

import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.Iterator;

public class SerrationAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private ArrayList<AbstractCard> cannotUpgrade = new ArrayList();
    private boolean upgraded = false;

    public SerrationAction(boolean serrationPlus) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgraded = serrationPlus;
    }

    public void update() {
        Iterator var1;
        AbstractCard c;
        float percent = this.upgraded ? 1.65f : 0.9f;
        if (this.duration == Settings.ACTION_DUR_FAST) {

            var1 = this.p.hand.group.iterator();

            while(var1.hasNext()) {
                c = (AbstractCard)var1.next();
                if (c.type != AbstractCard.CardType.ATTACK) {
                    this.cannotUpgrade.add(c);
                }
            }

            if (this.cannotUpgrade.size() == this.p.hand.group.size()) {
                this.isDone = true;
                return;
            }

            if (this.p.hand.group.size() - this.cannotUpgrade.size() == 1) {
                var1 = this.p.hand.group.iterator();

                while(var1.hasNext()) {
                    c = (AbstractCard)var1.next();
                    if (c.type == AbstractCard.CardType.ATTACK){
                        AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(c.uuid,(int)(c.baseDamage*percent)));
                        c.superFlash();
                        this.isDone = true;
                        return;
                    }
                }
            }

            this.p.hand.group.removeAll(this.cannotUpgrade);
            if (this.p.hand.group.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                this.tickDuration();
                return;
            }

            if (this.p.hand.group.size() == 1) {
                if (this.p.hand.getTopCard().type == AbstractCard.CardType.ATTACK){
                    AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(this.p.hand.getTopCard().uuid,(int)(this.p.hand.getTopCard().baseDamage*percent)));
                    this.p.hand.getTopCard().superFlash();
                    this.returnCards();
                    this.isDone = true;
                }
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while(var1.hasNext()) {
                c = (AbstractCard)var1.next();
                if (c.type == AbstractCard.CardType.ATTACK){
                    AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(c.uuid,(int)(c.baseDamage*percent)));
                    c.superFlash();
                    this.isDone = true;
                    this.p.hand.addToTop(c);
                }
            }

            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }

        this.tickDuration();
    }

    private void returnCards() {
        Iterator var1 = this.cannotUpgrade.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            this.p.hand.addToTop(c);
        }

        this.p.hand.refreshHandLayout();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction");
        TEXT = uiStrings.TEXT;
    }
}
