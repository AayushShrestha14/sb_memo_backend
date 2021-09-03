package com.sb.solutions.web.document.v1;

import javax.validation.Valid;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.document.service.DocumentService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.web.document.v1.dto.DocumentDto;
import com.sb.solutions.web.document.v1.mapper.DocumentMapper;


@RestController
@RequestMapping(value = "/v1/document")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    public DocumentController(
        @Autowired DocumentService documentService,
        @Autowired DocumentMapper documentMapper
    ) {
        this.documentService = documentService;
        this.documentMapper = documentMapper;
    }

    @PostMapping
    public ResponseEntity<?> addDocument(@Valid @RequestBody DocumentDto documentDto) {

        final Document doc = documentService.save(documentMapper.mapDtoToEntity(documentDto));

        if (doc != null) {
            return new RestResponseDto().successModel(doc);
        } else {
            return new RestResponseDto().failureModel("Error Occurred");
        }
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAllByPagination(@RequestBody SearchDto searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            documentService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(value = "/statusCount")
    public ResponseEntity<?> getCount() {
        return new RestResponseDto().successModel(documentService.documentStatusCount());
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(documentService.findAll());
    }

    @GetMapping(value = "/status/{statusValue}")
    public ResponseEntity<?> getAllByStatus(@PathVariable String statusValue) {
        return new RestResponseDto().successModel(documentService.getByStatus(statusValue));
    }

}
