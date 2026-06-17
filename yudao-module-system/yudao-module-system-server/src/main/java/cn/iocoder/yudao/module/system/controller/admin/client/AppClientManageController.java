package cn.iocoder.yudao.module.system.controller.admin.client;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.app.client.vo.AppClientSecretRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserDO;
import cn.iocoder.yudao.module.system.service.app.AppClientPermissionService;
import cn.iocoder.yudao.module.system.service.app.AppClientUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * App客户端管理 Controller(Web端)
 *
 * @author 芋道源码
 */
@Tag(name = "管理后台 - App客户端管理")
@RestController
@RequestMapping("/system/app-client")
@Validated
public class AppClientManageController {

    @Resource
    private AppClientUserService AppClientUserService;

    @Resource
    private AppClientPermissionService AppClientPermissionService;

    @GetMapping("/page")
    @Operation(summary = "分页查询客户端用户")
    @PreAuthorize("@ss.hasPermission('system:app-client:query')")
    public CommonResult<PageResult<AppClientUserDO>> getAppClientUserPage(
            @Valid PageParam pageParam,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) Integer status) {
        PageResult<AppClientUserDO> pageResult = AppClientUserService.getAppClientUserPage(pageParam, username, status);
        // 隐藏敏感信息
        pageResult.getList().forEach(user -> user.setClientSecret(null));
        return success(pageResult);
    }

    @PostMapping("/generate-secret")
    @Operation(summary = "生成客户端密钥")
    @Parameter(name = "userId", description = "Web端用户ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:app-client:generate-secret')")
    public CommonResult<AppClientSecretRespVO> generateSecret(@RequestParam("userId") Long userId) {
        AppClientSecretRespVO respVO = AppClientUserService.generateClientSecret(userId);
        return success(respVO);
    }

    @PutMapping("/reset-secret")
    @Operation(summary = "重置客户端密钥")
    @Parameter(name = "userId", description = "Web端用户ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:app-client:reset-secret')")
    public CommonResult<AppClientSecretRespVO> resetSecret(@RequestParam("userId") Long userId) {
        AppClientSecretRespVO respVO = AppClientUserService.resetClientSecret(userId);
        return success(respVO);
    }

    @GetMapping("/get")
    @Operation(summary = "查询客户端用户信息")
    @Parameter(name = "userId", description = "Web端用户ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:app-client:query')")
    public CommonResult<AppClientUserDO> getClientUser(@RequestParam("userId") Long userId) {
        AppClientUserDO clientUser = AppClientUserService.getClientUserByUsername(
                AppClientUserService.getClientUser(userId) != null ? 
                AppClientUserService.getClientUser(userId).getUsername() : null
        );
        
        // 隐藏敏感信息
        if (clientUser != null) {
            clientUser.setClientSecret(null);
        }
        
        return success(clientUser);
    }

    @PostMapping("/assign-role")
    @Operation(summary = "分配客户端角色")
    @PreAuthorize("@ss.hasPermission('system:app-client:assign-role')")
    public CommonResult<Boolean> assignRole(
            @RequestParam("clientUserId") Long clientUserId,
            @RequestParam("roleIds") Set<Long> roleIds) {
        AppClientPermissionService.assignClientUserRole(clientUserId, roleIds);
        return success(true);
    }

}
