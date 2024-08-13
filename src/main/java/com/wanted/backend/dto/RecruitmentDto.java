package com.wanted.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wanted.backend.model.Recruitment;
import lombok.Data;

import java.sql.Struct;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecruitmentDto {

    private Long id;
    private Long companyId;
    private String companyName;
    private String country;
    private String city;
    private String position;
    private String reward;
    private String description;
    private String skills;
    private List<Long> otherRecruitment;

    public void setCompanyId(Long companyId) {
        if (this.companyId != null) {
            throw new UnsupportedOperationException("Foreign key cannot be modified once set");
        }
        this.companyId = companyId;
    }

}
