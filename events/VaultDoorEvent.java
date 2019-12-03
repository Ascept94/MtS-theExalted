package Bromod.events;

import Bromod.cards.BlindRage;
import Bromod.cards.FleetingExpertise;
import Bromod.cards.NarrowMinded;
import Bromod.cards.SpoiledStrike;
import Bromod.patches.DefaultInsertPatch;
import Bromod.potions.AmmoRestore;
import Bromod.potions.EnergyRestore;
import Bromod.potions.HealthRestore;
import Bromod.potions.ShieldRestore;
import Bromod.relics.*;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.blights.Shield;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.cards.colorless.Blind;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import Bromod.BroMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Bromod.BroMod.makeEventPath;

public class VaultDoorEvent extends AbstractImageEvent {


    public static final String ID = BroMod.makeID("VaultDoorEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("VaultDoorEvent.png");
    private static final Logger logger = LogManager.getLogger(VaultDoorEvent.class.getName());

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;
    private static int choice;
    private static int cardchoice;

    private int healthRecover; //The actual number of how much Max HP we're going to recover.

    public VaultDoorEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.choice = MathUtils.random(0,3);

        switch (this.choice){
            case 0:
                if (AbstractDungeon.player.hasRelic("Bromod:DecayingDragonKey")){
                    imageEventText.setDialogOption(OPTIONS[5], new DragonsCurse()); //Receive
                }
                else{
                    imageEventText.setDialogOption(OPTIONS[0], true);
                }
                break;

            case 1:
                if (AbstractDungeon.player.hasRelic("Bromod:BleedingDragonKey")){
                    imageEventText.setDialogOption(OPTIONS[5], new DragonsCurse());
                }
                else{
                    imageEventText.setDialogOption(OPTIONS[1], true);
                }
                break;

            case 2:
                if (AbstractDungeon.player.hasRelic("Bromod:ExtinguishedDragonKey")){
                    imageEventText.setDialogOption(OPTIONS[5], new DragonsCurse());
                }
                else{
                    imageEventText.setDialogOption(OPTIONS[2], true);
                }
                break;

            case 3:
                if (AbstractDungeon.player.hasRelic("Bromod:HobbledDragonKey")){
                    imageEventText.setDialogOption(OPTIONS[5], new DragonsCurse());
                }
                else{
                    imageEventText.setDialogOption(OPTIONS[3], true);
                }
                break;
        }

        imageEventText.setDialogOption(OPTIONS[6]); // Leave
    }

    @Override
    protected void buttonEffect(int i) { // This is the event:
        switch (screenNum) {
            case 0: // While you are on screen number 0 (The starting screen)
                switch (i) {
                    case 0: // If you press button the first button (Button at index 0), in this case: Dragon Keys.
                        this.cardchoice = MathUtils.random(0, 3);
                        AbstractCard rewardCard;

                        switch (this.cardchoice) {
                            case 1:
                                rewardCard = new FleetingExpertise();
                                break;
                            case 2:
                                rewardCard = new BlindRage();
                                break;
                            case 3:
                                rewardCard = new SpoiledStrike();
                                break;
                            default:
                                rewardCard = new NarrowMinded();
                                break;
                        }

                        AbstractRelic r = new DragonsCurse();

                        if (AbstractDungeon.player.hasRelic("Bromod:DecayingDragonKey") && this.choice == 0) {
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(rewardCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            r.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic("Bromod:DecayingDragonKey")), true);
                        }
                        else if (AbstractDungeon.player.hasRelic("Bromod:BleedingDragonKey") && this.choice == 1) {
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(rewardCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            AbstractDungeon.player.increaseMaxHp(AbstractDungeon.player.getRelic("Bromod:BleedingDragonKey").counter,true);
                            r.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic("Bromod:BleedingDragonKey")), true);

                        }
                        else if (AbstractDungeon.player.hasRelic("Bromod:ExtinguishedDragonKey") && this.choice == 2) {
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(rewardCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            r.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic("Bromod:ExtinguishedDragonKey")), true);
                        }
                        else if (AbstractDungeon.player.hasRelic("Bromod:HobbledDragonKey") && this.choice == 3) {
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(rewardCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            r.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic("Bromod:HobbledDragonKey")), true);
                        }

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]); // Update the text of the event
                        imageEventText.clearAllDialogs();
                        imageEventText.setDialogOption(OPTIONS[4]); // Leave
                        screenNum = 1;
                        break; // Onto screen 1 we go.

                    case 1: // Leave

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.clearAllDialogs();
                        imageEventText.setDialogOption(OPTIONS[4]); // Leave
                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                switch (i){
                    case 0:
                        openMap();
                        break;
                }
        }
    }
}
