package ROTMGclasses;
/**
 * Stat data taken from realmeye.com/wiki/archer
 */
class ROTMGArcher extends ROTMGClass {
    ROTMGArcher() {
        super();
        mana = new ROTMGStatRange(2, 8);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(0, 2);
        dex = new ROTMGStatRange(0, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(0, 2);

        type = ROTMGCharacter.ARCHER;

        baseHP = 130;
        baseMP = 100;
        baseAtk = 12;
        baseDef = 0;
        baseSpd = 12;
        baseDex = 12;
        baseVit = 12;
        baseWis = 10;
    }
}
