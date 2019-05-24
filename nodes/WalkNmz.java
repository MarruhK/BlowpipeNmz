package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.utility.Validators;

public class WalkNmz extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("WalkNmz initiated... Executing...");

        DaxWalker.walkTo(Constants.AREA_NMZ.getRandomTile());
        Timing.waitCondition(()->
        {
            General.sleep(150);
            return Constants.AREA_NMZ.contains(Player.getPosition());
        }, 10000);
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldWalkNmz();
    }
}