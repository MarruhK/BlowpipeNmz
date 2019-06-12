package scripts.gengarnmz.gui;

import org.tribot.api2007.Combat;
import scripts.gengarnmz.data.AttackStyles;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class NmzGui extends JFrame implements ActionListener
{
    private String[] weapons = {"", "Bludgeon", "Blowpipe"};
    private int[][] weaponIds = {null, Constants.BLUDGEON_IDS, Constants.BLOWPIPE_IDS};
    private JComboBox<String> comboBox1 = new JComboBox<>(weapons);

    private String[] styles = {"", "Attack", "Strength", "Defence", "Controlled", "Range", "Long Range"};
    private AttackStyles[] attackStyles = {null, AttackStyles.ATTACK, AttackStyles.STRENGTH, AttackStyles.DEFENCE,
                                            AttackStyles.CONTROLLED, AttackStyles.RANGE, AttackStyles.LONGRANGE};
    private JComboBox<String> comboBox2 = new JComboBox<>(styles);

    private String[] potions = {"", "Ranging", "Super Ranging", "Overload", "Super Combat"};
    private int[][] potionIds = {null, Constants.ID_RANGING_POTIONS, Constants.ID_SUPER_RANGING_POTIONS, Constants.ID_OVERLOADS, Constants.ID_SUPER_COMBAT_POTIONS};
    private JComboBox<String> comboBox3 = new JComboBox<>(potions);

    public NmzGui()
    {
        JLabel label1 = new JLabel("Weapon");
        JLabel label2 = new JLabel("Skill to train");
        JLabel label3 = new JLabel("Potions to use");
        JButton button = new JButton("Start NMZ!");
        button.addActionListener(this);
        comboBox1.setPrototypeDisplayValue("------------------------------------------------------------");
        comboBox2.setPrototypeDisplayValue("------------------------------------------------------------");
        comboBox3.setPrototypeDisplayValue("------------------------------------------------------------");

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        panel1.add(label1);
        panel1.add(comboBox1);
        panel2.add(label2);
        panel2.add(comboBox2);
        panel3.add(label3);
        panel3.add(comboBox3);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.SOUTH);

        // Frame properties
        setSize(354, 250);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        // Handle X close
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                // Need to set to true to escape infinite loop
                Vars.shouldExecute = true;
                e.getWindow().dispose();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String selectedTask1 = comboBox1.getSelectedItem().toString();
        String selectedTask2 = comboBox2.getSelectedItem().toString();
        String selectedTask3 = comboBox3.getSelectedItem().toString();

        if (handleJcb(selectedTask1) && handleJcb(selectedTask2) && handleJcb(selectedTask3))
        {
            // JCB1
            for (int i = 1; i < weapons.length; i++)
            {
                if (weapons[i].equals(selectedTask1))
                    Vars.weaponIds = weaponIds[i];
            }

            // JCB2
            int tempIndex = 1;

            if (selectedTask2.equals("Range"))
                Vars.isRanging = true;

            if (!(selectedTask2.equals("Strength") || selectedTask2.equals("Range")))
            {
                AttackStyles tempStyle = null;

                for (int i = 1; i < styles.length; i++)
                {
                    if (styles[i].equals(selectedTask2))
                    {
                        System.out.println("NmzGui: tempStyle determined to be: " + AttackStyles.styleToString(tempStyle));
                        tempStyle = attackStyles[i];
                        break;
                    }
                }

                HashMap<Integer, AttackStyles> stylesMap = Constants.WEAPON_MAPPING.get(Vars.weaponIds);

                for (int i = 0; i < 4; i++)
                {
                    if (stylesMap.containsValue(tempStyle))
                    {
                        tempIndex = i;
                        break;
                    }
                }
            }

            Combat.selectIndex(tempIndex);

            // JCB3
            for (int i = 1; i < potions.length; i++)
            {
                if (potions[i].equals(selectedTask3))
                    Vars.combatPotionIds = potionIds[i];
            }

            Vars.shouldExecute = true;
            this.dispose();
        }
    }

    private boolean handleJcb(String item)
    {
        if (item.equals(""))
        {
            // select task
            JOptionPane.showMessageDialog(this,
                    "Please select a valid task.",
                    "Invalid selection!",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;
    }
}
