package com.iconectiv.irsf.portal.core;

/**
 * Created by echang on 4/28/2017.
 */
public enum PartitionExportStatus {
    Sending ("sending"),
    Failed ("failed"),
    Success ("exported"),
    ;

    private String value;

    PartitionExportStatus(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
