package cn.iocoder.yudao.module.system.dal.mysql.app;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * App客户端用户角色关联 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AppClientUserRoleMapper extends BaseMapperX<AppClientUserRoleDO> {

    default List<AppClientUserRoleDO> selectListByUserId(Long userId) {
        return selectList(AppClientUserRoleDO::getUserId, userId);
    }

    default List<AppClientUserRoleDO> selectListByRoleIds(Collection<Long> roleIds) {
        return selectList(AppClientUserRoleDO::getRoleId, roleIds);
    }

    default void deleteByUserId(Long userId) {
        delete(AppClientUserRoleDO::getUserId, userId);
    }

    default void deleteByRoleId(Long roleId) {
        delete(AppClientUserRoleDO::getRoleId, roleId);
    }

}
