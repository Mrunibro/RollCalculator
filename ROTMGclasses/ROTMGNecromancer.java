package ROTMGclasses;
/**
 * Data taken from https://www.realmeye.com/wiki/necromancer
 */
public class ROTMGNecromancer extends ROTMGClass {
    public ROTMGNecromancer(){
        super();
        mana = new ROTMGStatRange(5, 15);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(0, 2);
        dex = new ROTMGStatRange(1, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(1, 2);

        type = ROTMGCharacter.NECROMANCER;

        baseHP = 100;
        baseMP = 100;
        baseAtk = 12;
        baseDef = 0;
        baseSpd = 10;
        baseDex = 15;
        baseVit = 10;
        baseWis = 12;
    }
}