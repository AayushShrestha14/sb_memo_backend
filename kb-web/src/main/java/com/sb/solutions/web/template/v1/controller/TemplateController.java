package com.sb.solutions.web.template.v1.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.template.entity.Template;
import com.sb.solutions.api.template.service.TemplateService;
import com.sb.solutions.core.controller.BaseController;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;

@RestController
@RequestMapping(TemplateController.URL)
public class TemplateController extends BaseController<Template, Long> {

    private final Logger logger = LoggerFactory.getLogger(TemplateController.class);

    static final String URL= "/v1/template";

    private final TemplateService service;

    public TemplateController(TemplateService service) {
        this.service = service;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Template template) {
        return new RestResponseDto().successModel(service.save(template));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(service.findOne(id));
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
}
