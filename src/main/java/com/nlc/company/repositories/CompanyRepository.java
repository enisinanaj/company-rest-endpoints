package com.nlc.company.repositories;

import com.nlc.company.resources.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
