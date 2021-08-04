package com.sb.solutions.web.user;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy;
import com.sb.solutions.api.rolePermissionRight.service.MemoRoleHierarchyService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.web.user.dto.MemoRoleHierarchyDto;
import com.sb.solutions.web.user.mapper.MemoRoleHierarchyMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("v1/memoRole-hierarchy")
public class MemoRoleHierarchyController {


    private final MemoRoleHierarchyMapper memoRoleHierarchyMapper;
    private final MemoRoleHierarchyService memoRoleHierarchyService;
    private final UserService userService;

    public MemoRoleHierarchyController(@Autowired MemoRoleHierarchyMapper memoRoleHierarchyMapper,
                                       @Autowired MemoRoleHierarchyService memoRoleHierarchyService,
                                       @Autowired  UserService userService) {
        this.memoRoleHierarchyMapper = memoRoleHierarchyMapper;
        this.memoRoleHierarchyService = memoRoleHierarchyService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody List<MemoRoleHierarchyDto> memoRoleHierarchyDtos){
        final List<MemoRoleHierarchy> memoRoleHierarchies =
                memoRoleHierarchyService.saveList(memoRoleHierarchyMapper.mapDtosToEntities(memoRoleHierarchyDtos));
        if(memoRoleHierarchies.isEmpty()){
            return new RestResponseDto().failureModel("Error occurs" + memoRoleHierarchyDtos);
        }

        return new RestResponseDto().successModel(memoRoleHierarchyMapper.mapEntitiesToDtos(memoRoleHierarchies));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return new RestResponseDto().successModel(
                memoRoleHierarchyMapper.mapEntitiesToDtos(memoRoleHierarchyService.findAll()));
    }

    @GetMapping("/getForward")
    public ResponseEntity<?> getRoleHierarchyListPerRoleForward() {
        User u = userService.getAuthenticated();
        return new RestResponseDto().successModel(
                memoRoleHierarchyService.roleHierarchyByCurrentRoleForward(u.getRole().getId()));
    }

    @GetMapping("/getBackward")
    public ResponseEntity<?> getRoleHierarchyListPerRoleBackward() {
        User u = userService.getAuthenticated();
        return new RestResponseDto().successModel(
                memoRoleHierarchyService.roleHierarchyByCurrentRoleBackward(u.getRole().getId()));
    }
}
