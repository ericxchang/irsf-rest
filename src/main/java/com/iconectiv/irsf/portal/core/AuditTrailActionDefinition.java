package com.iconectiv.irsf.portal.core;

/**
 * Created by echang on 3/30/2017.
 */
public class AuditTrailActionDefinition {
	private AuditTrailActionDefinition() {
		
	}
    public static final String Clone_Partition = "clone partition";
    public static final String Clone_Rule = "clone rule";
    public static final String Create_Partition = "create partition";
    public static final String Update_Partition = "update partition";
    public static final String Add_Rule_To_Partition = "add rule";
    public static final String Remove_Rule_To_Partition = "remove rule";
    public static final String Export_Partition_Data = "export partition";
    public static final String Refresh_Partition_Data = "refresh partition";

    public static final String Create_Rule = "create rule";
    public static final String Update_Rule = "update rule";
	public static final String Update_List_Entry = "update list entry";
}
