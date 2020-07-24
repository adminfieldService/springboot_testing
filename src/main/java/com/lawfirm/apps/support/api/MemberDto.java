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
public class MemberDto {

    private String employee_id;
    private Double fee_share;
    private String employee_name;
}
