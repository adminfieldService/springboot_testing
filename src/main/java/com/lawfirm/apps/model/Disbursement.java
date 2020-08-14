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
@Table(name = "tbl_disbursement")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "Disbursement.findAll", query = "SELECT d FROM Disbursement d")})
public class Disbursement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "disbursement_uu_id")
    private String disbursementUUId;

    @Column(name = "disbursement_id")
    private String disbursementId;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Loan loan;

    @PrePersist
    public void onCreate() {
        tgInput = new Date();
        this.setDisbursementUUId(UUID.randomUUID().toString());
    }

    public Disbursement() {
    }

    public Disbursement(String disbursementUUId, String disbursementId, Date disburse_date, Date tgInput, String tahunInput, String bulanInput, Double disbursementAmount, String signature, Loan loan) {
        this.disbursementUUId = disbursementUUId;
        this.disbursementId = disbursementId;
        this.disburse_date = disburse_date;
        this.tgInput = tgInput;
        this.tahunInput = tahunInput;
        this.bulanInput = bulanInput;
        this.disbursementAmount = disbursementAmount;
        this.signature = signature;
        this.loan = loan;
    }

    public String getDisbursementUUId() {
        return disbursementUUId;
    }

    public void setDisbursementUUId(String disbursementUUId) {
        this.disbursementUUId = disbursementUUId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
                .replaceAll("<script>(.*?)</script>", "")
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?/>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
                .replaceAll("vbscript", "")
                .replaceAll("encode", "")
                .replaceAll("decode", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", "")
                .replaceAll("</script>", "")
                .replaceAll("<script(.*?)>", "")
                .replaceAll("eval\\((.*?)\\)", "")
                .replaceAll("expression\\((.*?)\\)", "");
    }

    public String getDisbursementId() {
        return disbursementId;
    }

    public void setDisbursementId(String disbursementId) {
        this.disbursementId = disbursementId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
                .replaceAll("<script>(.*?)</script>", "")
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?/>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
                .replaceAll("vbscript", "")
                .replaceAll("encode", "")
                .replaceAll("decode", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", "")
                .replaceAll("</script>", "")
                .replaceAll("<script(.*?)>", "")
                .replaceAll("eval\\((.*?)\\)", "")
                .replaceAll("expression\\((.*?)\\)", "");
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
        this.tahunInput = tahunInput.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
                .replaceAll("<script>(.*?)</script>", "")
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?/>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
                .replaceAll("vbscript", "")
                .replaceAll("encode", "")
                .replaceAll("decode", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", "")
                .replaceAll("</script>", "")
                .replaceAll("<script(.*?)>", "")
                .replaceAll("eval\\((.*?)\\)", "")
                .replaceAll("expression\\((.*?)\\)", "");
    }

    public String getBulanInput() {
        return bulanInput;
    }

    public void setBulanInput(String bulanInput) {
        this.bulanInput = bulanInput.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
                .replaceAll("<script>(.*?)</script>", "")
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?/>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
                .replaceAll("vbscript", "")
                .replaceAll("encode", "")
                .replaceAll("decode", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", "")
                .replaceAll("</script>", "")
                .replaceAll("<script(.*?)>", "")
                .replaceAll("eval\\((.*?)\\)", "")
                .replaceAll("expression\\((.*?)\\)", "");
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

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

}