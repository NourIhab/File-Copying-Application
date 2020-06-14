package com.asset.fc.website.jsf.constant;

/**
 *
 * @author nour.ihab
 */
public enum ImportMethod {
    JSON(1),
    XML(2),
    CSV(3),
    HTTPREQUEST(4);

    private final int value;

    private ImportMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
