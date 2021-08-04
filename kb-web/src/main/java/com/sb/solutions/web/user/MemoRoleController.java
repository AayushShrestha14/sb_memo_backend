package com.sb.solutions.web.user;

import com.sb.solutions.api.rolePermissionRight.entity.MemoRole;
import com.sb.solutions.api.rolePermissionRight.entity.MemoRoleHierarchy;
import com.sb.solutions.api.rolePermissionRight.service.MemoRoleHierarchyService;
import com.sb.solutions.api.rolePermissionRight.service.MemoRoleService;
import com.sb.solutions.api.rolePermissionRight.service.RoleHierarchyService;
import com.sb.solutions.api.rolePermissionRight.service.RolePermissionRightService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.List;

@Controller
@RequestMapping("/v1/memoRole")
public class MemoRoleController {

    private final MemoRoleService memoRoleService;
    private final MemoRoleHierarchyService memoRoleHierarchyService;

    private final RolePermissionRightService rolePermissionRightService;

    public MemoRoleController(@Autowired MemoRoleService memoRoleService,
                             @Autowired MemoRoleHierarchyService roleHierarchyService,
                              RolePermissionRightService rolePermissionRightService) {
        this.memoRoleService = memoRoleService;
        this.memoRoleHierarchyService = roleHierarchyService;
        this.rolePermissionRightService = rolePermissionRightService;
    }

    @PostMapping
    public ResponseEntity<?> saveMemoRole(@RequestBody MemoRole memoRole){
        final MemoRole r = memoRoleService.save(memoRole);
        if (r == null){
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            if(memoRole.getId() != null && !memoRole.getRoleType().equals(RoleType.ADMIN)){
                List<MemoRoleHierarchy>  memoRoleHierarchies = memoRoleHierarchyService.findAll();
                MemoRoleHierarchy roleHierarchy = new MemoRoleHierarchy();
                roleHierarchy.setRole(r);
                roleHierarchy.setRoleOrder(memoRoleHierarchies.size() + 1L);
                memoRoleHierarchyService.save(roleHierarchy);
            }
        }
        return new RestResponseDto().successModel(r.getRoleName());
    }

    @GetMapping("/all")
    @Produces("application/json")
    public ResponseEntity<?> getMemoRole(){

        return new RestResponseDto().successModel(memoRoleService.findAll());
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody MemoRole memoRole){
        final MemoRole r= memoRoleService.save(memoRole);

        return new RestResponseDto().successModel(r.getRoleName());
    }

    @GetMapping("/{id}")
    @Produces("application/json")
    public ResponseEntity<?> getById(@PathVariable("id") Long Id){

        return new RestResponseDto().successModel(memoRoleService.findOne(Id));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveRole(){
        return new RestResponseDto().successModel(memoRoleService.activeMemoRole());
    }
    
    @GetMapping("/maker")
public ResponseEntity<?> isMaker(){
    return new RestResponseDto().successModel(memoRoleService.isMaker());
    }

    @GetMapping("/getApproval")
    public ResponseEntity<?> getApproval(){
        return new RestResponseDto().successModel(memoRoleService.getApproval());
    }
}
