package Bromod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Bromod.BroMod;
import Bromod.util.TextureLoader;

import static Bromod.BroMod.makeRelicOutlinePath;
import static Bromod.BroMod.makeRelicPath;

public class ExtinguishedDragonKey extends CustomRelic {

    // ID, images, text.
    public static final String ID = BroMod.makeID("ExtinguishedDragonKey");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ExtinguishedDragonKey.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ExtinguishedDragonKey.png"));


    public ExtinguishedDragonKey() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }


    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        return (int)((float)damageAmount*0.5);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
