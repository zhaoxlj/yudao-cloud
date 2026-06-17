package cn.iocoder.yudao.module.system.dal.mysql.app;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleMenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * App客户端角色菜单关联 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AppClientRoleMenuMapper extends BaseMapperX<AppClientRoleMenuDO> {

    default List<AppClientRoleMenuDO> selectListByRoleId(Long roleId) {
        return selectList(AppClientRoleMenuDO::getRoleId, roleId);
    }

    default List<AppClientRoleMenuDO> selectListByRoleIds(Collection<Long> roleIds) {
        return selectList(AppClientRoleMenuDO::getRoleId, roleIds);
    }

    default void deleteByRoleId(Long roleId) {
        delete(AppClientRoleMenuDO::getRoleId, roleId);
    }

    default void deleteByRoleIds(Collection<Long> roleIds) {
        delete(AppClientRoleMenuDO::getRoleId, roleIds);
    }

}
