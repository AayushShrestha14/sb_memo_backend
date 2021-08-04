package com.sb.solutions.web.companyInfo.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.companyInfo.model.service.CompanyInfoService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;

@RestController
@RequestMapping(value = "/v1/companyInfo")
public class CompanyInfoController {

    private final CompanyInfoService companyInfoService;

    public CompanyInfoController(@Autowired CompanyInfoService companyInfoService) {
        this.companyInfoService = companyInfoService;
    }

    @PostMapping
    public ResponseEntity<?> saveCompanyInfo(@Valid @RequestBody CompanyInfo companyInfo) {
        return new RestResponseDto().successModel(companyInfoService.save(companyInfo));
    }

    @PostMapping("/list")
    public ResponseEntity<?> getPageable(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(companyInfoService.findAllPageable(searchDto, PaginationUtils
                .pageable(page, size)));
    }
}
