package Bromod.actions;

import Bromod.util.MyTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class LifeStrikeAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard card;

    public LifeStrikeAction(AbstractCreature target, DamageInfo info, AbstractCard UsedCard) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FASTER;
        this.card = UsedCard;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FASTER && this.target != null) {
            DamageInfo dmgInfo = new DamageInfo(info.owner,info.output, damageType.NORMAL);
            AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(target, dmgInfo, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            this.card.modifyCostForCombat(1);
        }

        this.tickDuration();
    }
}
