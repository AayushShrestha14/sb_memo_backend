package com.sb.solutions.web.creditmemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sb.solutions.api.user.entity.User;
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
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.creditmemo.entity.CreditMemo;
import com.sb.solutions.api.creditmemo.entity.CreditMemoDocument;
import com.sb.solutions.api.creditmemo.service.CreditMemoService;
import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.controller.BaseController;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.web.common.stage.dto.StageDto;
import com.sb.solutions.web.creditmemo.mapper.CreditMemoStageMapper;

/**
 * @author Elvin Shrestha on 7/7/2020
 */
@RestController
@RequestMapping(CreditMemoController.URL)
public class CreditMemoController extends BaseController<CreditMemo, Long> {

    private final Logger logger = LoggerFactory.getLogger(CreditMemoController.class);

    static final String URL = "/v1/credit-memo";

    private final CreditMemoService service;
    private final CreditMemoStageMapper mapper;
    private final UserService userService;

    public CreditMemoController(
        CreditMemoService service,
        CreditMemoStageMapper mapper,
        UserService userService
    ) {
        this.service = service;
        this.mapper = mapper;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CreditMemo creditMemo) {
        User user = userService.getAuthenticated();
        if(user.getUsername() != null){
            StringBuilder fromUserName = new StringBuilder();
            fromUserName.append(user.getName()).append(" ").append("(").append(user.getRole().getRoleName()).append(")");
            creditMemo.setFromUser(fromUserName.toString());
        }
        user.getBranch().forEach(branch->{
            creditMemo.setBranchName(branch.getName());
        });
        StringBuilder userFlow = new StringBuilder();
        creditMemo.getUserFlow().forEach( flow -> {
            if(flow.getId() != creditMemo.getUserFlow().get(creditMemo.getUserFlow().size()-1).getId()) {
                userFlow.append(flow.getName()).append("/");
            }
        });
        if(creditMemo.getUserFlow().size() != 1){
            userFlow.replace(userFlow.length()-1, userFlow.length(),".");
        }
        creditMemo.setToUser(userFlow.toString());
        return new RestResponseDto().successModel(service.save(creditMemo));
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @PostMapping("/loanAssociated")
    public ResponseEntity<?> getPageableForLoanAssociated(@RequestBody Object search, @RequestParam("page") int page,
                                         @RequestParam("size") int size) {
        return new RestResponseDto()
                .successModel(service.findAllPageableForLoanAssociated(search, PaginationUtils.pageable(page, size)));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @PostMapping("/NotloanAssociated")
    public ResponseEntity<?> getPageableForNotLoanAssociated(@RequestBody Object search, @RequestParam("page") int page,
                                                          @RequestParam("size") int size) {
        return new RestResponseDto()
                .successModel(service.findAllMemoTypePageableWithFilter(search, PaginationUtils.pageable(page,size)));

    }

    @PostMapping("/action")
    public ResponseEntity<?> action(@RequestBody StageDto stageDto) {
        final CreditMemo creditMemo = mapper
            .action(stageDto, service.findOne(stageDto.getCreditMemoId()));
        return new RestResponseDto().successModel(service.action(creditMemo));
    }

    @GetMapping(value = "/statusCount")
    public ResponseEntity<?> countStatus() {
        return new RestResponseDto().successModel(service.statusCount());
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(
        @RequestParam("file") MultipartFile multipartFile,
        @RequestParam("documentId") Long documentId,
        @RequestParam("customerName") String customerName,
        @RequestParam("citizenshipNumber") String citizenshipNumber,
        @RequestParam("customerLoanId") String customerLoanId,
        @RequestParam("memoType") String memoType,
        @RequestParam("documentName") String documentName,
        @RequestParam("DocumentType") String docType
    ) {
        StringBuilder pathName =new StringBuilder(UploadDir.initialDocument);
        pathName.append(docType).append("/");
        CreditMemoDocument creditMemoDocument = new CreditMemoDocument();
        Document document = new Document();
        document.setId(documentId);
        creditMemoDocument.setDocument(document);
//        String branchName = userService.getAuthenticated().getBranch().get(0).getName().replace(" ", "_");
        String path = new PathBuilder(pathName.toString())
            .withCustomerName(customerName)
            .withCitizenship(citizenshipNumber)
            .withCustomerLoanId(customerLoanId)
            .withMemoType(memoType)
            .isJsonPath(false)
            .buildCreditMemo();
        ResponseEntity<?> responseEntity = FileUploadUtils
            .uploadFile(multipartFile, path, documentName);
        creditMemoDocument.setPath(((RestResponseDto) Objects
            .requireNonNull(responseEntity.getBody())).getDetail().toString());
        return new RestResponseDto().successModel(creditMemoDocument);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
