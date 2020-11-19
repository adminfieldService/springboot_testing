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
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
//@Table(name = "tbl_case_details")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING, length = 80)
@DiscriminatorValue("CaseDetails")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "CaseDetails.findAll", query = "SELECT d FROM CaseDetails d")})
public class CaseDetails extends Engagement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "case_id", length = 20, unique = true)
    protected String caseID;
    @Column(name = "profesional_fee")
    private Double profesionalFee;

    @Column(name = "profesional_fee_net")
    private Double profesionalFeeNet;

    @Column(name = "case_over_view")
    private String caseOverview;
    @Column(name = "note")
    private String note;
    @Column(name = "target_achievement")
    private String targetAchievement;
    @Column(name = "strategy")
    private String strategy;
    @Column(name = "panitera")
    private String panitera;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Column(name = "event_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date event_date;

    @Column(name = "event_time", length = 10, nullable = true)
    private String event_time;

    @OneToMany(mappedBy = "caseDetails")
    private Collection<CaseDocument> caseDocumentCollection;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "caseDetails")
    private Collection<Professional> professionalCollection;

    @OneToMany(mappedBy = "caseDetails")
    private List<Events> evenList;

//    @Basic(optional = false)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
//    @Column(name = "created_date", nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    protected Date created_date;
    @PrePersist
    @Override
    protected void onCreate() {
        isActive = "0";
        created_date = new Date();
//        dmPercent = 40;
    }

    public CaseDetails() {
    }

    public CaseDetails(Double profesionalFee, Double profesionalFeeNet, String caseOverview, String note, String targetAchievement, String strategy, String panitera, Date event_date, String event_time, Collection<CaseDocument> caseDocumentCollection, Collection<Professional> professionalCollection, List<Events> evenList, Long engagementId, String isActive, String approvedBy, String closedBy, Date created_date, Date approved_date, Date closed_date, Date disburseDate, Date cutOffDate, String disburseBy, String signature, String invoiceNumber, String status, Double dmpPortion, Integer dmPercent, ClientData client, Employee employee, Collection<TeamMember> teamMemberCollection, Collection<EngagementHistory> engagementHistoryCollection, Collection<Financial> financialCollection, Collection<Loan> loanCollection, Collection<Disbursement> disbursementCollection, String tahun_input, String caseID) {
        super(engagementId, isActive, approvedBy, closedBy, created_date, approved_date, closed_date, disburseDate, cutOffDate, disburseBy, signature, invoiceNumber, status, dmpPortion, dmPercent, client, employee, teamMemberCollection, engagementHistoryCollection, financialCollection, loanCollection, disbursementCollection, tahun_input, caseID);
        this.profesionalFee = profesionalFee;
        this.profesionalFeeNet = profesionalFeeNet;
        this.caseOverview = caseOverview;
        this.note = note;
        this.targetAchievement = targetAchievement;
        this.strategy = strategy;
        this.panitera = panitera;
        this.event_date = event_date;
        this.event_time = event_time;
        this.caseDocumentCollection = caseDocumentCollection;
        this.professionalCollection = professionalCollection;
        this.evenList = evenList;
    }

    public Double getProfesionalFee() {
        return profesionalFee;
    }

    public void setProfesionalFee(Double profesionalFee) {
        this.profesionalFee = profesionalFee;
    }

    public Double getProfesionalFeeNet() {
        return profesionalFeeNet;
    }

    public void setProfesionalFeeNet(Double profesionalFeeNet) {
        this.profesionalFeeNet = profesionalFeeNet;
    }

    public String getCaseOverview() {
        return caseOverview;
    }

    public void setCaseOverview(String caseOverview) {
        this.caseOverview = caseOverview.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getTargetAchievement() {
        return targetAchievement;
    }

    public void setTargetAchievement(String targetAchievement) {
        this.targetAchievement = targetAchievement.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getPanitera() {
        return panitera;
    }

    public void setPanitera(String panitera) {
        this.panitera = panitera.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public List<Events> getEvenList() {
        return evenList;
    }

    public void setEvenList(List<Events> evenList) {
        this.evenList = evenList;
    }

    public Collection<CaseDocument> getCaseDocumentCollection() {
        return caseDocumentCollection;
    }

    public void setCaseDocumentCollection(Collection<CaseDocument> caseDocumentCollection) {
        this.caseDocumentCollection = caseDocumentCollection;
    }

    public Collection<Professional> getProfessionalCollection() {
        return professionalCollection;
    }

    public void setProfessionalCollection(Collection<Professional> professionalCollection) {
        this.professionalCollection = professionalCollection;
    }

    @Override
    public Long getEngagementId() {
        return engagementId;
    }

    @Override
    public void setEngagementId(Long engagementId) {
        this.engagementId = engagementId;
    }

    @Override
    public String getCaseID() {
        return caseID;
    }

    @Override
    public void setCaseID(String caseID) {
        this.caseID = caseID.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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
    public String getIsActive() {
        return isActive;
    }

    @Override
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String getApprovedBy() {
        return approvedBy;
    }

    @Override
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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
    public String getStatus() {
        return status;
    }

    @Override
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
    public ClientData getClient() {
        return client;
    }

    @Override
    public void setClient(ClientData client) {
        this.client = client;
    }

    @Override
    public Date getApproved_date() {
        return approved_date;
    }

    @Override

    public void setApproved_date(Date approved_date) {
        this.approved_date = approved_date;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
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

    @Override
    public Date getCreated_date() {
        return created_date;
    }

    @Override
    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    @Override
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    @Override
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    /**
     * @return the financialCollection
     */
    @Override
    public Collection<Financial> getFinancialCollection() {
        return financialCollection;
    }

    /**
     * @param financialCollection the financialCollection to set
     */
    @Override
    public void setFinancialCollection(Collection<Financial> financialCollection) {
        this.financialCollection = financialCollection;
    }

    @Override
    public Collection<Loan> getLoanCollection() {
        return loanCollection;
    }

    @Override
    public void setLoanCollection(Collection<Loan> loanCollection) {
        this.loanCollection = loanCollection;
    }

    @Override
    public String getTahun_input() {
        return tahun_input;
    }

    @Override
    public void setTahun_input(String tahun_input) {
        this.tahun_input = tahun_input.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Double getDmpPortion() {
        return dmpPortion;
    }

    @Override
    public void setDmpPortion(Double dmpPortion) {
        this.dmpPortion = dmpPortion;
    }

    @Override
    public Integer getDmPercent() {
        return dmPercent;
    }

    @Override
    public void setDmPercent(Integer dmPercent) {
        this.dmPercent = dmPercent;
    }

    @Override
    public Collection<Disbursement> getDisbursementCollection() {
        return disbursementCollection;
    }

    @Override
    public void setDisbursementCollection(Collection<Disbursement> disbursementCollection) {
        this.disbursementCollection = disbursementCollection;
    }

    @Override
    public String getClosedBy() {
        return closedBy;
    }

    @Override
    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    @Override
    public Date getClosed_date() {
        return closed_date;
    }

    @Override
    public void setClosed_date(Date closed_date) {
        this.closed_date = closed_date;
    }

    @Override
    public Date getDisburseDate() {
        return disburseDate;
    }

    @Override
    public void setDisburseDate(Date disburseDate) {
        this.disburseDate = disburseDate;
    }

    @Override
    public String getDisburseBy() {
        return disburseBy;
    }

    @Override
    public void setDisburseBy(String disburseBy) {
        this.disburseBy = disburseBy;
    }

    @Override
    public Date getCutOffDate() {
        return cutOffDate;
    }

    @Override
    public void setCutOffDate(Date cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.CaseDetails[engagementId=" + this.engagementId + " ]";
    }
}
