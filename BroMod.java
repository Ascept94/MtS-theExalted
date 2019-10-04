package Bromod;

import Bromod.relics.*;
import Bromod.util.MyTags;
import Bromod.variables.ComboMagic;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Bromod.cards.*;
import Bromod.characters.TheExalted;
import Bromod.events.IdentityCrisisEvent;
import Bromod.util.IDCheckDontTouchPls;
import Bromod.util.TextureLoader;
import Bromod.variables.DefaultCustomVariable;
import Bromod.variables.DefaultSecondMagicNumber;
import Bromod.variables.ComboDamage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import static Bromod.characters.TheExalted.Enums.*;

// Please don't just mass replace "Bromod" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.

//
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "Bromod:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "Bromod". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class BroMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber/*,
        PreDungeonUpdateSubscriber,
        StartActSubscriber*/ {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(BroMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_DEBUFF_HEALTHBARS = "enableDebuffHealthBars";
    public static boolean enableDebuffHealthBars = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The Exalted";
    private static final String AUTHOR = "Ascept"; // And pretty soon - You!
    private static final String DESCRIPTION = "A mod adding a new custom character, the Exalted, based on Excalibur from Warframe.";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color COLOR_BRO = CardHelper.getColor(220.0f, 200.0f, 255.0f);

    //These are not used anymore since I made them all a single color (and then changed the background individually for each card)

    //public static final Color MOD_RARE = CardHelper.getColor(200.0f, 200.0f, 55.0f);
    //public static final Color MOD_UNCOMMON = CardHelper.getColor(230.0f, 230.0f, 255.0f);
    //public static final Color MOD_COMMON = CardHelper.getColor(200.0f, 100.0f, 55.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_BRO = "BromodResources/images/512/bg_attack_bro.png";
    private static final String SKILL_BRO = "BromodResources/images/512/bg_skill_bro.png";
    private static final String POWER_BRO = "BromodResources/images/512/bg_power_bro.png";
    private static final String CARD_ENERGY_ORB = "BromodResources/images/512/card_small_orb_bro.png";
    private static final String ENERGY_ORB_BRO = "BromodResources/images/512/card_bro_orb.png";

    //These are not used anymore since I made them all a single color (and then changed the background individually for each card)

    //private static final String ATTACK_MOD_RARE = "BromodResources/images/512/ModRare_attack.png";
    //private static final String SKILL_MOD_RARE = "BromodResources/images/512/ModRare_skill.png";
    //private static final String POWER_MOD_RARE = "BromodResources/images/512/ModRare_power.png";

    //private static final String ATTACK_MOD_UNCOMMON = "BromodResources/images/512/ModUncommon_attack.png";
    //private static final String SKILL_MOD_UNCOMMON = "BromodResources/images/512/ModUncommon_skill.png";
    //private static final String POWER_MOD_UNCOMMON = "BromodResources/images/512/ModUncommon_power.png";

    //private static final String ATTACK_MOD_COMMON = "BromodResources/images/512/ModCommon_attack.png";
    //private static final String SKILL_MOD_COMMON = "BromodResources/images/512/ModCommon_skill.png";
    //private static final String POWER_MOD_COMMON = "BromodResources/images/512/ModCommon_power.png";


    //private static final String ENERGY_ORB_MOD_RARE = "BromodResources/images/512/WF_Card_OrbRare.png";
    //private static final String ENERGY_ORB_MOD_UNCOMMON = "BromodResources/images/512/WF_Card_OrbUncommon.png";
    //private static final String ENERGY_ORB_MOD_COMMON = "BromodResources/images/512/WF_Card_OrbCommon.png";


    //// Portraits
    private static final String ENERGY_ORB_BRO_PORTRAIT = "BromodResources/images/1024/card_bro_orb.png";

    private static final String ATTACK_BRO_PORTRAIT = "BromodResources/images/1024/bg_attack_bro.png";
    private static final String SKILL_BRO_PORTRAIT = "BromodResources/images/1024/bg_skill_bro.png";
    private static final String POWER_BRO_PORTRAIT = "BromodResources/images/1024/bg_power_bro.png";

    //These are not used anymore since I made them all a single color (and then changed the background individually for each card)

    //private static final String ATTACK_MOD_RARE_PORTRAIT = "BromodResources/images/1024/ModRare_attack.png";
    //private static final String SKILL_MOD_RARE_PORTRAIT = "BromodResources/images/1024/ModRare_skill.png";
    //private static final String POWER_MOD_RARE_PORTRAIT = "BromodResources/images/1024/ModRare_power.png";
    //private static final String ENERGY_ORB_MOD_RARE_PORTRAIT = "BromodResources/images/1024/WF_Card_OrbRare.png";

    //private static final String ATTACK_MOD_UNCOMMON_PORTRAIT = "BromodResources/images/1024/ModUncommon_attack.png";
    //private static final String SKILL_MOD_UNCOMMON_PORTRAIT = "BromodResources/images/1024/ModUncommon_skill.png";
    //private static final String POWER_MOD_UNCOMMON_PORTRAIT = "BromodResources/images/1024/ModUncommon_power.png";
    //private static final String ENERGY_ORB_MOD_UNCOMMON_PORTRAIT = "BromodResources/images/1024/WF_Card_OrbUncommon.png";

    //private static final String ATTACK_MOD_COMMON_PORTRAIT = "BromodResources/images/1024/ModCommon_attack.png";
    //private static final String SKILL_MOD_COMMON_PORTRAIT = "BromodResources/images/1024/ModCommon_skill.png";
    //private static final String POWER_MOD_COMMON_PORTRAIT = "BromodResources/images/1024/ModCommon_power.png";
    //private static final String ENERGY_ORB_MOD_COMMON_PORTRAIT = "BromodResources/images/1024/WF_Card_OrbCommon.png";

    // Character assets
    private static final String THE_EXALTED_BUTTON = "BromodResources/images/charSelect/BroCharacterButton.png";
    private static final String THE_EXALTED_PORTRAIT = "BromodResources/images/charSelect/BroCharacterBG.png";
    public static final String THE_EXALTED_SHOULDER_1 = "BromodResources/images/char/Excalibro/Shoulder2.png";
    public static final String THE_EXALTED_SHOULDER_2 = "BromodResources/images/char/Excalibro/Shoulder1.png";
    public static final String THE_EXALTED_CORPSE = "BromodResources/images/char/Excalibro/Dead.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "BromodResources/images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_EXALTED_SKELETON_ATLAS = "BromodResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_EXALTED_SKELETON_JSON = "BromodResources/images/char/defaultCharacter/skeleton.json";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public BroMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */

        setModID("Bromod");
        // cool

        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.

        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of Bromod with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.

        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than Bromod. They get loaded before getID is a thing.

        logger.info("Done subscribing");


        /*BaseMod.addColor(TheDefault.Enums.COLOR_BRO, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);*/


        logger.info("Creating the color " + TheExalted.Enums.COLOR_BRO.toString());
        BaseMod.addColor(TheExalted.Enums.COLOR_BRO, COLOR_BRO, COLOR_BRO, COLOR_BRO,
                COLOR_BRO, COLOR_BRO, COLOR_BRO, COLOR_BRO,
                ATTACK_BRO, SKILL_BRO, POWER_BRO, ENERGY_ORB_BRO,
                ATTACK_BRO_PORTRAIT, SKILL_BRO_PORTRAIT, POWER_BRO_PORTRAIT,
                ENERGY_ORB_BRO_PORTRAIT, CARD_ENERGY_ORB);
/*
        logger.info("Creating the color " + TheExalted.Enums.MOD_COLOR_RARE.toString());
        BaseMod.addColor(TheExalted.Enums.MOD_COLOR_RARE, MOD_RARE, MOD_RARE, MOD_RARE,
                MOD_RARE, MOD_RARE, MOD_RARE, MOD_RARE,
                ATTACK_MOD_RARE, SKILL_MOD_RARE, POWER_MOD_RARE, ENERGY_ORB_MOD_RARE,
                ATTACK_MOD_RARE_PORTRAIT, SKILL_MOD_RARE_PORTRAIT, POWER_MOD_RARE_PORTRAIT,
                ENERGY_ORB_MOD_RARE_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Creating the color " + TheExalted.Enums.MOD_COLOR_UNCOMMON.toString());
        BaseMod.addColor(TheExalted.Enums.MOD_COLOR_UNCOMMON, MOD_UNCOMMON, MOD_UNCOMMON, MOD_UNCOMMON,
                MOD_UNCOMMON, MOD_UNCOMMON, MOD_UNCOMMON, MOD_UNCOMMON,
                ATTACK_MOD_UNCOMMON, SKILL_MOD_UNCOMMON, POWER_MOD_UNCOMMON, ENERGY_ORB_MOD_UNCOMMON,
                ATTACK_MOD_UNCOMMON_PORTRAIT, SKILL_MOD_UNCOMMON_PORTRAIT, POWER_MOD_UNCOMMON_PORTRAIT,
                ENERGY_ORB_MOD_UNCOMMON_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Creating the color " + TheExalted.Enums.MOD_COLOR_COMMON.toString());
        BaseMod.addColor(TheExalted.Enums.MOD_COLOR_COMMON, MOD_COMMON, MOD_COMMON, MOD_COMMON,
                MOD_COMMON, MOD_COMMON, MOD_COMMON, MOD_COMMON,
                ATTACK_MOD_COMMON, SKILL_MOD_COMMON, POWER_MOD_COMMON, ENERGY_ORB_MOD_COMMON,
                ATTACK_MOD_COMMON_PORTRAIT, SKILL_MOD_COMMON_PORTRAIT, POWER_MOD_COMMON_PORTRAIT,
                ENERGY_ORB_MOD_COMMON_PORTRAIT, CARD_ENERGY_ORB); */

        logger.info("Done creating the colors");

        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_DEBUFF_HEALTHBARS, "TRUE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableDebuffHealthBars = config.getBool(ENABLE_DEBUFF_HEALTHBARS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");

    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = BroMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = BroMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = BroMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    // ====== YOU CAN EDIT AGAIN ======


    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        BroMod defaultmod = new BroMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheExalted.Enums.THE_EXALTED.toString());

        BaseMod.addCharacter(new TheExalted("the Exalted", TheExalted.Enums.THE_EXALTED),
                THE_EXALTED_BUTTON, THE_EXALTED_PORTRAIT, TheExalted.Enums.THE_EXALTED);

        receiveEditPotions();
        logger.info("Added " + TheExalted.Enums.THE_EXALTED.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        AbstractCard card = CardLibrary.getCard("Bromod:Berserker");
        logger.info("BromodResources/images/512/WF_Card_Orb" + card.rarity.toString().toLowerCase() + ".png");
        logger.info("BromodResources/images/512/Mod" + card.rarity.toString().toLowerCase() + "_" + card.type.toString().toLowerCase() + ".png");

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("Enable HP damage markers. Disable this to increase compatibility with other mods that modify HP Bars.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableDebuffHealthBars, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                }, // thing??????? idk
                (button) -> { // The actual button:

                    enableDebuffHealthBars = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("BroMod", "theExaltedConfig", theDefaultDefaultSettings);
                        config.setBool(ENABLE_DEBUFF_HEALTHBARS, enableDebuffHealthBars);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);


        // =============== EVENTS =================

        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        //BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    // =============== / POST-INITIALIZE/ =================


    // ================ ADD POTIONS ===================

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_EXALTED".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        //BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheExalted.Enums.THE_EXALTED);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new Ordis(), TheExalted.Enums.COLOR_BRO);
        BaseMod.addRelicToCustomPool(new AscarisDevice(), TheExalted.Enums.COLOR_BRO);


        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(Ordis.ID);
        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        BaseMod.addDynamicVariable(new ComboDamage());
        BaseMod.addDynamicVariable(new ComboMagic());

        logger.info("Adding cards");
        // Add the cards
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        BaseMod.addCard(new Strike_Bro());
        BaseMod.addCard(new Defend_Bro());
        BaseMod.addCard(new SlashDash());
        BaseMod.addCard(new RadialBlind());
        BaseMod.addCard(new FastHands());
        BaseMod.addCard(new ChillingReload());
        BaseMod.addCard(new ContagiousSpread());
        BaseMod.addCard(new NorthWind());
        BaseMod.addCard(new HeatedCharge());
        BaseMod.addCard(new Stormbringer());
        BaseMod.addCard(new NoReturn());
        BaseMod.addCard(new JaggedEdge());
        BaseMod.addCard(new CollisionForce());
        BaseMod.addCard(new RendingStrike());
        BaseMod.addCard(new LifeStrike());
        BaseMod.addCard(new BulletJump());
        BaseMod.addCard(new FashionFrame());
        BaseMod.addCard(new ArchwingLauncher());
        BaseMod.addCard(new Forma());
        BaseMod.addCard(new ExaltedBlade());
        BaseMod.addCard(new ExaltedCancel());
        BaseMod.addCard(new RadialJavelin());
        BaseMod.addCard(new Vitality());
        BaseMod.addCard(new Redirection());
        BaseMod.addCard(new SteelFiber());
        BaseMod.addCard(new ShatteringImpact());
        BaseMod.addCard(new OrganShatter());
        BaseMod.addCard(new PressurePoint());
        BaseMod.addCard(new BodyCount());
        BaseMod.addCard(new SlipMagazine());
        BaseMod.addCard(new QuickDraw());
        BaseMod.addCard(new Reflection());
        BaseMod.addCard(new BloodRush());
        BaseMod.addCard(new BriefRespite());
        BaseMod.addCard(new FinishingTouch());
        BaseMod.addCard(new KillingBlow());
        BaseMod.addCard(new Maglev());
        BaseMod.addCard(new HornetStrike());
        BaseMod.addCard(new Fury());
        BaseMod.addCard(new VitalSense());
        BaseMod.addCard(new Patagium());
        BaseMod.addCard(new PointBlank());
        BaseMod.addCard(new PointStrike());
        BaseMod.addCard(new Provoked());
        BaseMod.addCard(new ReflexCoil());
        BaseMod.addCard(new RelentlessCombination());
        BaseMod.addCard(new Rejuvenation());
        BaseMod.addCard(new SeismicWave());
        BaseMod.addCard(new Serration());
        BaseMod.addCard(new SpeedTrigger());
        BaseMod.addCard(new SplitChamber());
        BaseMod.addCard(new TargetCracker());
        BaseMod.addCard(new Stretch());
        BaseMod.addCard(new SteelCharge());
        BaseMod.addCard(new TacticalPump());
        BaseMod.addCard(new EnergySiphon());
        BaseMod.addCard(new Berserker());
        BaseMod.addCard(new Adaptation());
        BaseMod.addCard(new Blaze());
        BaseMod.addCard(new ToxicFlight());
        BaseMod.addCard(new FocusEnergy());
        BaseMod.addCard(new ThermiteRounds());
        BaseMod.addCard(new Frostbite());
        BaseMod.addCard(new VoltaicStrike());
        BaseMod.addCard(new ToxicBarrage());
        BaseMod.addCard(new ConditionOverload());
        BaseMod.addCard(new DriftingContact());
        BaseMod.addCard(new Continuity());
        BaseMod.addCard(new HellsChamber());
        BaseMod.addCard(new Intensify());
        BaseMod.addCard(new Flow());
        BaseMod.addCard(new Thunderbolt());
        BaseMod.addCard(new LethalTorrent());
        BaseMod.addCard(new QuickThinking());
        BaseMod.addCard(new Rage());
        BaseMod.addCard(new Foundry());


        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.

        UnlockTracker.unlockCard(Defend_Bro.ID);
        UnlockTracker.unlockCard(Strike_Bro.ID);
        UnlockTracker.unlockCard(SlashDash.ID);
        UnlockTracker.unlockCard(RadialBlind.ID);
        UnlockTracker.unlockCard(FastHands.ID);
        UnlockTracker.unlockCard(ChillingReload.ID);
        UnlockTracker.unlockCard(ContagiousSpread.ID);
        UnlockTracker.unlockCard(HeatedCharge.ID);
        UnlockTracker.unlockCard(NorthWind.ID);
        UnlockTracker.unlockCard(Stormbringer.ID);
        UnlockTracker.unlockCard(CollisionForce.ID);
        UnlockTracker.unlockCard(NoReturn.ID);
        UnlockTracker.unlockCard(JaggedEdge.ID);
        UnlockTracker.unlockCard(RendingStrike.ID);
        UnlockTracker.unlockCard(LifeStrike.ID);
        UnlockTracker.unlockCard(BulletJump.ID);
        UnlockTracker.unlockCard(FashionFrame.ID);
        UnlockTracker.unlockCard(ArchwingLauncher.ID);
        UnlockTracker.unlockCard(Forma.ID);
        UnlockTracker.unlockCard(ExaltedBlade.ID);
        UnlockTracker.unlockCard(ExaltedCancel.ID);
        UnlockTracker.unlockCard(RadialJavelin.ID);
        UnlockTracker.unlockCard(Vitality.ID);
        UnlockTracker.unlockCard(Redirection.ID);
        UnlockTracker.unlockCard(SteelFiber.ID);
        UnlockTracker.unlockCard(ShatteringImpact.ID);
        UnlockTracker.unlockCard(OrganShatter.ID);
        UnlockTracker.unlockCard(PressurePoint.ID);
        UnlockTracker.unlockCard(BodyCount.ID);
        UnlockTracker.unlockCard(SlipMagazine.ID);
        UnlockTracker.unlockCard(Reflection.ID);
        UnlockTracker.unlockCard(QuickDraw.ID);
        UnlockTracker.unlockCard(BloodRush.ID);
        UnlockTracker.unlockCard(BriefRespite.ID);
        UnlockTracker.unlockCard(FinishingTouch.ID);
        UnlockTracker.unlockCard(KillingBlow.ID);
        UnlockTracker.unlockCard(Maglev.ID);
        UnlockTracker.unlockCard(Fury.ID);
        UnlockTracker.unlockCard(HornetStrike.ID);
        UnlockTracker.unlockCard(VitalSense.ID);
        UnlockTracker.unlockCard(Patagium.ID);
        UnlockTracker.unlockCard(PointBlank.ID);
        UnlockTracker.unlockCard(PointStrike.ID);
        UnlockTracker.unlockCard(Provoked.ID);
        UnlockTracker.unlockCard(ReflexCoil.ID);
        UnlockTracker.unlockCard(Rejuvenation.ID);
        UnlockTracker.unlockCard(RelentlessCombination.ID);
        UnlockTracker.unlockCard(SeismicWave.ID);
        UnlockTracker.unlockCard(Serration.ID);
        UnlockTracker.unlockCard(SpeedTrigger.ID);
        UnlockTracker.unlockCard(SplitChamber.ID);
        UnlockTracker.unlockCard(SteelCharge.ID);
        UnlockTracker.unlockCard(TargetCracker.ID);
        UnlockTracker.unlockCard(Stretch.ID);
        UnlockTracker.unlockCard(TacticalPump.ID);
        UnlockTracker.unlockCard(EnergySiphon.ID);
        UnlockTracker.unlockCard(Berserker.ID);
        UnlockTracker.unlockCard(Adaptation.ID);
        UnlockTracker.unlockCard(Blaze.ID);
        UnlockTracker.unlockCard(ToxicFlight.ID);
        UnlockTracker.unlockCard(FocusEnergy.ID);
        UnlockTracker.unlockCard(ToxicBarrage.ID);
        UnlockTracker.unlockCard(Frostbite.ID);
        UnlockTracker.unlockCard(VoltaicStrike.ID);
        UnlockTracker.unlockCard(ThermiteRounds.ID);
        UnlockTracker.unlockCard(ConditionOverload.ID);
        UnlockTracker.unlockCard(Continuity.ID);
        UnlockTracker.unlockCard(DriftingContact.ID);
        UnlockTracker.unlockCard(Intensify.ID);
        UnlockTracker.unlockCard(HellsChamber.ID);
        UnlockTracker.unlockCard(Flow.ID);
        UnlockTracker.unlockCard(Thunderbolt.ID);
        UnlockTracker.unlockCard(LethalTorrent.ID);
        UnlockTracker.unlockCard(QuickThinking.ID);
        UnlockTracker.unlockCard(Rage.ID);
        UnlockTracker.unlockCard(Foundry.ID);

        logger.info("Done adding cards!");
    }

    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.

    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/BroMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/BroMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/BroMod-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/BroMod-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/BroMod-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/BroMod-Character-Strings.json");

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/BroMod-Orb-Strings.json");

        logger.info("Done edittting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/BroMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }


    public static void setModBackground(CustomCard card) { //ModUncommon_skill.png"

        card.setBackgroundTexture("BromodResources/images/512/Mod" + card.rarity.toString().toLowerCase() + "_" + card.type.toString().toLowerCase() + ".png",
               "BromodResources/images/1024/Mod" + card.rarity.toString().toLowerCase() + "_" + card.type.toString().toLowerCase() + ".png");

        card.setOrbTexture("BromodResources/images/512/WF_Card_Orb" + card.rarity.toString().toLowerCase() + ".png",
                "BromodResources/images/1024/WF_Card_Orb" + card.rarity.toString().toLowerCase() + ".png");
    }

/*
    @Override
    public void receivePreDungeonUpdate() {
        if (AbstractDungeon.player.chosenClass == THE_EXALTED && !AbstractDungeon.rareCardPool.contains(CardLibrary.getCard("Bromod:VitalSense")) ){
            ArrayList<AbstractCard> tmpPool = new ArrayList();
            CardLibrary.addCardsIntoPool(tmpPool, MOD_COLOR_COMMON);
            CardLibrary.addCardsIntoPool(tmpPool, MOD_COLOR_UNCOMMON);
            CardLibrary.addCardsIntoPool(tmpPool, MOD_COLOR_RARE);
            Iterator var4 = tmpPool.iterator();
            AbstractCard c;
            while (var4.hasNext()) {
                c = (AbstractCard) var4.next();
                switch (c.rarity) {
                    case COMMON:
                        AbstractDungeon.commonCardPool.addToRandomSpot(c);
                        AbstractDungeon.srcCommonCardPool.addToRandomSpot(c);
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonCardPool.addToRandomSpot(c);
                        AbstractDungeon.srcUncommonCardPool.addToRandomSpot(c);
                        break;
                    case RARE:
                        AbstractDungeon.rareCardPool.addToRandomSpot(c);
                        AbstractDungeon.srcRareCardPool.addToRandomSpot(c);
                        break;
                    default:
                        logger.info("Unspecified rarity: " + c.rarity.name() + " when creating pools! AbstractDungeon: Line 827");
                }
            }
        }

    }


    @Override
    public void receiveStartAct() {
        if (AbstractDungeon.player.chosenClass == THE_EXALTED){
            ArrayList<AbstractCard> tmpPool = new ArrayList();
            CardLibrary.addCardsIntoPool(tmpPool, MOD_COLOR_COMMON);
            CardLibrary.addCardsIntoPool(tmpPool, MOD_COLOR_UNCOMMON);
            CardLibrary.addCardsIntoPool(tmpPool, MOD_COLOR_RARE);
            Iterator var4 = tmpPool.iterator();
            AbstractCard c;
            while (var4.hasNext()) {
                c = (AbstractCard) var4.next();
                switch (c.rarity) {
                    case COMMON:
                        AbstractDungeon.commonCardPool.addToRandomSpot(c);
                        AbstractDungeon.srcCommonCardPool.addToRandomSpot(c);
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonCardPool.addToRandomSpot(c);
                        AbstractDungeon.srcUncommonCardPool.addToRandomSpot(c);
                        break;
                    case RARE:
                        AbstractDungeon.rareCardPool.addToRandomSpot(c);
                        AbstractDungeon.srcRareCardPool.addToRandomSpot(c);
                        break;
                    default:
                        logger.info("Unspecified rarity: " + c.rarity.name() + " when creating pools! AbstractDungeon: Line 827");
                }
            }
        }
    }*/
}
