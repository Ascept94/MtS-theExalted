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

public class DecayingDragonKey extends CustomRelic {

    // ID, images, text.
    public static final String ID = BroMod.makeID("DecayingDragonKey");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DecayingDragonKey.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DecayingDragonKey.png"));


    public DecayingDragonKey() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }


    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        return (int)(blockAmount* 0.5f);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
