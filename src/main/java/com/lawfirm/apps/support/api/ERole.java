/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.support.api;

/**
 *
 * @author newbiecihuy
 */
public enum ERole {
    ROLE_SysAdmin("sysadmin"),
    ROLE_FINANCE("finance"),
    ROLE_ADMIN("admin"),
    ROLE_DMP("dmp"),
    ROLE_LAWYER("lawyer");

    private String roleAssigned;

    private ERole(String roleAssigned) {
        this.roleAssigned = roleAssigned;
    }

    @Override
    public String toString() {
        return roleAssigned;
    }

}
