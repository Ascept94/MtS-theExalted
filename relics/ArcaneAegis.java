package Bromod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import static Bromod.BroMod.makeRelicOutlinePath;
import static Bromod.BroMod.makeRelicPath;

public class ArcaneAegis extends CustomRelic {

    // ID, images, text.
    public static final String ID = BroMod.makeID("ArcaneAegis");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArcaneAegis.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArcaneRare.png"));

    public ArcaneAegis() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        flash();
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
