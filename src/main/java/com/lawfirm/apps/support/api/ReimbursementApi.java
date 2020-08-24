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

    private Date expense_date;
    private String note;
    private Double reimburse_amount;
    protected String case_id;
    protected Long id_loan;
    protected String link_document;
}
