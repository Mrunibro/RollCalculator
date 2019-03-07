package ROTMGclasses;
/**
 * Data taken from https://www.realmeye.com/wiki/mystic
 */
class ROTMGMystic extends ROTMGClass {
    ROTMGMystic(){
        super();
        mana = new ROTMGStatRange(5, 15);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(0, 2);
        dex = new ROTMGStatRange(0, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(1, 2);

        type = ROTMGCharacter.MYSTIC;

        baseHP = 100;
        baseMP = 100;
        baseAtk = 10;
        baseDef = 0;
        baseSpd = 12;
        baseDex = 10;
        baseVit = 15;
        baseWis = 15;
    }
}