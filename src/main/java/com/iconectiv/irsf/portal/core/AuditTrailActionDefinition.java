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
    public static final String Remove_Rule_From_Partition = "remove rule";
    public static final String Export_Partition_Data = "export partition";
    public static final String Send_Partition_Data_To_EI = "send partition to EI";
    public static final String Re_Send_Partition_Data_To_EI = "resend partition to EI";
    public static final String Delete_Export_History = "delete partition history";
    public static final String Refresh_Partition_Data = "refresh partition";

    public static final String Create_Rule = "create rule";
    public static final String Update_Rule = "update rule";

    public static final String Update_List_Definition = "update list";
    public static final String Update_List_Entry = "update list entry";
    public static final String Delete_List_Entry = "delete list data";
    public static final String Delete_List= "delete list";
	public static final String Add_List_Record = "add list record";
	public static final String Update_List_Record = "update list record";
	public static final String Delete_List_Record = "delete list record";
}
