package ROTMGclasses;

/**
 * Data taken from https://www.realmeye.com/wiki/paladin
 */
class ROTMGPaladin extends ROTMGClass {
    ROTMGPaladin(){
        super();
        mana = new ROTMGStatRange(2, 8);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(0, 2);
        dex = new ROTMGStatRange(0, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(1, 2);

        type = ROTMGCharacter.PALADIN;

        baseHP = 200;
        baseMP = 100;
        baseAtk = 12;
        baseDef = 0;
        baseSpd = 12;
        baseDex = 10;
        baseVit = 10;
        baseWis = 10;
    }
}
