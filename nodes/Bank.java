package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.utility.Validators;

public class Bank extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Bank initiated... Executing...");

        Banking.openBank();
        if (!Timing.waitCondition(()->
        {
            General.sleep(100);
            return Banking.isBankScreenOpen();
        }, 4000))
        {
            System.out.println("Bank ~~ failed to open bank.");
            return;
        }

        Banking.depositAll();
        if (!Timing.waitCondition(()->
        {
            General.sleep(100);
            return Inventory.getAll().length == 0;
        }, 4000))
        {
            System.out.println("Bank ~~ failed to deposit all items.");
            return;
        }

        if (Vars.outOfScales || Vars.outOfDarts)
        {
            System.out.println("Bank ~~ checking bank for zulrah scales / darts");

            if (Vars.outOfScales)
                withdrawItem(0, Constants.ZULRAH_SCALES_ID);

            if (Vars.outOfDarts)
                withdrawItem(0, Constants.MITHRIL_DARTS_ID);
        }

        if (Vars.isCofferEmpty)
        {
            final int coinsId = 995;
            RSItem[] coinsItems = Banking.find(coinsId);

            if (coinsItems.length > 0)
            {
                RSItem coinsItem = coinsItems[0];
                int coinsStack = coinsItem.getStack();
                int count = coinsStack >= 260000 ? 260000 : 26000 * (coinsStack / 26000);

                withdrawItem(count, coinsId);
            }
        }

        withdrawItem(22, Constants.ID_PRAYER_POTION_4);
        Banking.close();
    }

    private void withdrawItem(int count, int id)
    {
        RSItem[] items = Banking.find(id);

        if (items.length <= 0)
        {
            System.out.println("Bank ~~ Out of either prayer pot, scales or dart. Logging out.");
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
