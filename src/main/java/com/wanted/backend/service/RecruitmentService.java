package com.wanted.backend.service;

import com.wanted.backend.dto.CompanyDto;
import com.wanted.backend.dto.RecruitmentDto;
import com.wanted.backend.model.Company;
import com.wanted.backend.model.Recruitment;
import com.wanted.backend.repository.CompanyRepository;
import com.wanted.backend.repository.RecruitmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final CompanyRepository companyRepository;

    /**
     * 새로운 채용 공고를 생성합니다.
     *
     * @param recruitmentDto 생성할 채용 공고의 데이터가 담긴 DTO
     * @return 생성된 채용 공고 엔티티
     */
    @Transactional
    public Recruitment createRecruitment(RecruitmentDto recruitmentDto) {
        // 회사 ID로 회사 정보를 조회
        Optional<Company> company = companyRepository.findById(recruitmentDto.getCompanyId());
        if (company.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Not Found");
        }

        // 새로운 Recruitment 객체를 생성하고 DTO에서 데이터를 복사
        Recruitment recruitmentToCreate = new Recruitment();
        BeanUtils.copyProperties(recruitmentDto, recruitmentToCreate);
        recruitmentToCreate.setCompany(company.get());

        // 채용 공고를 저장하고 반환
        return recruitmentRepository.save(recruitmentToCreate);
    }

    /**
     * 기존 채용 공고를 업데이트합니다.
     *
     * @param id 채용 공고의 ID
     * @param request 업데이트할 데이터가 담긴 DTO
     * @return 업데이트된 채용 공고 엔티티
     */
    @Transactional
    public Recruitment updateRecruitment(Long id, RecruitmentDto request) {
        // ID로 기존 채용 공고를 조회
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if (optionalRecruitment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recruitment not present in the database");
        }


        Recruitment recruitment = optionalRecruitment.get();

        // DTO에서 null이 아닌 값만 복사하여 기존 채용 공고를 업데이트
        copyNonNullProperties(request, recruitment);

        // 업데이트된 채용 공고를 저장하고 반환
        return recruitmentRepository.save(recruitment);
    }

    /**
     * DTO에서 null이 아닌 속성만 복사하여 엔티티를 업데이트합니다.
     *
     * @param source DTO에서 가져온 데이터
     * @param target 업데이트할 엔티티
     */
    private void copyNonNullProperties(RecruitmentDto source, Recruitment target) {
        if (source.getCountry() != null) {
            target.setCountry(source.getCountry());
        }
        if (source.getCity() != null) {
            target.setCity(source.getCity());
        }
        if (source.getPosition() != null) {
            target.setPosition(source.getPosition());
        }
        if (source.getReward() != null) {
            target.setReward(source.getReward());
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
        if (source.getSkills() != null) {
            target.setSkills(source.getSkills());
        }
    }

    /**
     * 주어진 ID의 채용 공고를 삭제합니다.
     *
     * @param id 삭제할 채용 공고의 ID
     */
    @Transactional
    public void deleteRecruitment(Long id) {
        // ID로 채용 공고를 조회
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if (optionalRecruitment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recruitment not present in the database");
        }

        // 채용 공고를 삭제
        recruitmentRepository.deleteById(id);
    }

    /**
     * 주어진 검색 키워드로 채용 공고를 조회하여 RecruitmentDto 리스트로 반환합니다.
     *
     * @param keyword 검색 키워드 (null일 경우 빈 문자열로 처리)
     * @return 검색된 채용 공고 리스트를 담은 RecruitmentDto 리스트
     */
    public List<RecruitmentDto> getRecruitments(String keyword) {
        // 키워드가 null일 경우 빈 문자열로 설정하여 검색 처리
        String searchKeyword = (keyword != null) ? keyword.toLowerCase() : "";

        // 키워드를 사용하여 데이터베이스에서 모집 정보 조회
        List<Recruitment> recruitmentList = recruitmentRepository.searchByKeyword(searchKeyword);

        // 조회된 Recruitment 객체를 RecruitmentDto 객체로 변환하여 리스트로 반환
        return recruitmentList.stream()
                // Recruitment 객체를 RecruitmentDto 객체로 변환
                .map(recruitment -> {
                    RecruitmentDto dto = new RecruitmentDto();
                    // BeanUtils를 사용하여 Recruitment 객체의 속성을 RecruitmentDto 객체로 복사
                    BeanUtils.copyProperties(recruitment, dto);
                    return dto;
                })
                // 변환된 RecruitmentDto 객체들을 리스트로 수집
                .collect(Collectors.toList());
    }

    /**
     * 주어진 ID의 채용 공고를 조회하고 해당 채용 공고에 대한 상세 정보를 반환합니다.
     *
     * @param id 조회할 채용 공고의 ID
     * @return 채용 공고의 상세 정보가 담긴 DTO
     */
    public RecruitmentDto getRecruitment(Long id) {
        // ID로 채용 공고를 조회
        Optional<Recruitment> recruitmentToRead = recruitmentRepository.findById(id);
        if (recruitmentToRead.isPresent()) {
            Recruitment recruitment = recruitmentToRead.get();
            RecruitmentDto recruitmentToReturn = new RecruitmentDto();

            // Recruitment에서 RecruitmentDto로 프로퍼티 복사
            BeanUtils.copyProperties(recruitment, recruitmentToReturn);
            recruitmentToReturn.setCompanyName(recruitment.getCompany().getName());

            // 같은 회사의 다른 채용 공고를 가져오기
            Optional<List<Recruitment>> sameCompanyList = recruitmentRepository.findByCompany(recruitment.getCompany());

            // 같은 회사의 채용 공고 중에서 현재 조회된 ID를 제외한 ID 리스트를 필터링
            List<Long> idList = sameCompanyList
                    .orElseGet(Collections::emptyList)  // Optional이 비어있는 경우 빈 리스트 반환
                    .stream()
                    .filter(r -> !r.getId().equals(id))  // 현재 ID와 다른 Recruitment 필터링
                    .map(Recruitment::getId)  // Recruitment 객체에서 ID만 추출
                    .collect(Collectors.toList());

            // DTO에 같은 회사의 다른 채용 공고 id 리스트를 설정
            recruitmentToReturn.setOtherRecruitment(idList);

            return recruitmentToReturn;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find any recruitment under given ID");
    }
}
