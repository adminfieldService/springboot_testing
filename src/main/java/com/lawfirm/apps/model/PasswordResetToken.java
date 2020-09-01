/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_password_reset_token")
public class PasswordResetToken implements Serializable {

    private static final int EXPIRATION = 60 * 24; //24 hours
    @Id
    @SequenceGenerator(name = "reset_token_seq", sequenceName = "reset_token_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_token_seq")
    @Column(name = "id_token")
    private Long idToken;

    private String token;

    @OneToOne(targetEntity = Employee.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id_employee")
    private Employee employee;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Column(name = "expiry_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    public PasswordResetToken(Long idToken, String token, Employee employee, Date expiryDate) {
        this.idToken = idToken;
        this.token = token;
        this.employee = employee;
        this.expiryDate = expiryDate;
    }

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}
