package Bromod.relics;

import Bromod.characters.TheExalted;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.relicInterfaces.OnAfterUseCardPatch;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Bromod.BroMod;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;

import java.util.Iterator;

import static Bromod.BroMod.makeRelicOutlinePath;
import static Bromod.BroMod.makeRelicPath;

public class HobbledDragonKey extends CustomRelic implements OnAfterUseCardRelic {

    // ID, images, text.
    public static final String ID = BroMod.makeID("HobbledDragonKey");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HobbledDragonKey.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HobbledDragonKey.png"));


    public HobbledDragonKey() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
        this.counter = 0;
    }


    @Override
    public void atBattleStart() {
        this.counter=0;
    }

    @Override
    public void onAfterUseCard(AbstractCard abstractCard, UseCardAction useCardAction) {
        this.counter++;
        if (this.counter >= 4) {
            this.counter = 0;
            this.flash();
            this.playLandingSFX();
            AbstractDungeon.actionManager.cardQueue.clear();
            Iterator var3 = AbstractDungeon.player.limbo.group.iterator();

            while(var3.hasNext()) {
                AbstractCard c = (AbstractCard)var3.next();
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
            }

            AbstractDungeon.player.limbo.group.clear();
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
        }
    }

    @Override
    public void onPlayerEndTurn() {
        this.counter = 0;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
