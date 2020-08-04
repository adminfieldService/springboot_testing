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
@Table(name = "tbl_events")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "Events.findAll", query = "SELECT e FROM Events e ")})
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @SequenceGenerator(name = "reimbursedoc_seq", sequenceName = "reimbursedoc_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reimbursedoc_seq")
    @Column(name = "event_id")
    private String eventId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)// fetch = FetchType.LAZY
    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
    private CaseDetails caseDetails;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Column(name = "schedule_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date scheduleDate;

    @Column(name = "schedule_time", length = 10, nullable = true)
    private String scheduleTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "date_input", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_input;

    @Column(name = "event_type", length = 1, nullable = true)
    private String eventType;

    @Column(name = "event_name", length = 255, nullable = true)
    private String eventName;

    @Column(name = "is_active", length = 1, nullable = true)
    private String isActive;

    @PrePersist
    protected void onCreate() {
        isActive = "1";
        date_input = new Date();
        this.setEventId(UUID.randomUUID().toString());
    }

    public Events() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public CaseDetails getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(CaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public Date getDate_input() {
        return date_input;
    }

    public void setDate_input(Date date_input) {
        this.date_input = date_input;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

}
