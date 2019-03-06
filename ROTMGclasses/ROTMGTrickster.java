package ROTMGclasses;
/**
 * Data taken from https://www.realmeye.com/wiki/trickster
 */
public class ROTMGTrickster extends ROTMGClass {
    public ROTMGTrickster(){
        super();
        mana = new ROTMGStatRange(2, 8);
        attack = new ROTMGStatRange(1, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(1, 2);
        dex = new ROTMGStatRange(1, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(0, 2);

        type = ROTMGCharacter.TRICKSTER;

        baseHP = 150;
        baseMP = 100;
        baseAtk = 10;
        baseDef = 0;
        baseSpd = 12;
        baseDex = 15;
        baseVit = 12;
        baseWis = 12;
    }
}