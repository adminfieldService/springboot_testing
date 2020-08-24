/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_reimbursement")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "Reimbursement.findAll", query = "SELECT r FROM Reimbursement r")})
public class Reimbursement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "reimburse_seq", sequenceName = "reimburse_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reimburse_seq")
    @Column(name = "reimburse_id")
    private Long reimburseId;

    @Column(name = "reimbursement_id", length = 30)
    private String reimbursementId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
    private Employee employee;

    @Column(name = "status")
    private String status;

//    @Column(name = "expense_date ")
//    private String expenseDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Column(name = "expense_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date expenseDate;

    @Column(name = "note")
    private String note;

    @Column(name = "reimburse_amount")
    private Double reimburseAmount;

//    @Column(name = "approved_by")
//    private String approvedBy;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reimbursement")
    private Collection<DocumentReimburse> documentReimburseCollection;

//    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private Loan loan;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
    protected Engagement engagement;

    @OneToMany(mappedBy = "reimbursement", cascade = CascadeType.ALL)
    private Collection<ReimbursementHistory> reimbursementHistoryCollection;

    @Column(name = "signature")
    private String signature;

    public Reimbursement() {
    }

    public Reimbursement(Long reimburseId, String reimbursementId, Employee employee, String status, Date expenseDate, String note, Double reimburseAmount, Collection<DocumentReimburse> documentReimburseCollection, Engagement engagement, Collection<ReimbursementHistory> reimbursementHistoryCollection, String signature) {
        this.reimburseId = reimburseId;
        this.reimbursementId = reimbursementId;
        this.employee = employee;
        this.status = status;
        this.expenseDate = expenseDate;
        this.note = note;
        this.reimburseAmount = reimburseAmount;
        this.documentReimburseCollection = documentReimburseCollection;
        this.engagement = engagement;
        this.reimbursementHistoryCollection = reimbursementHistoryCollection;
        this.signature = signature;
    }

    
    public Long getReimburseId() {
        return reimburseId;
    }

    public void setReimburseId(Long reimburseId) {
        this.reimburseId = reimburseId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public Collection<DocumentReimburse> getDocumentReimburseCollection() {
        return documentReimburseCollection;
    }

    public void setDocumentReimburseCollection(Collection<DocumentReimburse> documentReimburseCollection) {
        this.documentReimburseCollection = documentReimburseCollection;
    }

    public Engagement getEngagement() {
        return engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public Double getReimburseAmount() {
        return reimburseAmount;
    }

    public void setReimburseAmount(Double reimburseAmount) {
        this.reimburseAmount = reimburseAmount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(String reimbursementId) {
        this.reimbursementId = reimbursementId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public Collection<ReimbursementHistory> getReimbursementHistoryCollection() {
        return reimbursementHistoryCollection;
    }

    public void setReimbursementHistoryCollection(Collection<ReimbursementHistory> reimbursementHistoryCollection) {
        this.reimbursementHistoryCollection = reimbursementHistoryCollection;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.Reimbursement[reimburseId=" + this.reimburseId + " ]";
    }

}
