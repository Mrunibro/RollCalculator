package ROTMGclasses;

/**
 * Stat data taken from realmeye.com/wiki/priest
 */
public class ROTMGPriest extends ROTMGClass{
    public ROTMGPriest() {
        super();
        mana = new ROTMGStatRange(5, 15);
        attack = new ROTMGStatRange(0, 2);
        defense = new ROTMGStatRange(0, 0);
        speed = new ROTMGStatRange(1, 2);
        dex = new ROTMGStatRange(0, 2);
        vit = new ROTMGStatRange(0, 1);
        wis = new ROTMGStatRange(1, 2);

        type = ROTMGCharacter.PRIEST;

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
