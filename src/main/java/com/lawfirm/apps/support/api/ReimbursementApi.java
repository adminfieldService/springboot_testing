/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.support.api;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author newbiecihuy
 */
@Data
@ToString
public class ReimbursementApi {

    private String name;
    private String employee_id;
    protected String case_id;
    private Date expense_date;
    private Double amount;//reimburse_amount
    private String note;
    protected String link_document;
}
