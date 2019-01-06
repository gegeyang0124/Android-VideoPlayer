package com.zy.player;

/**
 * Put PlayerMgr into layout
 * From a PlayerMgr to another VideoPlayer
 * Created by 周阳 on 19/1/25.
 */
public class PlayerMgr {

    public static Player FIRST_FLOOR_JZVD;
    public static Player SECOND_FLOOR_JZVD;

    public static Player getFirstFloor() {
        return FIRST_FLOOR_JZVD;
    }

    public static void setFirstFloor(Player jzvd) {
        FIRST_FLOOR_JZVD = jzvd;
    }

    public static Player getSecondFloor() {
        return SECOND_FLOOR_JZVD;
    }

    public static void setSecondFloor(Player jzvd) {
        SECOND_FLOOR_JZVD = jzvd;
    }

    public static Player getCurrentJzvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_JZVD != null) {
            SECOND_FLOOR_JZVD.onCompletion();
            SECOND_FLOOR_JZVD = null;
        }
        if (FIRST_FLOOR_JZVD != null) {
            FIRST_FLOOR_JZVD.onCompletion();
            FIRST_FLOOR_JZVD = null;
        }
    }
}
