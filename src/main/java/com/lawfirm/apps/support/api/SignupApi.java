/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.support.api;

import java.util.Set;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author newbiecihuy
 */
@Data
@ToString
public class SignupApi {

    protected String email;
    protected String userName;
    protected Set<String> roleName;
}
