package com.wanted.backend.controller;

import com.wanted.backend.dto.RecruitmentDto;
import com.wanted.backend.model.Recruitment;
import com.wanted.backend.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/wanted/api")
@RequiredArgsConstructor
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    @PostMapping("/recruitment")
    public Recruitment createRecruitment(@RequestBody RecruitmentDto request) {
        return recruitmentService.createRecruitment(request);
    }

    @GetMapping("/recruitments")
    public List<RecruitmentDto> getRecruitments(@RequestParam(name = "search", required = false) String keyword) {
        return recruitmentService.getRecruitments(keyword);
    }

    @GetMapping("/recruitment/{id}")
    public RecruitmentDto getRecruitment(@PathVariable("id") Long id) {
        return recruitmentService.getRecruitment(id);
    }

    @PatchMapping("/recruitment/{id}")
    public Recruitment updateRecruitment(@PathVariable("id") Long id, RecruitmentDto request) {
        return recruitmentService.updateRecruitment(id, request);
    }

    @DeleteMapping("/recruitment/{id}")
    public void deleteRecruitment(@PathVariable("id") Long recruitmentId) {
        recruitmentService.deleteRecruitment(recruitmentId);
    }


}