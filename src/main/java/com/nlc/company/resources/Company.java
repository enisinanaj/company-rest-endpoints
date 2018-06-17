package com.nlc.company.resources;

import javax.persistence.*;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private String city;
    private String country;
    private String email;
    private String phone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<BeneficialOwner> beneficialOwners;

    public Company() {
    }

    public Company(final Long id, final String name,
                   final String address, final String city, final String country,
                   final String email, final String phone, final List<BeneficialOwner> beneficialOwners) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phone = phone;
        this.beneficialOwners = beneficialOwners;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<BeneficialOwner> getBeneficialOwners() {
        return beneficialOwners;
    }

    public void setBeneficialOwners(List<BeneficialOwner> beneficialOwners) {
        this.beneficialOwners = beneficialOwners;
    }
}
