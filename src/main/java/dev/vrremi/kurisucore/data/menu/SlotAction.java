package dev.vrremi.kurisucore.data.menu;

public enum SlotAction {

    APPLY_TAG,
    RESET_TAG,
    APPLY_RANK,
    APPLY_PERMANENT,
    APPLY_7_DAYS,
    APPLY_1_MONTH,
    APPLY_3_MONTH;

    public long getTime() {
        switch (this) {
            case APPLY_7_DAYS:
                return 604800000 + System.currentTimeMillis();
            case APPLY_1_MONTH:
                return 2592000000L + System.currentTimeMillis();
            case APPLY_3_MONTH:
                return 7776000000L + System.currentTimeMillis();
            case APPLY_PERMANENT:
                return Long.MAX_VALUE;
        }
        return 0;
    }

}
