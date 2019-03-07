package ROTMGclasses;

/**
 * Stat data taken from realmeye.com/wiki/warrior
 */
class ROTMGWarrior extends ROTMGClass {
    ROTMGWarrior() {
        super();
        mana = new ROTMGStatRange(2, 8);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(0, 2);
        dex = new ROTMGStatRange(0, 2);
        vit = new ROTMGStatRange(1, 2);
        wis = new ROTMGStatRange(0, 2);

        type = ROTMGCharacter.WARRIOR;

        baseHP = 200;
        baseMP = 100;
        baseAtk = 15;
        baseDef = 0;
        baseSpd = 7;
        baseDex = 10;
        baseVit = 10;
        baseWis = 10;
    }
}
