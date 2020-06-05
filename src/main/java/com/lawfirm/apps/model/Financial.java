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
import javax.persistence.Basic;
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
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_financial")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "Financial.findAll", query = "SELECT f FROM Financial f")})
public class Financial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "financial_seq", sequenceName = "financial_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "financial_seq")
    @Column(name = "financial_id")
    private Long financialId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", referencedColumnName = "loan_id")
    private Loan loan;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id", referencedColumnName = "team_member_id")
    private TeamMember teamMember;
    @Column(name = "salary")
    private Double salary;
    @Column(name = "pkp")
    private Double pkp;
    @Column(name = "pajak")
    private Double pajak;
    @Column(name = "nett_profit")
    private Double nettProfit;
    @Column(name = "status", length = 1)
    private String status;
    @Column(name = "out_standing")
    private Double outStanding;
    @Basic(optional = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "created_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;

    @Basic(optional = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "disburse_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date disburse_date;
// 
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
    private Engagement engagement;

    @Column(name = "signature")
    private String signature;

    @PrePersist
    public void onCreate() {
        this.status = "0";
    }

    public Financial() {

    }

    public Long getFinancialId() {
        return financialId;
    }

    public void setFinancialId(Long financialId) {
        this.financialId = financialId;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getPkp() {
        return pkp;
    }

    public void setPkp(Double pkp) {
        this.pkp = pkp;
    }

    public Double getPajak() {
        return pajak;
    }

    public void setPajak(Double pajak) {
        this.pajak = pajak;
    }

    public Double getNettProfit() {
        return nettProfit;
    }

    public void setNettProfit(Double nettProfit) {
        this.nettProfit = nettProfit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getDisburse_date() {
        return disburse_date;
    }

    public void setDisburse_date(Date disburse_date) {
        this.disburse_date = disburse_date;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Engagement getEngagement() {
        return engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public Double getOutStanding() {
        return outStanding;
    }

    public void setOutStanding(Double outStanding) {
        this.outStanding = outStanding;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.Financial[financialId=" + this.financialId + " ]";
    }
}
