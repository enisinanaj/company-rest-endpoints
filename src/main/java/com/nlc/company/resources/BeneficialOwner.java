package com.nlc.company.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BeneficialOwner {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne
    private Company company;

    public BeneficialOwner() {
    }

    public BeneficialOwner(final Long id, final String name, final Company company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Company getCompany() {
        return company;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
