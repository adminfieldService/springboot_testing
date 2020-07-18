/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.support.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author newbiecihuy
 */
@Data
@ToString
public class LoanApi {

    protected Long id_employee;
    protected Long id_employee_admin;
    protected Long financial_id;
    protected Long loan_id;
    protected Long case_id;
    protected String employeeId;
    protected String name;
    protected String npwp;
    protected String bank_name_l;
    protected String account_number_l;
    protected String loan_type;
    protected Double loan_amount;
    protected Double out_standing;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    protected Date repayment_date;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    protected Date created_date;
}
