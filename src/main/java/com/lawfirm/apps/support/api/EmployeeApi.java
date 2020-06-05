/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.support.api;

import com.lawfirm.apps.model.Employee;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author newbiecihuy
 */
@Data
@ToString
public class EmployeeApi {

    protected String name;
    protected String nik;
    protected String email;
    protected String address;
    protected String npwp;
    protected String tax_status;
    protected String mobile_phone;
    protected String userName;
    protected String roleName;
    protected String userPass;
    protected String passUser;
    protected Date dateRegister;
    protected Double salary;
    protected Double loanAmount;
    protected String gender;
    protected char isActive;
    protected Employee approvedBy;
    protected Date input_date;
    protected Date aprroved_date;
    protected String linkCv;
//    private MultipartFile doc_cv;

   
}
