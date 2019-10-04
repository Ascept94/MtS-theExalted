package Bromod.relics;

import Bromod.characters.TheExalted;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import static Bromod.BroMod.makeRelicOutlinePath;
import static Bromod.BroMod.makeRelicPath;

public class Ordis extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = BroMod.makeID("Ordis");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Ordis.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Ordis.png"));

    public Ordis() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.

/*
    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (drawnCard.type == AbstractCard.CardType.POWER && !TheExalted.hasAscaris()){
            flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player,1));
        }
    }
*/

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player,1));
    }

    @Override
    public void onShuffle() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player,1));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[1];
    }

}
