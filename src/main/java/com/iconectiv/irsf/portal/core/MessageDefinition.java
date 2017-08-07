package com.iconectiv.irsf.portal.core;

public class MessageDefinition {
    private MessageDefinition() {
	}

	public static final String Query_Success = "";
	public static final String Create_User_Success = "Successfully create user account";
	public static final String Update_User_Success = "Successfully update user account";
	public static final String Change_Password_Success = "Password is updated successfully";
	public static final String Process_List_Upload = "System is processing your list file";
	public static final String Login_Success = "Successful login";
	public static final String Rename_List_Success = "Successfully rename list";
	public static final String Delete_List_Success = "Successfully delete list";
    public static final String Save_Partition_Success = "Successfully save partition";
	public static final String Save_Rule_Success = "Your Rule has just been saved into the following partitions: ";
	public static final String Update_Rule_Success = "Your Rule has just been updated";

	public static final String Add_ListDetails_Success = "Successfully added new list records";
	public static final String Update_ListDetails_Success = "Successfully updated list records";
	public static final String Delete_ListDetails_Success = "Successfully deleted list records";
	public static java.lang.String Generating_Partition_Dataset_Success = "System is generating partition data set, it will take a moment";
    public static java.lang.String Exporting_Partition_Dataset_Success = "System is exporting partition data set";
	public static final String ListSizeOverLimitError = "This $1 has reached the maximum of 100,000 rows.\nNo additional destination numbers can be added.";
	public static final String Remove_Rule =  "Successfully removed rule from partition";

	public static final String DELETE_PARTITION = "Successfully delete all rules from partition";
}
