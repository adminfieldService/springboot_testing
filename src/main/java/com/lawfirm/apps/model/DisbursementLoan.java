/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_disburse_loan")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "DisbursementLoan.findAll", query = "SELECT d FROM DisbursementLoan d")})
public class DisbursementLoan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "disbursement_uu_id")
    private String disbursementUUId;

    @Column(name = "disbursement_id")
    private String disbursementId;
//    @Column(name = "team_member_id")
//    private Long teamMemberId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Column(name = "disburse_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date disburse_date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "tgl_input", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tgInput;

    @Column(name = "tahun_input", length = 10, nullable = true)
    private String tahunInput;

    @Column(name = "bulan_input", length = 10, nullable = true)
    private String bulanInput;

    @Column(name = "disbursement_amount")
    private Double disbursementAmount;

    @Column(name = "signature")
    private String signature;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loan", referencedColumnName = "id")
    private Loan loan;

    @PrePersist
    public void onCreate() {
        isActive = "1";
        tgInput = new Date();
        this.setDisbursementUUId(UUID.randomUUID().toString());
    }

    public DisbursementLoan(String disbursementUUId, String disbursementId, Date disburse_date, Date tgInput, String tahunInput, String bulanInput, Double disbursementAmount, String signature, String isActive) {
        this.disbursementUUId = disbursementUUId;
        this.disbursementId = disbursementId;
        this.disburse_date = disburse_date;
        this.tgInput = tgInput;
        this.tahunInput = tahunInput;
        this.bulanInput = bulanInput;
        this.disbursementAmount = disbursementAmount;
        this.signature = signature;
        this.isActive = isActive;
    }

    public String getDisbursementUUId() {
        return disbursementUUId;
    }

    public void setDisbursementUUId(String disbursementUUId) {
        this.disbursementUUId = disbursementUUId;
    }

    public String getDisbursementId() {
        return disbursementId;
    }

    public void setDisbursementId(String disbursementId) {
        this.disbursementId = disbursementId;
    }

    public Date getDisburse_date() {
        return disburse_date;
    }

    public void setDisburse_date(Date disburse_date) {
        this.disburse_date = disburse_date;
    }

    public Date getTgInput() {
        return tgInput;
    }

    public void setTgInput(Date tgInput) {
        this.tgInput = tgInput;
    }

    public String getTahunInput() {
        return tahunInput;
    }

    public void setTahunInput(String tahunInput) {
        this.tahunInput = tahunInput;
    }

    public String getBulanInput() {
        return bulanInput;
    }

    public void setBulanInput(String bulanInput) {
        this.bulanInput = bulanInput;
    }

    public Double getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
