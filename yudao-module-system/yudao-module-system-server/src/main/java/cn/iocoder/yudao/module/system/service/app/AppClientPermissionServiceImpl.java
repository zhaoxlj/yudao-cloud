package cn.iocoder.yudao.module.system.service.app;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleMenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserRoleDO;
import cn.iocoder.yudao.module.system.dal.mysql.app.AppClientRoleMapper;
import cn.iocoder.yudao.module.system.dal.mysql.app.AppClientRoleMenuMapper;
import cn.iocoder.yudao.module.system.dal.mysql.app.AppClientUserRoleMapper;
import cn.iocoder.yudao.module.system.service.permission.MenuService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.CLIENT_ROLE_NOT_EXISTS;

/**
 * App客户端权限 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class AppClientPermissionServiceImpl implements AppClientPermissionService {

    @Resource
    private AppClientUserRoleMapper AppClientUserRoleMapper;

    @Resource
    private AppClientRoleMenuMapper AppClientRoleMenuMapper;

    @Resource
    private AppClientRoleMapper AppClientRoleMapper;

    @Resource
    private AppClientMenuService appClientMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignClientUserRole(Long clientUserId, Set<Long> roleIds) {
        // 1. 删除原有的用户角色关联
        AppClientUserRoleMapper.deleteByUserId(clientUserId);

        // 2. 如果角色列表为空,直接返回
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }

        // 3. 校验角色是否存在
        List<AppClientRoleDO> roles = AppClientRoleMapper.selectListByIds(roleIds);
        if (roles.size() != roleIds.size()) {
            throw exception(CLIENT_ROLE_NOT_EXISTS);
        }

        // 4. 创建新的用户角色关联
        List<AppClientUserRoleDO> userRoles = roleIds.stream()
                .map(roleId -> AppClientUserRoleDO.builder()
                        .userId(clientUserId)
                        .roleId(roleId)
                        .build())
                .collect(Collectors.toList());
        AppClientUserRoleMapper.insertBatch(userRoles);
    }

    @Override
    public Set<Long> getClientUserRoleIds(Long clientUserId) {
        List<AppClientUserRoleDO> userRoles = AppClientUserRoleMapper.selectListByUserId(clientUserId);
        return convertSet(userRoles, AppClientUserRoleDO::getRoleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignClientRoleMenu(Long roleId, Set<Long> menuIds) {
        // 1. 删除原有的角色菜单关联
        AppClientRoleMenuMapper.deleteByRoleId(roleId);

        // 2. 如果菜单列表为空,直接返回
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }

        // 3. 创建新的角色菜单关联
        List<AppClientRoleMenuDO> roleMenus = menuIds.stream()
                .map(menuId -> AppClientRoleMenuDO.builder()
                        .roleId(roleId)
                        .menuId(menuId)
                        .build())
                .collect(Collectors.toList());
        AppClientRoleMenuMapper.insertBatch(roleMenus);
    }

    @Override
    public Set<Long> getClientRoleMenuIds(Long roleId) {
        List<AppClientRoleMenuDO> roleMenus = AppClientRoleMenuMapper.selectListByRoleId(roleId);
        return convertSet(roleMenus, AppClientRoleMenuDO::getMenuId);
    }

    @Override
    public List<AppClientMenuDO> getClientMenuList(Long clientUserId) {
        // 1. 获取用户的角色ID列表
        Set<Long> roleIds = getClientUserRoleIds(clientUserId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        // 2. 获取角色的菜单ID列表
        List<AppClientRoleMenuDO> roleMenus = AppClientRoleMenuMapper.selectListByRoleIds(roleIds);
        Set<Long> menuIds = convertSet(roleMenus, AppClientRoleMenuDO::getMenuId);
        if (CollUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }

        // 3. 获取菜单列表
        List<AppClientMenuDO> menuList = appClientMenuService.getMenuList(menuIds);
        
        // 4. 过滤禁用的菜单
        return appClientMenuService.filterDisableMenus(menuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processClientUserDeleted(Long clientUserId) {
        // 删除用户角色关联
        AppClientUserRoleMapper.deleteByUserId(clientUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processClientRoleDeleted(Long roleId) {
        // 删除角色菜单关联
        AppClientRoleMenuMapper.deleteByRoleId(roleId);
        
        // 删除用户角色关联
        AppClientUserRoleMapper.deleteByRoleId(roleId);
    }

}
