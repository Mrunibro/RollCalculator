package ROTMGclasses;
/**
 * Data taken from https://www.realmeye.com/wiki/ninja
 */
class ROTMGNinja extends ROTMGClass {
    ROTMGNinja(){
        super();
        mana = new ROTMGStatRange(2, 8);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(0, 2);
        dex = new ROTMGStatRange(1, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(1, 2);

        type = ROTMGCharacter.NINJA;

        baseHP = 150;
        baseMP = 100;
        baseAtk = 15;
        baseDef = 0;
        baseSpd = 10;
        baseDex = 12;
        baseVit = 10;
        baseWis = 12;
    }
}