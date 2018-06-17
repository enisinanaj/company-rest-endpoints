package com.nlc.company.controllers;


import com.nlc.company.errors.CompanyNotFoundException;
import com.nlc.company.repositories.BeneficialOwnerRepository;
import com.nlc.company.repositories.CompanyRepository;
import com.nlc.company.resources.BeneficialOwner;
import com.nlc.company.resources.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/companies/{companyId}/beneficialOwners")
public class BeneficialOwnerController {

    private CompanyRepository companyRepository;
    private BeneficialOwnerRepository beneficialOwnerRepository;

    @Autowired
    public BeneficialOwnerController(CompanyRepository companyRepository, BeneficialOwnerRepository beneficialOwnerRepository) {
        this.companyRepository = companyRepository;
        this.beneficialOwnerRepository = beneficialOwnerRepository;
    }

    @GetMapping
    public Collection<BeneficialOwner> getAllBeneficialOwners(@PathVariable Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));

        return company.getBeneficialOwners();
    }

    @PostMapping
    public ResponseEntity createBeneficialOwner(@PathVariable Long companyId, @RequestBody BeneficialOwner beneficialOwner) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));

        beneficialOwner.setCompany(company);
        BeneficialOwner newBeneficialOwner = this.beneficialOwnerRepository.save(beneficialOwner);

        URI uri = getCurrentContextPath().path("/companies")
                .path("/{companyId}")
                .path("/beneficialOwners")
                .path("/{beneficialOwnerId}")
                .buildAndExpand(companyId, newBeneficialOwner.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    protected ServletUriComponentsBuilder getCurrentContextPath() {
        return ServletUriComponentsBuilder.fromCurrentContextPath();
    }

}
