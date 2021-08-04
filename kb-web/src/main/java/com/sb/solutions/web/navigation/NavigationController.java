package com.sb.solutions.web.navigation;

import java.util.List;

import com.sb.solutions.core.constant.AppConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.rolePermissionRight.entity.RolePermissionRights;
import com.sb.solutions.api.rolePermissionRight.service.RolePermissionRightService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.web.navigation.mapper.MenuMapper;

import static com.sb.solutions.core.constant.AppConstant.*;

/**
 * @author Rujan Maharjan on 3/28/2019
 */
@RestController
@RequestMapping("/v1")
public class NavigationController {

    private final RolePermissionRightService rolePermissionRightService;

    private final UserService userService;

    private final MenuMapper menuMapper;

    public NavigationController(
        RolePermissionRightService rolePermissionRightService,
        UserService userService, MenuMapper menuMapper) {
        this.rolePermissionRightService = rolePermissionRightService;
        this.userService = userService;
        this.menuMapper = menuMapper;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/nav")
    public ResponseEntity<?> getNav() {
        User u = userService.getAuthenticated();
        return new RestResponseDto()
            .successModel(rolePermissionRightService.getByRoleId(u.getRole().getId()));
    }

    @GetMapping("/menu")
    public ResponseEntity<?> getMenu() {
        User u = userService.getAuthenticated();
        List<RolePermissionRights> rolePermissionRights = rolePermissionRightService
            .getByRoleId(u.getRole().getId());
        /*
        Beside admin user, no other user can set question for eligibility
        below logic is to guard
         */
        boolean hasEligibilityPermission = rolePermissionRights.stream()
                .anyMatch(r -> r.getPermission().getId() == ELIGIBILITY_PERMISSION);
        if (!u.getRole().getRoleName().equals(AppConstant.ADMIN_ROLE) && hasEligibilityPermission) {
            rolePermissionRights.stream().forEach(role -> {
                if (role.getPermission().getId() == ELIGIBILITY_PERMISSION) {
                    role.getPermission().getSubNavs()
                            .removeIf(subNav -> subNav.getId() == ELIGIBILITY_PERMISSION_SUBNAV_QUESTION
                                    || subNav.getId() == ELIGIBILITY_PERMISSION_SUBNAV_GENERAL_QUESTION);
                }
            });

        }
        return new RestResponseDto().successModel(menuMapper.menuDtoList(rolePermissionRights));
    }
}
