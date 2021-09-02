package com.sb.solutions.web.memoCategory.controller;

import com.sb.solutions.api.creditMemoCategory.entity.MemoCategory;
import com.sb.solutions.api.creditMemoCategory.service.MemoCategoryService;
import com.sb.solutions.core.controller.BaseController;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(MemoCategoryController.URL)
public class MemoCategoryController extends BaseController<MemoCategoryController, Long> {

    static final String URL= "/v1/memo-category";

    private final MemoCategoryService memoCategoryService;

    public MemoCategoryController(MemoCategoryService memoCategoryService) {
        this.memoCategoryService = memoCategoryService;
    }

    @Override
    protected Logger getLogger() {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody MemoCategory memoCategory){

        return new RestResponseDto().successModel(memoCategoryService.save(memoCategory));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll()
    {
        return new RestResponseDto().successModel(memoCategoryService.findAll());
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
                .successModel(memoCategoryService.findAllPageable(search, PaginationUtils.pageable(page, size)));
    }

}
