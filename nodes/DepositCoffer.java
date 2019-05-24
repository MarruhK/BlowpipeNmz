package scripts.gengarnmz.nodes;

import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.utility.Validators;

public class DepositCoffer extends Node
{
    @Override
    public void execute()
    {
        final int cofferId = 26292;
        RSItem[] coins = Inventory.find(Constants.COINS_ID);
        RSObject[] coffers = Objects.find(8, cofferId);

        if (coffers.length > 0 && coins.length > 0)
        {
            AccurateMouse.click(coffers[0], "Use");

            final int masterFirst = 193;
            final int componentFirst = 2;
            RSInterfaceMaster interfaceFirst = Interfaces.get(masterFirst);

            if (Timing.waitCondition(()-> interfaceFirst == null, 5000))
                return;

            interfaceFirst.getComponent(componentFirst).click();

            if (Timing.waitCondition(()-> NPCChat.getOptions()== null, 2600))
                return;

            NPCChat.selectOption("Deposit money.", true);

            if (Timing.waitCondition(()-> NPCChat.getOptions()!= null, 2500))
                return;

            Keyboard.typeSend("" + coins[0].getStack());

            if (Timing.waitCondition(()-> NPCChat.getOptions() == null, 2500))
                return;

            NPCChat.selectOption("Cancel", true);
            Timing.waitCondition(()-> NPCChat.getOptions()== null, 2500);
        }
    }

    @Override
    public boolean validate()
    {
        return Validators.shouldDepositCoffer();
    }
}
