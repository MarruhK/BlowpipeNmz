package scripts.gengarnmz.data;

import org.tribot.api2007.Skills;

public abstract class Vars
{
    private Vars(){}

    public static boolean shouldExecute = false;

    // BSS Vars

    // Blowpipe Vars
    public static boolean outOfDarts = false;
    public static boolean outOfScales = false;
    public static boolean isFullDarts = false;
    public static boolean isFullScales = false;

    // Nmz Vars
    public static boolean isCofferEmpty = false;
    public static boolean isBarrelEmpty = false;

    // GUI Vars
    public static int[] weaponIds;
    public static int[] combatPotionIds;
    public static AttackStyles attackStyles;
    public static boolean isRanging = false;

    public static Skills.SKILLS skill;
}
