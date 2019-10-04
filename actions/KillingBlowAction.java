package Bromod.actions;

import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.Bludgeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class KillingBlowAction extends AbstractGameAction {
    private int energyGainAmt;
    private DamageInfo info;
    private AbstractCard card;

    public KillingBlowAction(AbstractCreature target, DamageInfo info, int energyAmt, AbstractCard cardToCopy) {
        this.info = info;
        this.setValues(target, info);
        this.energyGainAmt = energyAmt;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FASTER;
        this.card = cardToCopy;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FASTER && this.target != null) {
            this.target.damage(this.info);
            if (((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) {
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.energyGainAmt));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
