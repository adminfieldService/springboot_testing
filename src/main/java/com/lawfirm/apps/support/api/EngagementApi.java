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
public class EngagementApi {

    protected Long engagement_id;
    protected String client_name;
    protected String case_id;
    protected String address;
    protected String npwp;
    protected String pic;
    protected String panitera;
    protected String strategy;
    protected Double profesional_fee;
    protected Double profesional_fee_net;
    protected Double dmp_portion;
    protected Double dmp_percent;
    protected String case_over_view;
    protected String notes;
    protected String decision;
    protected String remarks;
    protected String description;
    protected Long id_employee;
    protected Double dmp_fee;
    protected Double operational_cost;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    protected Date approved_date;
    protected String employee_id[] = null;
    protected String employee_name[] = null;
    protected Double fee_share[] = null;
    protected String member_id[] = null;

}
