package org.sample.data;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

@DataObject
public class LibraryRecord {

	private String libUserId;
	

	private String userType;
	
	private String createDate;
	
	private String updateDate;
	
	private int itemCount;
	
	private String holdDays;
	

	
	public LibraryRecord() {
		
	}
	
	public LibraryRecord(String libUserId, String userType, String createDate, String updateDate, int itemCount,
			String holdDays) {
		super();
		this.libUserId = libUserId;
		this.userType = userType;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.itemCount = itemCount;
		this.holdDays = holdDays;
	}
	
	
		
	public LibraryRecord(JsonObject json) {
	
		this.libUserId = json.getString("lib_user_id");
		this.userType = json.getString("user_type");
		this.createDate = json.getString("create_date");
		this.updateDate = json.getString("update_date");
		this.itemCount = json.getInteger("item_count");
		this.holdDays = json.getString("hold_days");
	}


		
	public String getLibUserId() {
		return libUserId;
	}

	public void setLibUserId(String libUserId) {
		this.libUserId = libUserId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public String getHoldDays() {
		return holdDays;
	}

	public void setHoldDays(String holdDays) {
		this.holdDays = holdDays;
	}

	public JsonObject toJson() {
		return JsonObject.mapFrom(this);
	}
	
	@Override
	public String toString() {
		return Json.encodePrettily(this);
	}
	
}
