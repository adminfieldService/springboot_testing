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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "tbl_professional")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "Professional.findAll", query = "SELECT p FROM Professional p")})
public class Professional implements Serializable {

//    private static final long serialVersionUID = 1L;
//    @Id
//    @SequenceGenerator(name = "profess_seq", sequenceName = "profess_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profess_seq")
//    @Column(name = "professional_id")
//    private Long professionalId;
    @EmbeddedId
    private ProfessionalPK professionalPK;

    @Column(name = "amount", nullable = true)
    private Double amount;
    @Column(name = "perecentage", nullable = true)
    private Double perecentage;
    @Column(name = "status", length = 1, nullable = true)
    private String status;

    @Basic(optional = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "input_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date iputDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)// fetch = FetchType.LAZY
    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
    private CaseDetails caseDetails;

    @PrePersist
    public void onCreate() {

        iputDate = new Date();
        professionalPK.setUuid(UUID.randomUUID().toString());
    }

    public Professional() {
    }

    public ProfessionalPK getProfessionalPK() {
        return professionalPK;
    }

    public Date getIputDate() {
        return iputDate;
    }

    public void setIputDate(Date iputDate) {
        this.iputDate = iputDate;
    }

    public void setProfessionalPK(ProfessionalPK professionalPK) {
        this.professionalPK = professionalPK;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public CaseDetails getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(CaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPerecentage() {
        return perecentage;
    }

    public void setPerecentage(Double perecentage) {
        this.perecentage = perecentage;
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

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.Professional[ professionalPK=" + professionalPK + " ]";
    }

}
