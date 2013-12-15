package main.resources;

/**
 * User: Heinrich Quirit
 * Last Modified: 9/25/13
 * Time: 6:48 PM
 */
public enum Permission {
	
    USER("staffticket.myticket"), STAFF("staffticket.mod"), ADMIN("staffticket.admin");
    
    private String permission;
    
    Permission(String permission) {
    	this.permission = permission;
    }
    
    public String getPerm() {
    	return permission;
    }
}
