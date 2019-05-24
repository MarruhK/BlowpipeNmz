package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.AAAgengarapi.GengarClicking;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.utility.Validators;

public class GetNmzPots extends Node
{
    private RSTile bucketTile = new RSTile(2601, 3114, 0);
    private RSTile chestTIle = new RSTile(2609, 3118, 0);

    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("GetNmzPots initiated... Executing...");

        final int dosesOneDose = Inventory.getCount(11725);
        final int dosesTwoDose = Inventory.getCount(11724) * 2;
        final int dosesThreeDose = Inventory.getCount(11723) * 3;
        final int dosesFourDose = Inventory.getCount(11722) * 4;
        final int totalDoses = dosesFourDose + dosesThreeDose + dosesTwoDose + dosesOneDose;

        if (totalDoses == 24)
            return;

        if (Vars.isBarrelEmpty && buyPotions())
        {
            Vars.isBarrelEmpty = false;
        }
        else
        {
            getPotions(totalDoses);
        }
    }

    private boolean isCofferEmpty()
    {
        final int master = 207;
        final int child = 8;
        final int component = 4;

        RSInterface cofferInterface = Interfaces.get(master, child, component);

        if (cofferInterface != null && !cofferInterface.isHidden())
        {
            int cofferCoins = cofferInterface.getComponentStack();

            if (cofferCoins < 26000)
            {
                System.out.println("isCofferEmpty ~~ coffer is empty. Getting Gp from bank to fill it up.");
                Vars.isCofferEmpty = true;
            }
        }

        return false;
    }

    private void getPotions(int totalDoses)
    {
        DaxWalker.walkTo(bucketTile);
        Timing.waitCondition(()-> Player.isMoving(), 1200);
        Timing.waitCondition(()-> !Player.isMoving(), 12500);

        // Check coffer gp interface
        if (isCofferEmpty())
            return;

        final int masterIndex = 162;
        final int childIndex = 44;
        final int idRangePotBucket = 26277;
        RSObject[] rangePotBucket = Objects.find(8, idRangePotBucket);
        RSInterface barrelInterface = Interfaces.get(masterIndex, childIndex);

        if (rangePotBucket.length <= 0)
        {
            System.out.println("getPotions ~~ Can't find potion bucket.");
            return;
        }

        // Store potions
        if (totalDoses > 24)
        {
            GengarClicking.clickObject(rangePotBucket[0], "Store");

            if (Timing.waitCondition(()-> NPCChat.getOptions() != null, 5000))
                NPCChat.selectOption("Yes, please.", false);
        }

        AccurateMouse.click(rangePotBucket[0], "Take");

        if (Timing.waitCondition(()-> (barrelInterface != null && !barrelInterface.isHidden()) || Vars.isBarrelEmpty, 5000))
        {
            String dosesLeft = barrelInterface.getText();
            dosesLeft = dosesLeft.substring(dosesLeft.indexOf("(") + 1);
            int doses = Integer.parseInt(dosesLeft.substring(0, dosesLeft.length() - 1));

            if (doses >= 24)
            {
                General.sleep(450, 900);
                String dosesNeeded = "" + (24 - totalDoses);
                Keyboard.typeSend(dosesNeeded);
            }
            else if (buyPotions())
            {
                Vars.isBarrelEmpty = false;
            }
        }
    }

    private boolean buyPotions()
    {
        DaxWalker.walkTo(chestTIle);
        Timing.waitCondition(()-> Player.isMoving(), 1200);
        Timing.waitCondition(()-> !Player.isMoving(), 7500);

        final int idNmzChest = 26273;
        RSObject[] nmzChests = Objects.find(10, idNmzChest);

        if (nmzChests.length > 0)
        {
            final int masterIndex = 206;

            final int childIndexHeader = 2;
            final int componentIndexPotions = 5;
            final int componentIndexRewardPoints = 6;

            final int childIndexBenefits = 6;
            final int componentIndexRangingPotion= 0;

            AccurateMouse.click(nmzChests[0], "Search");

            if (Timing.waitCondition(()-> Interfaces.get(masterIndex) != null, 10000))
            {
                System.out.println("buyPotions ~~ Chest has been opened");
                // Check if atleast 24000 points, if not log out.
                RSInterface pointsInterface = Interfaces.get(masterIndex, childIndexHeader, componentIndexRewardPoints);
                int points = Integer.parseInt(pointsInterface.getText().substring(15).replaceAll(",", ""));

                if (points <= 24000)
                {
                    System.out.println("buyPotions ~~ Not enough points for potions, log out");
                    Vars.shouldExecute = false;
                    return false;
                }

                RSInterface benefitsButton = Interfaces.get(masterIndex, childIndexHeader, componentIndexPotions);
                benefitsButton.click();

                if (Timing.waitCondition(()-> !Interfaces.get(masterIndex, childIndexBenefits).isHidden(), 4000))
                {
                    System.out.println("buyPotions ~~ About to buy potions");
                    RSInterface potionInterface = Interfaces.get(masterIndex, childIndexBenefits, componentIndexRangingPotion);
                    potionInterface.click("Buy-50");

                    // Check if successfully purchased
                    int currentPoints = Integer.parseInt(pointsInterface.getText().substring(15).replaceAll(",", ""));

                    if (currentPoints < points)
                    {
                        System.out.println("buyPotions ~~ Potions were purchased successfully.");

                        final int childIndexChestClose = 1;
                        final int componentIndexChestClose = 11;

                        RSInterface closeButton = Interfaces.get(masterIndex, childIndexChestClose, componentIndexChestClose);

                        if (closeButton.click())
                        {
                            System.out.println("buyPotions ~~ Successfully closed the chest interface.");
                            return true;
                        }
                    }
                }
            }
            else
            {
                System.out.println("buyPotions ~~ Failed to open the main interface");
            }
        }

        return false;
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldGetNmzPots();
    }
}
