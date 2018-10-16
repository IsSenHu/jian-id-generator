package com.husen.util;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class CommonUtils {
    private static String[] SWITCH_ON_EXP = new String[]{"ON", "TRUE", "on", "true"};

    public static String[] SWITCH_OFF_EXP = new String[]{"OFF", "FALSE", "off", "false"};

    public static boolean isOn(String swtch) {
        return Arrays.asList(SWITCH_ON_EXP).contains(swtch);
    }

    public static boolean isPropKeyOn(String key) {
        String prop = System.getProperty(key);
        return Arrays.asList(SWITCH_ON_EXP).contains(prop);
    }
}