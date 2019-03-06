package ROTMGclasses;
/**
 * Data taken from https://www.realmeye.com/wiki/sorcerer
 */
public class ROTMGSorcerer extends ROTMGClass {
    public ROTMGSorcerer(){
        super();
        mana = new ROTMGStatRange(5, 15);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(1, 2);
        dex = new ROTMGStatRange(0, 2);
        vit = new ROTMGStatRange(1, 2);
        wis = new ROTMGStatRange(1, 2);

        type = ROTMGCharacter.SORCERER;

        baseHP = 100;
        baseMP = 100;
        baseAtk = 12;
        baseDef = 0;
        baseSpd = 12;
        baseDex = 12;
        baseVit = 10;
        baseWis = 15;
    }
}