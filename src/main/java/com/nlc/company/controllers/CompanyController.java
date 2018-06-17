package com.nlc.company.controllers;

import com.nlc.company.errors.CompanyNotFoundException;
import com.nlc.company.repositories.CompanyRepository;
import com.nlc.company.resources.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    CompanyRepository companyRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostMapping
    public ResponseEntity createNewCompany(@RequestBody Company companyData) {
        Company result = this.companyRepository.save(companyData);

        URI uri = getCurrentContextPath().path("/companies").path("/{id}").buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping
    public Company updateCompany(@RequestBody Company company) {
        return null;
    }

    @GetMapping
    public Collection<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    protected ServletUriComponentsBuilder getCurrentContextPath() {
        return ServletUriComponentsBuilder.fromCurrentContextPath();
    }

    @GetMapping(path="/{companyId}")
    public Company getCompanyById(@PathVariable Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
    }
}
