package com.wanted.backend.controller;

import com.wanted.backend.dto.CompanyDto;
import com.wanted.backend.model.Company;
import com.wanted.backend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/wanted/api")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/company")
    public Company createCompany(CompanyDto request) {
        return companyService.createCompany(request);
    }
}
