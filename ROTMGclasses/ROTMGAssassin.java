package ROTMGclasses;
/**
 * Data taken from https://www.realmeye.com/wiki/assassin
 */
class ROTMGAssassin extends ROTMGClass {
    ROTMGAssassin(){
        super();
        mana = new ROTMGStatRange(2, 8);
        attack = new ROTMGStatRange(0, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(1, 2);
        dex = new ROTMGStatRange(1, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(1, 2);

        type = ROTMGCharacter.ASSASSIN;

        baseHP = 150;
        baseMP = 100;
        baseAtk = 12;
        baseDef = 0;
        baseSpd = 15;
        baseDex = 15;
        baseVit = 15;
        baseWis  = 10;
    }
}