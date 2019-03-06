package ROTMGclasses;
/**
 * Stat data taken from realmeye.com/wiki/knight
 */
public class ROTMGKnight extends ROTMGClass {
    public ROTMGKnight(){
        super();
        mana = new ROTMGStatRange(2, 8);
        defense = new ROTMGStatRange(0, 0);
        attack = new ROTMGStatRange(1, 2);
        speed = new ROTMGStatRange(0, 2);
        dex = new ROTMGStatRange(0, 2);
        vit = new ROTMGStatRange(1, 2);
        wis = new ROTMGStatRange(0, 2);

        type = ROTMGCharacter.KNIGHT;

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
