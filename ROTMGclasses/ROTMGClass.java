package ROTMGclasses;

/**
 * Abstract Data-Type for Realm Of The Mad God (ROTMG) Classes
 *
 * A ROTMG class has 8 'stats',
 * and each class has a range for how much the stat increases per level.
 *
 * For example, a Wizard has Mana that can deviate from 5 to 15,
 * while a non-spellcaster such as an Archer has a range of 2 to 8 for the same stat
 *
 * The 8 stats are:
 * Health, Mana
 * Attack, Defense
 * Speed, Dex
 * Vit, Wis
 *
 * A ROTMG class also has 8 base values for these stats
 * these are offsets to keep in mind when computing the minimum roll value for a level
 * (baseStat + (level * ROTMGStat.getMin());
 */
public abstract class ROTMGClass {

    ROTMGCharacter type;

    private ROTMGStatRange health;

    ROTMGStatRange mana;
    ROTMGStatRange attack;
    ROTMGStatRange defense;
    ROTMGStatRange speed;
    ROTMGStatRange dex;
    ROTMGStatRange vit;
    ROTMGStatRange wis;

    int baseHP;
    int baseMP;
    int baseAtk;
    int baseDef;
    int baseSpd;
    int baseDex;
    int baseVit;
    int baseWis;

    ROTMGClass() {
        this.health = new ROTMGStatRange(20, 30); //health is the same for any class
    }

    public ROTMGStatRange getStatRange(ROTMGStat stat){
        switch (stat){
            case HP: return health;
            case MP: return mana;
            case ATK: return attack;
            case DEF: return defense;
            case SPD: return speed;
            case DEX: return dex;
            case VIT: return vit;
            case WIS: return wis;
            default: throw new UnsupportedOperationException("Unrecognized stat: " + stat.toString());
        }
    }

    public int getBaseStat(ROTMGStat stat){
        switch(stat) {
            case HP: return baseHP;
            case MP: return baseMP;
            case ATK: return baseAtk;
            case DEF: return baseDef;
            case SPD: return baseSpd;
            case DEX: return baseDex;
            case VIT: return baseVit;
            case WIS: return baseWis;
            default: throw new UnsupportedOperationException("Unrecognized stat: " + stat.toString());
        }
    }

    public ROTMGCharacter getType() {
        return type;
    }

    /**
     * Creates a ROTMGClass based on the corresponding character enum you pass to it.
     * E.g. ROTMGCharacter.WARRIOR returns ROTMGWarrior
     * @param character the character type to create
     * @return the character object representing the param
     */
    public static ROTMGClass create(ROTMGCharacter character) {
        switch (character) {
            case ARCHER: return new ROTMGArcher();
            case ASSASSIN: return new ROTMGAssassin();
            case HUNTRESS: return new ROTMGHuntress();
            case KNIGHT: return new ROTMGKnight();
            case NINJA: return new ROTMGNinja();
            case MYSTIC: return new ROTMGMystic();
            case NECROMANCER: return new ROTMGNecromancer();
            case PALADIN: return new ROTMGPaladin();
            case PRIEST: return new ROTMGPriest();
            case ROGUE: return new ROTMGRogue();
            case SAMURAI: return new ROTMGSamurai();
            case SORCERER: return new ROTMGSorcerer();
            case TRICKSTER: return new ROTMGTrickster();
            case WARRIOR: return new ROTMGWarrior();
            case WIZARD: return new ROTMGWizard();
            default: throw new UnsupportedOperationException("ROTMGClass " + character + " is not recognized!");
        }
    }
}


