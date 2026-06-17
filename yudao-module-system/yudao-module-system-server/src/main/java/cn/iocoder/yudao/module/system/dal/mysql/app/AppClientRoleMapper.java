package cn.iocoder.yudao.module.system.dal.mysql.app;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * App客户端角色 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AppClientRoleMapper extends BaseMapperX<AppClientRoleDO> {

    default AppClientRoleDO selectByName(String name) {
        return selectOne(AppClientRoleDO::getName, name);
    }

    default AppClientRoleDO selectByCode(String code) {
        return selectOne(AppClientRoleDO::getCode, code);
    }

    default List<AppClientRoleDO> selectListByIds(Collection<Long> ids) {
        return selectList(AppClientRoleDO::getId, ids);
    }

    default PageResult<AppClientRoleDO> selectPage(PageParam pageParam, String name, String code, Integer status) {
        return selectPage(pageParam, new LambdaQueryWrapperX<AppClientRoleDO>()
                .likeIfPresent(AppClientRoleDO::getName, name)
                .eqIfPresent(AppClientRoleDO::getCode, code)
                .eqIfPresent(AppClientRoleDO::getStatus, status)
                .orderByAsc(AppClientRoleDO::getSort));
    }

}
