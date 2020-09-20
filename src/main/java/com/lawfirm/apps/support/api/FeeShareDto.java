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
public class FeeShareDto {

    protected Double dmp_fee_new;
    protected String employee_id[] = null;
    protected String employee_name[] = null;
    protected Double fee_share_new[] = null;
}
