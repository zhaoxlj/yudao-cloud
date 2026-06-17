package cn.iocoder.yudao.module.system.convert.app;

import cn.iocoder.yudao.module.system.controller.app.client.vo.AppClientPermissionInfoRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * App客户端 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface AppClientConvert {

    AppClientConvert INSTANCE = Mappers.getMapper(AppClientConvert.class);

    /**
     * 组装权限信息
     *
     * @param user 客户端用户
     * @param roles 角色列表
     * @param menus 菜单列表
     * @return 权限信息
     */
    default AppClientPermissionInfoRespVO convert(AppClientUserDO user,
                                                    List<AppClientRoleDO> roles,
                                                    List<AppClientMenuDO> menus) {
        AppClientPermissionInfoRespVO respVO = new AppClientPermissionInfoRespVO();
        respVO.setUser(user);
        respVO.setRoles(roles);
        respVO.setMenus(menus);
        return respVO;
    }

}
