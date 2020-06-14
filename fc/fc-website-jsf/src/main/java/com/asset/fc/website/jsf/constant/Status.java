package com.asset.fc.website.jsf.constant;

/**
 *
 * @author nour.ihab
 */
public enum Status {
    NEW(1),
    ENQUEUED_FOR_PARSING(2),
    PARSING(3),
    ENQUEUED_FOR_COPYING(4),
    COPYING(5),
    DONE(6),
    FAILED(7);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
