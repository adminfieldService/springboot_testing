/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_period")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "EntityPeriod.findAll", query = "SELECT p FROM EntityPeriod p")})
public class EntityPeriod implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @SequenceGenerator(name = "loan_history_seq", sequenceName = "loan_history_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_history_seq")
    @Column(name = "id")
    private String Id;

    @Column(name = "case_id", length = 10)
    private String caseId;

    @Column(name = "disburse_id", length = 10)
    private String disburseId;

    @Column(name = "tax_year", length = 10)
    private String taxYear;

    @Column(name = "bulan_disburse", length = 10)
    private String bulanDisburse;

    @Column(name = "number_disbursement", length = 10)
    private Integer numberDisbursement;

    @Column(name = "prev_disbursement")
    private Double prevDisbursement;
    
    @Column(name = "amount_portion")
    private Double amountPortion;

    @Column(name = "income_tax_paid_on_prior_period")
    private Double incomeTaxPaidOnPriorPeriod;

    @Column(name = "id_employee")
    private Long idEmployee;

    @Column(name = "employee_id", length = 10)
    private String employeeId;

    @Column(name = "status", length = 1)
    protected String status;

    @PrePersist
    public void onCreate() {
        this.setId(UUID.randomUUID().toString());
        this.status = "1";
    }

    public EntityPeriod() {
    }

    public EntityPeriod(String Id, String caseId, String disburseId, String taxYear, String bulanDisburse, Integer numberDisbursement, Double prevDisbursement, Double amountPortion, Double incomeTaxPaidOnPriorPeriod, Long idEmployee, String employeeId, String status) {
        this.Id = Id;
        this.caseId = caseId;
        this.disburseId = disburseId;
        this.taxYear = taxYear;
        this.bulanDisburse = bulanDisburse;
        this.numberDisbursement = numberDisbursement;
        this.prevDisbursement = prevDisbursement;
        this.amountPortion = amountPortion;
        this.incomeTaxPaidOnPriorPeriod = incomeTaxPaidOnPriorPeriod;
        this.idEmployee = idEmployee;
        this.employeeId = employeeId;
        this.status = status;
    }

   

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDisburseId() {
        return disburseId;
    }

    public void setDisburseId(String disburseId) {
        this.disburseId = disburseId;
    }

    public String getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(String taxYear) {
        this.taxYear = taxYear;
    }

    public String getBulanDisburse() {
        return bulanDisburse;
    }

    public void setBulanDisburse(String bulanDisburse) {
        this.bulanDisburse = bulanDisburse;
    }

    public Integer getNumberDisbursement() {
        return numberDisbursement;
    }

    public void setNumberDisbursement(Integer numberDisbursement) {
        this.numberDisbursement = numberDisbursement;
    }

    public Double getPrevDisbursement() {
        return prevDisbursement;
    }

    public void setPrevDisbursement(Double prevDisbursement) {
        this.prevDisbursement = prevDisbursement;
    }

    public Double getIncomeTaxPaidOnPriorPeriod() {
        return incomeTaxPaidOnPriorPeriod;
    }

    public void setIncomeTaxPaidOnPriorPeriod(Double incomeTaxPaidOnPriorPeriod) {
        this.incomeTaxPaidOnPriorPeriod = incomeTaxPaidOnPriorPeriod;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Double getAmountPortion() {
        return amountPortion;
    }

    public void setAmountPortion(Double amountPortion) {
        this.amountPortion = amountPortion;
    }
    

}
