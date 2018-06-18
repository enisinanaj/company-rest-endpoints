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

    @RequestMapping(path="/{companyId}", method=RequestMethod.PATCH)
    public Company updateCompany(@PathVariable Long companyId, @RequestBody Company company) {
        Company oldCompany = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));

        updateCompanyName(company, oldCompany);
        updateCity(company, oldCompany);
        updateAddress(company, oldCompany);
        updateCountry(company, oldCompany);
        updateEmail(company, oldCompany);
        updatePhone(company, oldCompany);

        companyRepository.saveAndFlush(oldCompany);

        return oldCompany;
    }

    private void updateCity(Company company, Company oldCompany) {
        if (company.getCity() != null) {
            oldCompany.setCity(company.getCity());
        }
    }

    private void updateCompanyName(Company company, Company oldCompany) {
        if (company.getName() != null) {
            oldCompany.setName(company.getName());
        }
    }

    private void updateCountry(Company company, Company oldCompany) {
        if (company.getCountry() != null) {
            oldCompany.setCountry(company.getCountry());
        }
    }

    private void updateAddress(Company company, Company oldCompany) {
        if (company.getAddress() != null) {
            oldCompany.setAddress(company.getAddress());
        }
    }

    private void updateEmail(Company company, Company oldCompany) {
        if (company.getEmail() != null) {
            oldCompany.setEmail(company.getEmail());
        }
    }

    private void updatePhone(Company company, Company oldCompany) {
        if (company.getPhone() != null) {
            oldCompany.setPhone(company.getPhone());
        }
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
