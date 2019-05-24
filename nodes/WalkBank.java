package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.utility.Validators;


public class WalkBank extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("WalkBank initiated... Executing...");

        DaxWalker.walkTo(Constants.AREA_BANK.getRandomTile());
        Timing.waitCondition(()->
        {
            General.sleep(150);
            return Constants.AREA_BANK.contains(Player.getPosition());
        }, 10000);
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldWalkBank();
    }
}
