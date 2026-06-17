package cn.iocoder.yudao.module.system.controller.admin.client;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleDO;
import cn.iocoder.yudao.module.system.service.permission.MenuService;
import cn.iocoder.yudao.module.system.service.app.AppClientPermissionService;
import cn.iocoder.yudao.module.system.dal.mysql.app.AppClientRoleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * App客户端角色管理 Controller
 *
 * @author 芋道源码
 */
@Tag(name = "管理后台 - App客户端角色管理")
@RestController
@RequestMapping("/system/app-client/role")
@Validated
public class AppClientRoleController {

    @Resource
    private AppClientRoleMapper AppClientRoleMapper;

    @Resource
    private AppClientPermissionService AppClientPermissionService;

    @Resource
    private MenuService menuService;

    @PostMapping("/create")
    @Operation(summary = "创建客户端角色")
    @PreAuthorize("@ss.hasPermission('system:rpa-role:create')")
    public CommonResult<Long> createRole(@Valid @RequestBody AppClientRoleDO role) {
        // 校验角色名称是否重复
        if (AppClientRoleMapper.selectByName(role.getName()) != null) {
            throw exception(CLIENT_ROLE_NAME_DUPLICATE, role.getName());
        }
        // 校验角色标识是否重复
        if (AppClientRoleMapper.selectByCode(role.getCode()) != null) {
            throw exception(CLIENT_ROLE_CODE_DUPLICATE, role.getCode());
        }

        AppClientRoleMapper.insert(role);
        return success(role.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户端角色")
    @PreAuthorize("@ss.hasPermission('system:rpa-role:update')")
    public CommonResult<Boolean> updateRole(@Valid @RequestBody AppClientRoleDO role) {
        // 校验角色是否存在
        if (AppClientRoleMapper.selectById(role.getId()) == null) {
            throw exception(CLIENT_ROLE_NOT_EXISTS);
        }

        AppClientRoleMapper.updateById(role);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户端角色")
    @Parameter(name = "id", description = "角色ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:rpa-role:delete')")
    public CommonResult<Boolean> deleteRole(@RequestParam("id") Long id) {
        // 校验角色是否存在
        if (AppClientRoleMapper.selectById(id) == null) {
            throw exception(CLIENT_ROLE_NOT_EXISTS);
        }

        // 删除角色
        AppClientRoleMapper.deleteById(id);
        
        // 处理关联数据
        AppClientPermissionService.processClientRoleDeleted(id);
        
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取客户端角色列表")
    @PreAuthorize("@ss.hasPermission('system:rpa-role:query')")
    public CommonResult<List<AppClientRoleDO>> getRoleList() {
        List<AppClientRoleDO> list = AppClientRoleMapper.selectList();
        return success(list);
    }

    @PostMapping("/assign-menu")
    @Operation(summary = "分配角色菜单权限")
    @PreAuthorize("@ss.hasPermission('system:rpa-role:assign-menu')")
    public CommonResult<Boolean> assignMenu(
            @RequestParam("roleId") Long roleId,
            @RequestParam("menuIds") Set<Long> menuIds) {
        AppClientPermissionService.assignClientRoleMenu(roleId, menuIds);
        return success(true);
    }

    @GetMapping("/get-menus")
    @Operation(summary = "获取角色的菜单权限")
    @Parameter(name = "roleId", description = "角色ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:rpa-role:query')")
    public CommonResult<List<MenuDO>> getRoleMenus(@RequestParam("roleId") Long roleId) {
        Set<Long> menuIds = AppClientPermissionService.getClientRoleMenuIds(roleId);
        if (menuIds.isEmpty()) {
            return success(List.of());
        }
        
        List<MenuDO> menuList = menuService.getMenuList(menuIds);
        return success(menuList);
    }

}
