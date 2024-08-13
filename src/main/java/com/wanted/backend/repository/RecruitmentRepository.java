package com.wanted.backend.repository;

import com.wanted.backend.model.Company;
import com.wanted.backend.model.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Optional<List<Recruitment>> findByCompany(Company company);
    @Query("SELECT r FROM Recruitment r WHERE LOWER(r.company.name) LIKE %:keyword% OR LOWER(r.position) LIKE %:keyword%")
    List<Recruitment> searchByKeyword(@Param("keyword") String keyword);

}
