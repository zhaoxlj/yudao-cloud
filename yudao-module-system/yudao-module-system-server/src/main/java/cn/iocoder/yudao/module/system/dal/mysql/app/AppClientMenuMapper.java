package cn.iocoder.yudao.module.system.dal.mysql.app;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * App客户端菜单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AppClientMenuMapper extends BaseMapperX<AppClientMenuDO> {

    default AppClientMenuDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(AppClientMenuDO::getParentId, parentId, AppClientMenuDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(AppClientMenuDO::getParentId, parentId);
    }

    default List<AppClientMenuDO> selectListByNameAndStatus(String name, Integer status) {
        return selectList(new LambdaQueryWrapperX<AppClientMenuDO>()
                .likeIfPresent(AppClientMenuDO::getName, name)
                .eqIfPresent(AppClientMenuDO::getStatus, status)
                .orderByAsc(AppClientMenuDO::getSort));
    }

    default List<AppClientMenuDO> selectListByIds(Collection<Long> ids) {
        return selectList(AppClientMenuDO::getId, ids);
    }

    default List<AppClientMenuDO> selectListByPermission(String permission) {
        return selectList(AppClientMenuDO::getPermission, permission);
    }

    default AppClientMenuDO selectByComponentName(String componentName) {
        return selectOne(AppClientMenuDO::getComponentName, componentName);
    }

}
