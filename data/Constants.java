package scripts.gengarnmz.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants
{
    public static final String[] mobs = {"Count Draynor", "Tree spirit", "Khazard Warlord", "Bouncer", "Black Demon", "The Kendal"};

    public static final int ID_PRAYER_POTION_4 = 2434;
    public static final int ID_PRAYER_POTION_3 = 139;
    public static final int ID_PRAYER_POTION_2 = 141;
    public static final int ID_PRAYER_POTION_1 = 143;
    public static final int[] ID_PRAYER_POTIONS = {ID_PRAYER_POTION_4, ID_PRAYER_POTION_3, ID_PRAYER_POTION_2, ID_PRAYER_POTION_1};

    private static final int ID_RANGING_POTION_4 = 2444;
    private static final int ID_RANGING_POTION_3 = 169;
    private static final int ID_RANGING_POTION_2 = 171;
    private static final int ID_RANGING_POTION_1 = 173;
    private static final int ID_SUPER_COMBAT_POTION_4 = 12695;
    private static final int ID_SUPER_COMBAT_POTION_3 = 12697;
    private static final int ID_SUPER_COMBAT_POTION_2 = 12699;
    private static final int ID_SUPER_COMBAT_POTION_1 = 12701;

    public static final int ID_SUPER_RANGING_POTION_4 = 11722;
    public static final int ID_SUPER_RANGING_POTION_3 = 11723;
    public static final int ID_SUPER_RANGING_POTION_2 = 11724;
    public static final int ID_SUPER_RANGING_POTION_1 = 11725;

    public static final int[] ID_COMBAT_POTIONS =  {ID_RANGING_POTION_4, ID_RANGING_POTION_3,
                                                    ID_RANGING_POTION_2, ID_RANGING_POTION_1,
                                                    ID_SUPER_COMBAT_POTION_4, ID_SUPER_COMBAT_POTION_3,
                                                    ID_SUPER_COMBAT_POTION_2, ID_SUPER_COMBAT_POTION_1,
                                                    ID_SUPER_RANGING_POTION_4, ID_SUPER_RANGING_POTION_3,
                                                    ID_SUPER_RANGING_POTION_2, ID_SUPER_RANGING_POTION_1};

    private static final RSTile TILE_BANK_1 = new RSTile(2609, 3097, 0);
    private static final RSTile TILE_BANK_2 = new RSTile(2613, 3088, 0);
    public static final RSArea AREA_BANK = new RSArea(TILE_BANK_1, TILE_BANK_2);

    private static final RSTile TILE_NMZ_1 = new RSTile(2601, 3118, 0);
    private static final RSTile TILE_NMZ_2 = new RSTile(2611, 3112, 0);
    public static final RSArea AREA_NMZ = new RSArea(TILE_NMZ_1, TILE_NMZ_2);

    public static final int ZULRAH_SCALES_ID = 12934;
    public static final int MITHRIL_DARTS_ID = 809;
    public static final int COINS_ID = 995;

}
