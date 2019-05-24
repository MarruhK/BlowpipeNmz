package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.AAAgengarapi.GengarClicking;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;
import scripts.gengarnmz.utility.Validators;


public class StartDream extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("StartDream initiated... Executing...");

        // Click dominic, dream
        final int dominicId = 1120;
        RSNPC[] dominic = NPCs.findNearest(dominicId);

        if (dominic.length > 0)
        {
            AccurateMouse.click(dominic[0], "Dream");
            selectCustomizableRumbleHard();
        }

        // if dream selected, then drink the potion and ensure monsters are selected that are needed
        final int potionId = 26291;
        RSObject[] potion = Objects.find(6, potionId);

        if (potion.length > 0)
        {
            // Try to get accurate mouse to work iwth this shit, not working idk why
            GengarClicking.clickObject(potion[0], "Drink");
            initializeDream();
        }
    }

    private void selectCustomizableRumbleHard()
    {
        // Wait for chat interface to come and see if u can select the shit
        if (Timing.waitCondition(()->
        {
            General.sleep(General.random(150, 300));
            return NPCChat.getOptions() != null;
        }, 3000) && NPCChat.selectOption("Previous: Customisable Rumble (hard)", true))
        {
            continueChat();
            NPCChat.selectOption("Yes", true);
            continueChat();
        }
        else
        {
            // set it up
            System.out.println("selectCustomizableRumbleHard ~~ set up needed???");
        }
    }

    private void continueChat()
    {
        do
        {
            NPCChat.clickContinue(true);
            General.sleep(300, 450);
        }
        while (NPCChat.getClickContinueInterface() != null);
        General.sleep(300, 350);
    }

    private void initializeDream()
    {
        final int unselectedColour = 9408399;

        final int master = 129;
        final int childAccept = 6;
        final int childListOfMobs = 13;

        // Wait for interface to show up
        Timing.waitCondition(()->
        {
            General.sleep(150);
            return Interfaces.get(master) != null;
        }, 5000);

        // Relevant component is divisible by 3
        RSInterfaceComponent[] allComponents = Interfaces.get(master, childListOfMobs).getChildren();
        RSInterfaceComponent[] relevantComponents = new RSInterfaceComponent[allComponents.length / 3];

        int counter = 0;

        // Create relevant list of components
        for (int i = 0; i < allComponents.length; i++)
        {
            if (i % 3 == 0)
                relevantComponents[counter++] = allComponents[i];
        }

        // Iterate over relevant component and ensure that mobs are selected.
        for (RSInterfaceComponent component : relevantComponents)
        {
            // check if the component's text is a relevant mob
            if (component.isHidden())
                continue;

            if (isRelevantMob(component.getText()))
            {
                // if it is, check if the colour is selected
                if (component.getTextColour() == unselectedColour)
                {
                    int componentValue = component.getComponentIndex();

                    while (!selectComponent(Interfaces.get(master, childListOfMobs).getChild(componentValue)))
                    {
                        General.sleep(150);
                    }
                }
            }
        }

        Interfaces.get(master, childAccept).click();
        Timing.waitCondition(()-> Validators.isDreaming(), 7000);
    }

    private boolean isRelevantMob(String componentText)
    {
        boolean isRelevant = false;

        for (String mob : Constants.mobs)
        {
            if (componentText.equals(mob))
                isRelevant = true;
        }
        return isRelevant;
    }

    private boolean selectComponent(RSInterfaceComponent component)
    {

        final int selectedColour = 16750623;

        component.click();

        return Timing.waitCondition(()->
        {
            General.sleep(100, 350);
            return component.getTextColour() == selectedColour;
        }, General.random(900, 1450));
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldStartDream();
    }
}
