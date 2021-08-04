package com.sb.solutions.web.approvallimit.v1;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;
import com.sb.solutions.api.approvallimit.service.ApprovalLimitService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.utils.PaginationUtils;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/approval-limit")
public class ApprovalLimitController {

    private final ApprovalLimitService approvalLimitService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> addApprovalLimit(@RequestBody ApprovalLimit approvalLimit) {
        final ApprovalLimit saved = approvalLimitService.save(approvalLimit);

        if (saved != null) {
            return new RestResponseDto().successModel(saved);
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
    public ResponseEntity<?> getAll(@RequestBody SearchDto searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(approvalLimitService
            .findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }


    @GetMapping(value = "/{id}/role")
    public ResponseEntity<?> getByRoleAndLoan(@PathVariable Long id) {
        return new RestResponseDto().successModel(approvalLimitService
            .getByRoleAndLoan(userService.getAuthenticated().getRole().getId(), id,
                LoanApprovalType.PERSONAL_TYPE));
    }

}
