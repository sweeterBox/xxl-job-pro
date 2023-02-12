package com.xxl.job.client.enums;

/**
 * Created by xuxueli on 17/5/9.
 */
public enum BlockStrategy {

    SERIAL_EXECUTION("Serial execution"),
    DISCARD_LATER("Discard Later"),
    COVER_EARLY("Cover Early");

    private String title;
    BlockStrategy(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public static BlockStrategy match(String name, BlockStrategy defaultItem) {
        if (name != null) {
            for (BlockStrategy item: BlockStrategy.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}
