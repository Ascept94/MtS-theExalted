package Bromod.events;

import Bromod.patches.DefaultInsertPatch;
import Bromod.potions.AmmoRestore;
import Bromod.potions.EnergyRestore;
import Bromod.potions.HealthRestore;
import Bromod.potions.ShieldRestore;
import Bromod.relics.BleedingDragonKey;
import Bromod.relics.DecayingDragonKey;
import Bromod.relics.ExtinguishedDragonKey;
import Bromod.relics.HobbledDragonKey;
import com.megacrit.cardcrawl.blights.Shield;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
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

public class DragonKeysEvent extends AbstractImageEvent {


    public static final String ID = BroMod.makeID("DragonKeysEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("DragonKeysEvent.png");
    private static final Logger logger = LogManager.getLogger(DragonKeysEvent.class.getName());

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;

    private float HEALTH_LOSS_PERCENTAGE = 0.03F; // 3%
    private float HEALTH_LOSS_PERCENTAGE_LOW_ASCENSION = 0.05F; // 5%

    private int healthdamage; //The actual number of how much Max HP we're going to lose.

    public DragonKeysEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        // The first dialogue options available
        imageEventText.setDialogOption(OPTIONS[0]); // Challenge - Gain a number of Dragon Key Relics
        imageEventText.setDialogOption(OPTIONS[1]); // Upgrade Mods - Upgrade a card
        imageEventText.setDialogOption(OPTIONS[2]); // Craft gear - Gain potions
        imageEventText.setDialogOption(OPTIONS[3]); // Leave
    }

    @Override
    protected void buttonEffect(int i) { // This is the event:
        switch (screenNum) {
            case 0: // While you are on screen number 0 (The starting screen)
                switch (i) {
                    case 0: // If you press button the first button (Button at index 0), in this case: Dragon Keys.
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]); // Update the text of the event
                        imageEventText.clearAllDialogs();
                        imageEventText.setDialogOption(OPTIONS[5], new BleedingDragonKey());
                        imageEventText.setDialogOption(OPTIONS[6], new DecayingDragonKey());
                        imageEventText.setDialogOption(OPTIONS[7], new ExtinguishedDragonKey());
                        imageEventText.setDialogOption(OPTIONS[8], new HobbledDragonKey());
                        imageEventText.setDialogOption(OPTIONS[4]); // Leave
                        screenNum = 1; // Screen set the screen number to 1. Once we exit the switch (i) statement,
                        // we'll still continue the switch (screenNum) statement. It'll find screen 1 and do it's actions
                        // (in our case, that's the final screen, but you can chain as many as you want like that)

                        break; // Onto screen 1 we go.
                    case 1: // If you press button the second button (Button at index 1), in this case: Upgrade Mods
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getUpgradableCards()).size() > 0) {
                            // If you have cards you can upgrade - upgrade a card
                            AbstractDungeon.gridSelectScreen.open(
                                    CardGroup.getGroupWithoutBottledCards(
                                            AbstractDungeon.player.masterDeck.getUpgradableCards()),
                                    1, OPTIONS[9], true, false, false, false);
                        }

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 2;

                        // Same as before. A note here is that you can also do
                        // imageEventText.clearAllDialogs();
                        // imageEventText.setDialogOption(OPTIONS[1]);
                        // imageEventText.setDialogOption(OPTIONS[4]);
                        // (etc.)
                        // And that would also just set them into slot 0, 1, 2... in order, just like what we do in the very beginning

                        break; // Onto screen 1 we go.
                    case 2: // If you press button the third button (Button at index 2), in this case: Craft Gear

                        AbstractPotion h = new HealthRestore().makeCopy();
                        AbstractPotion s = new ShieldRestore().makeCopy();
                        AbstractPotion a = new AmmoRestore().makeCopy();
                        AbstractPotion e = new EnergyRestore().makeCopy();

                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(h));
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(s));
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(a));
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(e));
                        //AbstractDungeon.getCurrRoom().rewards.remove(4);
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        AbstractDungeon.combatRewardScreen.open();

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 2;
                        break;
                    case 3: // If you press button the fourth button (Button at index 3), in this case: Shut Up Ordis
                        // Other than that, this option doesn't do anything special.
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 2;
                        break;
                }
                break;
            case 1: // Welcome to screenNum = 1;
                switch (i) {
                    case 0:
                        if (AbstractDungeon.player.hasRelic(BleedingDragonKey.ID)){
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            this.imageEventText.clearRemainingOptions();
                            screenNum = 2;
                            break;
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new BleedingDragonKey());
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        break;
                    case 1:
                        if (AbstractDungeon.player.hasRelic(DecayingDragonKey.ID)){
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            this.imageEventText.clearRemainingOptions();
                            screenNum = 2;
                            break;
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new DecayingDragonKey());
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        break;
                    case 2:
                        if (AbstractDungeon.player.hasRelic(ExtinguishedDragonKey.ID)){
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            this.imageEventText.clearRemainingOptions();
                            screenNum = 2;
                            break;
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new ExtinguishedDragonKey());
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        break;
                    case 3:
                        if (AbstractDungeon.player.hasRelic(HobbledDragonKey.ID)){
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            this.imageEventText.clearRemainingOptions();
                            screenNum = 2;
                            break;
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new HobbledDragonKey());
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        break;
                    case 4:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 2;
                        break;
                }
                break;
            case 2:
                switch (i){
                    case 0:
                        if (AbstractDungeon.eventList.isEmpty()){AbstractDungeon.eventList.addAll(BroMod.TmpEventList);}
                        if (AbstractDungeon.specialOneTimeEventList.isEmpty()){AbstractDungeon.specialOneTimeEventList.addAll(BroMod.TmpSpecialEventList);}
                        if (AbstractDungeon.shrineList.isEmpty()){AbstractDungeon.shrineList.addAll(BroMod.TmpShrineList);}

                        logger.info("And this is the event list: " + AbstractDungeon.eventList.toString());
                        logger.info("And this is the other event list: " + AbstractDungeon.specialOneTimeEventList.toString());
                        logger.info("And this is the other other event list: " + AbstractDungeon.shrineList.toString());
                        openMap();
                        break;
                }
        }
    }

    public void update() { // We need the update() when we use grid screens (such as, in this case, the screen for selecting a card to remove)
        super.update(); // Do everything the original update()
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) { // Once the grid screen isn't empty (we selected a card for removal)
            AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0); // Get the card
            c .upgrade();
            AbstractDungeon.gridSelectScreen.selectedCards.clear(); // Or you can .remove(c) instead of clear,
            // if you want to continue using the other selected cards for something
        }

    }

}
