package com.wanted.backend.service;

import com.wanted.backend.dto.CompanyDto;
import com.wanted.backend.model.Company;
import com.wanted.backend.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Transactional
    public Company createCompany(CompanyDto request) {
        Company company = new Company();
        BeanUtils.copyProperties(request, company);
        return companyRepository.save(company);
    }
}
