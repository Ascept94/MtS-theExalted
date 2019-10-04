package Bromod.relics;

import Bromod.characters.TheExalted;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Bromod.BroMod;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.Comparator;

import static Bromod.BroMod.makeRelicOutlinePath;
import static Bromod.BroMod.makeRelicPath;

public class AscarisDevice extends CustomRelic implements ClickableRelic {

    // ID, images, text.
    public static final String ID = BroMod.makeID("AscarisDevice");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("AscarisDevice.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("AscarisDevice.png"));

    private static float offsetX;
    public static final Color PASSIVE_OUTLINE_COLOR;
    private float rotation = 0;

    public AscarisDevice() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        if (this.usedUp){
            return DESCRIPTIONS[1];
        }
        return DESCRIPTIONS[0];
    }


    @Override
    public void onRightClick() {
        this.usedUp();
    }

    @Override
    public void usedUp() {
        this.usedUp = true;
        this.description = DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        super.renderInTopPanel(sb);
        if (!Settings.hideRelics && this.usedUp) {
            this.renderOutline(sb, true);
            sb.setColor(PASSIVE_OUTLINE_COLOR);
            sb.draw(this.img, this.currentX - 64.0F + offsetX, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, this.rotation, 0, 0, 128, 128, false, false);
            this.renderCounter(sb, true);
            this.renderFlash(sb, true);
            this.hb.render(sb);
        }
    }

    static {
        offsetX = 0.0F;
        PASSIVE_OUTLINE_COLOR = new Color(0.0F, 0.0F, 0.0F, 0.33F);
    }
}
