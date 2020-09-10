/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_tax")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "EntityTax.findAll", query = "SELECT t FROM EntityTax t")})
public class EntityTax implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "tax_id", unique = true)
    private String taxId;
    @Column(name = "ptkp")
    private Double ptkp;
    @Column(name = "tax_status", length = 10)
    private String taxStatus;

    public EntityTax(String taxId, Double ptkp, String taxStatus) {
        this.taxId = taxId;
        this.ptkp = ptkp;
        this.taxStatus = taxStatus;
    }

}
