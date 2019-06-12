package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class Bank extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Bank initiated... Executing...");

        if (openBank() && depositAll())
        {
            // Handle non-nmz potions
            if (!Validators.isUsingNmzPots())
            {
                System.out.println("Bank: checking bank for combat potions...");
                withdrawItem(6, Vars.combatPotionIds[3]);
            }

            // Withdraw BP shit
            if (Vars.outOfScales || Vars.outOfDarts)
            {
                System.out.println("Bank: checking bank for zulrah scales / darts");

                if (Vars.outOfScales)
                    withdrawItem(0, Constants.ZULRAH_SCALES_ID);

                if (Vars.outOfDarts)
                    withdrawItem(0, Constants.MITHRIL_DARTS_ID);
            }

            // Handle empty coffer
            if (Vars.isCofferEmpty)
            {
                final int coinsId = 995;
                RSItem[] coinsItems = Banking.find(coinsId);

                if (coinsItems.length > 0)
                {
                    RSItem coinsItem = coinsItems[0];
                    int coinsStack = coinsItem.getStack();

                    if (coinsStack < 26000)
                    {
                        System.out.println("Bank: Coffer is empty and do not have enough gp for another NMZ session. Ending...");
                        Vars.shouldExecute = false;
                        return;
                    }

                    int count = 26000 * (coinsStack / 26000);
                    withdrawItem(count, coinsId);
                }
            }

            withdrawItem(22, Constants.ID_PRAYER_POTION_4);
            Banking.close();
        }
    }

    private boolean openBank()
    {
        Banking.openBank();

        if (!Timing.waitCondition(()->
        {
            General.sleep(100);
            return Banking.isBankScreenOpen();
        }, 4000))
        {
            System.out.println("Bank: failed to open bank.");
            return false;
        }

        return true;
    }

    private boolean depositAll()
    {
        Banking.depositAll();

        if (!Timing.waitCondition(()->
        {
            General.sleep(100);
            return Inventory.getAll().length == 0;
        }, 4000))
        {
            System.out.println("Bank: failed to deposit all items.");
            return false;
        }

        return true;
    }

    private void withdrawItem(int count, int id)
    {
        RSItem[] items = Banking.find(id);

        if (items.length <= 0)
        {
            System.out.println("Bank: Out of either prayer pot, scales or dart. Logging out.");
            Vars.shouldExecute = false;
            Banking.close();
            return;
        }

        Banking.withdraw(count, id);
        Timing.waitCondition(()->
        {
            General.sleep(100);
            return Inventory.find(id).length > 20;
        }, 4000);
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldBank();
    }
}
