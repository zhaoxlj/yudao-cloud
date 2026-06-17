package cn.iocoder.yudao.module.system.service.app;

import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * App客户端权限 Service 接口
 *
 * @author 芋道源码
 */
public interface AppClientPermissionService {

    /**
     * 分配客户端用户角色
     *
     * @param clientUserId 客户端用户ID
     * @param roleIds 角色ID集合
     */
    void assignClientUserRole(Long clientUserId, Set<Long> roleIds);

    /**
     * 获取客户端用户的角色ID列表
     *
     * @param clientUserId 客户端用户ID
     * @return 角色ID列表
     */
    Set<Long> getClientUserRoleIds(Long clientUserId);

    /**
     * 分配客户端角色菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID集合
     */
    void assignClientRoleMenu(Long roleId, Set<Long> menuIds);

    /**
     * 获取客户端角色的菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    Set<Long> getClientRoleMenuIds(Long roleId);

    /**
     * 获取客户端用户的菜单权限列表
     *
     * @param clientUserId 客户端用户ID
     * @return 菜单列表
     */
    List<AppClientMenuDO> getClientMenuList(Long clientUserId);

    /**
     * 处理客户端用户删除时,删除关联授权数据
     *
     * @param clientUserId 客户端用户ID
     */
    void processClientUserDeleted(Long clientUserId);

    /**
     * 处理客户端角色删除时,删除关联授权数据
     *
     * @param roleId 角色ID
     */
    void processClientRoleDeleted(Long roleId);

}
