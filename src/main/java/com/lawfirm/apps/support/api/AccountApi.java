/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.support.api;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @author newbiecihuy
 */
@Data
@ToString
public class AccountApi {

    protected String bank_name_p;
    protected String account_number_p;
    protected String account_name_p;
    protected String bank_name_l;
    protected String account_number_l;
    protected String account_name_l;

}
