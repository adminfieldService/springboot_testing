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
public class CaseApi {

    protected Long engagement_id = null;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    protected Date cut_off_date = null;
}
