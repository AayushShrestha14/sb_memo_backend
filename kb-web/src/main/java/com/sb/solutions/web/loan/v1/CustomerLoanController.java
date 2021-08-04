package com.sb.solutions.web.loan.v1;

import java.text.ParseException;
import javax.validation.Valid;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.web.common.stage.dto.StageDto;
import com.sb.solutions.web.loan.v1.mapper.Mapper;

/**
 * @author Rujan Maharjan on 5/10/2019
 */

@RestController
@RequestMapping(CustomerLoanController.URL)
public class CustomerLoanController {

    static final String URL = "/v1/Loan-customer";

    private static final Logger logger = LoggerFactory.getLogger(CustomerLoanController.class);

    private final CustomerLoanService service;

    private final UserService userService;

    private final Mapper mapper;

    public CustomerLoanController(
        @Autowired CustomerLoanService service,
        @Autowired Mapper mapper,
        @Autowired UserService userService) {

        this.service = service;
        this.mapper = mapper;
        this.userService = userService;
    }

    @PostMapping(value = "/action")
    public ResponseEntity<?> loanAction(@Valid @RequestBody StageDto actionDto) {
        final CustomerLoan c = mapper
            .actionMapper(actionDto, service.findOne(actionDto.getCustomerLoanId()),
                userService.getAuthenticated());
        service.sendForwardBackwardLoan(c);
        return new RestResponseDto().successModel(actionDto);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CustomerLoan customerLoan,
        BindingResult bindingResult) {

        logger.debug("saving Customer Loan {}", customerLoan);

        return new RestResponseDto().successModel(service.save(customerLoan));
    }

    @PostMapping("/close-renew-customer-loan")
    public ResponseEntity<?> closeRenew(@Valid @RequestBody CustomerLoan customerLoan) {

        logger.debug("saving Customer Loan {}", customerLoan);

        return new RestResponseDto().successModel(service.renewCloseEntity(customerLoan));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(service.findOne(id));
    }


    @GetMapping("/count/{id}")
    public ResponseEntity<?> getCustomerLoanCountByLoanCategory(@PathVariable("id") Long loanCategoryId, @RequestParam Long customerId) {
        return new RestResponseDto().successModel(service.getCustomerLoanCountByType(loanCategoryId, customerId));
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<?> delByIdRoleMaker(@PathVariable("id") Long id) {
        logger.info("deleting Customer Loan {}", id);
        return new RestResponseDto()
            .successModel(service.delCustomerLoan(id));
    }

    @PostMapping("/status")
    public ResponseEntity<?> getfirst5ByDocStatus(@RequestBody CustomerLoan customerLoan) {
        logger.debug("getByDocStatus Customer Loan {}", customerLoan);
        return new RestResponseDto().successModel(
            service.getFirst5CustomerLoanByDocumentStatus(customerLoan.getDocumentStatus()));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAllByPagination(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(service.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(value = "/statusCount")
    public ResponseEntity<?> countLoanStatus() {
        return new RestResponseDto().successModel(service.statusCount());
    }

    @GetMapping(value = "/proposed-amount")
    public ResponseEntity<?> getProposedAmount(@RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) throws ParseException {
        return new RestResponseDto().successModel(service.proposedAmount(startDate, endDate));
    }

    @GetMapping(value = "/loan-amount/{id}")
    public ResponseEntity<?> getProposedAmountByBranch(@PathVariable Long id,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) throws ParseException {
        return new RestResponseDto().successModel(service.proposedAmountByBranch(id, startDate,
            endDate));
    }

    @GetMapping(value = "/searchByCitizenship/{number}")
    public ResponseEntity<?> getLoansByCitizenship(
        @PathVariable("number") String citizenshipNumber) {
        logger.info("GET:/searchByCitizenship/{}", citizenshipNumber);
        return new RestResponseDto()
            .successModel(service.getByCitizenshipNumber(citizenshipNumber));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/catalogue")
    public ResponseEntity<?> getCatalogues(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(service.getCatalogues(searchDto, PaginationUtils.pageable(page, size)));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/committee-pull")
    public ResponseEntity<?> getCommitteePull(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(
                service.getCommitteePull(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(path = "/stats")
    public final ResponseEntity<?> getStats(@RequestParam(value = "branchId") Long branchId,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) throws ParseException {
        logger.debug("REST request to get the statistical data about the loans.");
        return new RestResponseDto().successModel(mapper.toBarchartDto(service.getStats(branchId,
            startDate, endDate)));
    }

    @GetMapping(path = "/check-user-customer-loan/{id}")
    public ResponseEntity<?> chkUserContainCustomerLoan(@PathVariable Long id) {
        logger.debug("REST request to get the check data about the user.");
        return new RestResponseDto().successModel(service.chkUserContainCustomerLoan(id));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/csv")
    public ResponseEntity<?> csv(@RequestBody Object searchDto) {
        return new RestResponseDto().successModel(service.csv(searchDto));
    }

    @PostMapping(value = "/list/ignore-stage")
    public ResponseEntity<?> getAllList(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(service
            .getCustomerLoanListIgnoreStage(searchDto, PaginationUtils.pageable(page, size)));
    }

    @PostMapping("/all")
    public ResponseEntity<?> findAllWithSearch(@RequestBody Object search) {
        return new RestResponseDto().successModel(service.findAll(search));
    }

}
