package com.iconectiv.irsf.portal.core;

/**
 * Created by echang on 4/28/2017.
 */
public enum PartitionExportStatus {
    Processing ("Processing"),
    Failed ("Failed"),
    Exported ("Exported"),
    ;

    private String value;

    PartitionExportStatus(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
