package scripts.gengarnmz.framework;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
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
        return Vars.outOfScales || Vars.outOfDarts;
    }

    private static boolean isBlowpipeConfigured()
    {
        return !(isBlowpipeEmpty() && shouldChargeBlowpipe());
    }

    private static boolean isPrayerPotionsInInventory()
    {
        return Inventory.find(Constants.ID_PRAYER_POTIONS).length >= 20;
    }

    private static boolean isCombatPotionsInInventory()
    {
        return Inventory.find(Vars.combatPotionIds).length >= 6;
    }

    private static boolean isUsingBlowpipe()
    {
        return Vars.weaponIds == Constants.BLOWPIPE_IDS;
    }

    public static boolean isDreaming()
    {
        return  Player.getPosition().getPlane() == 3;
    }

    public static boolean isUsingNmzPots()
    {
        return  Vars.combatPotionIds == Constants.ID_OVERLOADS          ||
                Vars.combatPotionIds == Constants.ID_SUPER_RANGING_POTIONS;
    }

    public static boolean shouldChargeBlowpipe()
    {
        return  (Inventory.find(Constants.MITHRIL_DARTS_ID).length > 0 && !Vars.isFullDarts) ||
                (Inventory.find(Constants.ZULRAH_SCALES_ID).length > 0 && !Vars.isFullScales);
    }

    public static boolean shouldDepositCoffer()
    {
        return  Vars.isCofferEmpty   &&
                Inventory.find(Constants.COINS_ID).length > 0;
    }

    public static boolean shouldBank()
    {
        if (isUsingBlowpipe())
        {
            return  isInBank()              &&
                    !shouldChargeBlowpipe() &&
                    (!isPrayerPotionsInInventory() || (!isUsingNmzPots() && !isCombatPotionsInInventory()) || isBlowpipeEmpty() || Vars.isCofferEmpty);
        }

        return  isInBank()                  &&
                (!isPrayerPotionsInInventory() || (!isUsingNmzPots() && !isCombatPotionsInInventory()) || Vars.isCofferEmpty);
    }

    public static boolean shouldGetNmzPots()
    {
        // Don't need to get NMZ pots
        if (!isUsingNmzPots())
            return false;

        return  !isDreaming()                   &&
                isInNmzArea()                   &&
                isPrayerPotionsInInventory()    &&
                !isCombatPotionsInInventory()   &&
                (!Vars.isRanging || isBlowpipeConfigured());
    }

    private static boolean shouldStartDream()
    {
        return  !isDreaming()                   &&
                isInNmzArea()                   &&
                isPrayerPotionsInInventory()    &&
                isCombatPotionsInInventory()    &&
                !Vars.isCofferEmpty             &&
                (!isUsingBlowpipe() || isBlowpipeConfigured());
    }

    public static boolean shouldStartDreamDominic()
    {
        RSObject potion = Entities.find(ObjectEntity::new)
                            .actionsEquals("Investigate")
                            .getFirstResult();

        return shouldStartDream() && potion != null;
    }

    public static boolean shouldStartDreamPotion()
    {
        RSObject potion = Entities.find(ObjectEntity::new)
                            .actionsEquals("Investigate")
                            .getFirstResult();

        return shouldStartDream() && potion == null;
    }

    public static boolean shouldWalkNmz()
    {
        return  !isDreaming()                                       &&
                !isInNmzArea()                                      &&
                isPrayerPotionsInInventory()                        &&
                (isUsingNmzPots()  || isCombatPotionsInInventory()) &&
                (!isUsingBlowpipe() || !shouldChargeBlowpipe());
    }

    public static boolean shouldWalkBank()
    {
        return  !isDreaming()                                   &&
                !isInBank()                                     &&
                (!isUsingBlowpipe() ||!shouldChargeBlowpipe())  &&
                (!isPrayerPotionsInInventory() || (isUsingBlowpipe() && isBlowpipeEmpty()) || (!isUsingNmzPots() && !isCombatPotionsInInventory()) || Vars.isCofferEmpty);
    }
}
