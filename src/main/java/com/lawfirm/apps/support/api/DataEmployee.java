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
public class DataEmployee {

//    protected EmployeeApi employeeApi;    
//    protected AccountApi accountApi;
    Long id_employee;
    protected Long id_employee_admin;
    protected String employeeId;
    protected String name;
    protected String nik;
    protected String email;
    protected String address;
    protected String npwp;
    protected String tax_status;
    protected String cell_phone;
    protected String user_name;
    protected String role_name;
    protected String user_pass;
    protected String passUser;
    protected Date dateRegister;
    protected Double salary;
    protected Double loan_limit;
    protected String gender;
    protected char isActive;
    protected Employee approvedBy;
    protected Date input_date;
    protected Date aprroved_date;
    protected String linkCv;
    protected String bank_name_p;
    protected String account_number_p;
    protected String account_name_p;
    protected String bank_name_l;
    protected String account_number_l;
    protected String account_name_l;
    protected char idem;

//    @Override
//    public String toString() {
//        return "EmployeeApi [name=" + getEmployeeApi().name + ", nik=" + getEmployeeApi().nik + ", email=" + getEmployeeApi().email + ", address=" + getEmployeeApi().address + ", npwp=" + getEmployeeApi().npwp + ", tax_status=" + getEmployeeApi().tax_status + ", mobile_phone=" + getEmployeeApi().mobile_phone + ""
//                + ", bank_name_p=" + this.getAccountApi().bank_name_p + ", account_number_p=" + getAccountApi().account_number_p + ", account_name_p=" + getAccountApi().account_name_p + ""
//                + ", bank_name_l=" + getAccountApi().bank_name_l + ", account_number_l=" + getAccountApi().account_number_l + ", account_name_l=" + getAccountApi().account_name_l + "]";
//    }
}
