/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import java.io.Serializable;
import java.util.Collection;
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
    private Employee employee;

    @Column(name = "status")
    private String status;

//    @Column(name = "approved_by")
//    private String approvedBy;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reimbursement")
    private Collection<DocumentReimburse> documentReimburseCollection;

    @Column(name = "signature")
    private String signature;

    public Reimbursement() {
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
        this.signature = signature;
    }

    public Collection<DocumentReimburse> getDocumentReimburseCollection() {
        return documentReimburseCollection;
    }

    public void setDocumentReimburseCollection(Collection<DocumentReimburse> documentReimburseCollection) {
        this.documentReimburseCollection = documentReimburseCollection;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.Reimbursement[reimburseId=" + this.reimburseId + " ]";
    }

}
