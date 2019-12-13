package com.example.dewscheduler;

public final class IconResourceFinder
{
    private IconResourceFinder() {}

    public static int getIconResIdByIndex(int idx)
    {
        int[] iconIds = new int[] { R.drawable.ic_plant0, R.drawable.ic_plant1, R.drawable.ic_plant2, R.drawable.ic_plant3 };
        return idx < iconIds.length && idx >= 0 ? iconIds[idx] : iconIds[0];
    }
}
