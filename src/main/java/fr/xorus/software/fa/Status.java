package fr.xorus.software.fa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xorus on 26/10/2015.
 */
public class Status {
    private static List<StatusUpdatable> statusUpdatableList = null;

    public static void registerStatusUpdatable(StatusUpdatable su) {
        if(statusUpdatableList == null) {
            statusUpdatableList = new ArrayList<>();
        }
        statusUpdatableList.add(su);
    }

    public static void updateStatus(String status, Integer progress) {
        if (statusUpdatableList == null) {
            return;
        }
        for (StatusUpdatable su : statusUpdatableList) {
            su.setStatus(status, progress);
        }
    }

    public static void updateStatus(String status) {
        if (statusUpdatableList == null) {
            return;
        }
        for (StatusUpdatable su : statusUpdatableList) {
            su.setStatus(status);
        }
    }
}
