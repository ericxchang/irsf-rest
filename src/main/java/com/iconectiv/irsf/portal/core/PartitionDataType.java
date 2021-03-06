package com.iconectiv.irsf.portal.core;

/**
 * match data_type column of partition_data_details table
 */
public enum PartitionDataType {
    BlackList ("BL"),
    WhiteList ("WL"),
    Rule ("R"),
    ;

    private String value;

    PartitionDataType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
