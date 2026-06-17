package cn.iocoder.yudao.module.system.dal.mysql.app;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * App客户端用户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AppClientUserMapper extends BaseMapperX<AppClientUserDO> {

    default AppClientUserDO selectByUsername(String username) {
        return selectOne(AppClientUserDO::getUsername, username);
    }

    default AppClientUserDO selectByUserId(Long userId) {
        return selectOne(AppClientUserDO::getUserId, userId);
    }

    default PageResult<AppClientUserDO> selectPage(PageParam pageParam, String username, Integer status) {
        return selectPage(pageParam, new LambdaQueryWrapperX<AppClientUserDO>()
                .likeIfPresent(AppClientUserDO::getUsername, username)
                .eqIfPresent(AppClientUserDO::getStatus, status)
                .orderByDesc(AppClientUserDO::getUserId));
    }

}
