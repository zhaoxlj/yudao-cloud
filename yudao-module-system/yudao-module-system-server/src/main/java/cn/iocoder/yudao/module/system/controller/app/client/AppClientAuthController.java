package cn.iocoder.yudao.module.system.controller.app.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.config.SecurityProperties;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.controller.app.client.vo.AppClientLoginReqVO;
import cn.iocoder.yudao.module.system.controller.app.client.vo.AppClientLoginRespVO;
import cn.iocoder.yudao.module.system.controller.app.client.vo.AppClientPermissionInfoRespVO;
import cn.iocoder.yudao.module.system.convert.app.AppClientConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserDO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.yudao.module.system.dal.mysql.app.AppClientRoleMapper;
import cn.iocoder.yudao.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import cn.iocoder.yudao.module.system.service.logger.LoginLogService;
import cn.iocoder.yudao.module.system.service.oauth2.OAuth2TokenService;
import cn.iocoder.yudao.module.system.service.permission.MenuService;
import cn.iocoder.yudao.module.system.service.app.AppClientPermissionService;
import cn.iocoder.yudao.module.system.service.app.AppClientUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * App客户端认证 Controller
 *
 * @author 芋道源码
 */
@Tag(name = "App客户端 - 认证")
@RestController
@RequestMapping("/system/app-client/auth")
@Validated
@Slf4j
public class AppClientAuthController {

    @Resource
    private AppClientUserService AppClientUserService;

    @Resource
    private AppClientPermissionService AppClientPermissionService;

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private MenuService menuService;

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private AppClientRoleMapper AppClientRoleMapper;

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "客户端登录")
    public CommonResult<AppClientLoginRespVO> login(@RequestBody @Valid AppClientLoginReqVO reqVO) {
        // 1. 验证用户名和密钥
        AppClientUserDO clientUser = AppClientUserService.getClientUserByUsername(reqVO.getUsername());
        if (clientUser == null) {
            throw exception(AUTH_CLIENT_LOGIN_BAD_CREDENTIALS);
        }
        
        if (!AppClientUserService.isClientSecretMatch(reqVO.getClientSecret(), clientUser.getClientSecret())) {
            throw exception(AUTH_CLIENT_LOGIN_BAD_CREDENTIALS);
        }

        // 2. 校验是否禁用
        if (CommonStatusEnum.isDisable(clientUser.getStatus())) {
            throw exception(AUTH_CLIENT_USER_DISABLED);
        }

        // 3. 更新登录信息
        AppClientUserService.updateClientUserLogin(clientUser.getUserId(), getClientIP());

        // 4. 创建Token令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                clientUser.getUserId(),
                UserTypeEnum.MEMBER.getValue(),
                OAuth2ClientConstants.CLIENT_ID_APP_CLIENT,
                null
        );

        // 5. 构建返回结果
        AppClientLoginRespVO respVO = BeanUtils.toBean(accessTokenDO, AppClientLoginRespVO.class)
                .setUserId(clientUser.getUserId());

        return success(respVO);
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "客户端登出")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            oauth2TokenService.removeAccessToken(token);
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    public CommonResult<AppClientLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        var accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_APP_CLIENT);
        return success(BeanUtils.toBean(accessTokenDO, AppClientLoginRespVO.class));
    }

    @GetMapping("/get-permission-info")
    @Operation(summary = "获取客户端用户的权限信息")
    public CommonResult<AppClientPermissionInfoRespVO> getPermissionInfo() {
        Long clientUserId = SecurityFrameworkUtils.getLoginUserId();
        
        // 1. 获取客户端用户信息
        AppClientUserDO user = AppClientUserService.getClientUser(clientUserId);
        if (user == null) {
            return success(null);
        }

        // 2. 获取用户角色列表
        Set<Long> roleIds = AppClientPermissionService.getClientUserRoleIds(clientUserId);
        List<AppClientRoleDO> roles = Collections.emptyList();
        if (CollUtil.isNotEmpty(roleIds)) {
            roles = AppClientRoleMapper.selectListByIds(roleIds);
        }

        // 3. 获取菜单列表
        List<AppClientMenuDO> menuList = AppClientPermissionService.getClientMenuList(clientUserId);

        // 4. 拼接结果返回
        return success(AppClientConvert.INSTANCE.convert(user, roles, menuList));
    }

}
