package Bromod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Bromod.BroMod;
import Bromod.util.TextureLoader;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Bromod.BroMod.makePowerPath;

public class ProvokedPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BroMod.makeID("ProvokedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ProvokedPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ProvokedPower32.png"));
    // The Name requires to be of the form: NamePower

    private static int VulnerableAmount;

    public ProvokedPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (AbstractDungeon.player.hasPower("Vulnerable")){
            VulnerableAmount = AbstractDungeon.player.getPower("Vulnerable").amount;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner,source,new StrengthPower(owner,VulnerableAmount),VulnerableAmount));
        }
        else{
            VulnerableAmount = 0;
        }
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.player.hasPower("Vulnerable")) {
            int difference = AbstractDungeon.player.getPower("Vulnerable").amount - VulnerableAmount -1;
            if (difference > 0){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner,source,new StrengthPower(owner,difference),difference));
            }
            VulnerableAmount = AbstractDungeon.player.getPower("Vulnerable").amount - 1;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer){
            VulnerableAmount --;
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ProvokedPower(owner, source, amount);
    }
}
