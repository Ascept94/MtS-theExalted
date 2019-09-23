package Bromod.variables;

import Bromod.cards.AbstractComboCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import Bromod.cards.AbstractDefaultCard;

import static Bromod.DefaultMod.makeID;

public class ComboMagic extends DynamicVariable {

    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.

    @Override
    public String key() {
        return makeID("ComboMagic");
        // This is what you put between "!!" in your card strings to actually display the number.
        // You can name this anything (no spaces), but please pre-phase it with your mod name as otherwise mod conflicts can occur.
        // Remember, we're using makeID so it automatically puts "Bromod:" (or, your id) before the name.
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return card.isMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card){ return card.magicNumber * (int)Math.pow(2,((AbstractComboCard) card).ComboCounter); }

    @Override
    public int baseValue(AbstractCard card){ return card.baseMagicNumber * (int)Math.pow(2,((AbstractComboCard) card).ComboCounter); }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractDefaultCard) card).upgradedMagicNumber;
    }
}