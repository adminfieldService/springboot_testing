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
public class AprovedApi {

    Long id_employee_admin;
    Long id_employee_finance;
//    Long[] employee_id = new Long[10];
//    Long id_employee;
    String employee_id;
    String role_name;
    Double salary = 0d;
    Double loan_amount = 0d;
}
