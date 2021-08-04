package com.sb.solutions.web.creditmemo.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.creditmemo.entity.CreditMemoType;
import com.sb.solutions.api.creditmemo.service.CreditMemoTypeService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@RestController
@RequestMapping(CreditMemoTypeController.URL)
public class CreditMemoTypeController {

    static final String URL = "/v1/credit-memo-type";

    private final CreditMemoTypeService service;

    public CreditMemoTypeController(CreditMemoTypeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CreditMemoType creditMemoType) {
        return new RestResponseDto().successModel(service.save(creditMemoType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return new RestResponseDto().successModel(service.findOne(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(service.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @PostMapping("/list")
    public ResponseEntity<?> getPageable(@RequestBody Object search, @RequestParam("page") int page,
        @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(service.findAllPageable(search, PaginationUtils.pageable(page, size)));
    }

    @GetMapping("/getByMemoRoles")
    public ResponseEntity<?> getByMemoRoles(){
        return new RestResponseDto().successModel(service.findByMemoRoles());
    }
}
