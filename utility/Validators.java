package scripts.gengarnmz.utility;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;

public class Validators
{
    private Validators(){}

    private static boolean isInBank()
    {
        return Constants.AREA_BANK.contains(Player.getPosition());
    }

    private static boolean isInNmzArea()
    {
        return Constants.AREA_NMZ.contains(Player.getPosition());
    }

    private static boolean isBlowpipeEmpty()
    {
        return  Vars.outOfScales || Vars.outOfDarts;
    }

    private static boolean isBlowpipeNotConfigured()
    {
        return !(isBlowpipeEmpty() && shouldChargeBlowpipe());
    }

    private static boolean isPrayerPotionsInInventory()
    {
        return Inventory.find(Constants.ID_PRAYER_POTIONS).length >= 20;
    }

    private static boolean isRangingPotionsInInventory()
    {
        return Inventory.find(Constants.ID_SUPER_RANGING_POTION_4).length >= 6;
    }

    //__________________________________________________________________________________________________________________

    public static boolean isDreaming()
    {
        return  Player.getPosition().getPlane() == 3;
    }

    public static boolean shouldChargeBlowpipe()
    {
        return  (Inventory.find(Constants.MITHRIL_DARTS_ID).length > 0 && !Vars.isFullDarts) ||
                (Inventory.find(Constants.ZULRAH_SCALES_ID).length > 0 && !Vars.isFullScales);
    }

    public static boolean shouldDepositCoffer()
    {
        return  Vars.isCofferEmpty              &&
                Inventory.find(Constants.COINS_ID).length > 0;
    }

    public static boolean shouldBank()
    {
        return  isInBank()                      &&
                !shouldChargeBlowpipe()         &&
                (!isPrayerPotionsInInventory() || isBlowpipeEmpty());
    }

    public static boolean shouldGetNmzPots()
    {
        return  !isDreaming()                   &&
                isInNmzArea()                   &&
                isPrayerPotionsInInventory()    &&
                !isRangingPotionsInInventory()  &&
                isBlowpipeNotConfigured();
    }

    public static boolean shouldStartDream()
    {
        return  !isDreaming()                   &&
                isInNmzArea()                   &&
                isPrayerPotionsInInventory()    &&
                isRangingPotionsInInventory()   &&
                isBlowpipeNotConfigured();
    }

    public static boolean shouldWalkNmz()
    {
        return  !isDreaming()                   &&
                !isInNmzArea()                  &&
                isPrayerPotionsInInventory()    &&
                !shouldChargeBlowpipe();
    }

    public static boolean shouldWalkBank()
    {
        return  !isDreaming()                   &&
                !isInBank()                     &&
                !shouldChargeBlowpipe()         &&
                (!isPrayerPotionsInInventory() || isBlowpipeEmpty() || Vars.isCofferEmpty);
    }


}
