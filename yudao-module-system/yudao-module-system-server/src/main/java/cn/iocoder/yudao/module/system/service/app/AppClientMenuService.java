package cn.iocoder.yudao.module.system.service.app;

import cn.iocoder.yudao.module.system.controller.admin.app.vo.menu.AppClientMenuSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;

import java.util.Collection;
import java.util.List;

/**
 * App客户端菜单 Service 接口
 *
 * @author 芋道源码
 */
public interface AppClientMenuService {

    /**
     * 创建菜单
     *
     * @param createReqVO 菜单信息
     * @return 创建出来的菜单编号
     */
    Long createMenu(AppClientMenuSaveReqVO createReqVO);

    /**
     * 更新菜单
     *
     * @param updateReqVO 菜单信息
     */
    void updateMenu(AppClientMenuSaveReqVO updateReqVO);

    /**
     * 删除菜单
     *
     * @param id 菜单编号
     */
    void deleteMenu(Long id);

    /**
     * 批量删除菜单
     *
     * @param ids 菜单编号数组
     */
    void deleteMenuList(List<Long> ids);

    /**
     * 获得所有菜单列表
     *
     * @return 菜单列表
     */
    List<AppClientMenuDO> getMenuList();

    /**
     * 过滤掉关闭的菜单及其子菜单
     *
     * @param list 菜单列表
     * @return 过滤后的菜单列表
     */
    List<AppClientMenuDO> filterDisableMenus(List<AppClientMenuDO> list);

    /**
     * 获得菜单
     *
     * @param id 菜单编号
     * @return 菜单
     */
    AppClientMenuDO getMenu(Long id);

    /**
     * 获得菜单数组
     *
     * @param ids 菜单编号数组
     * @return 菜单数组
     */
    List<AppClientMenuDO> getMenuList(Collection<Long> ids);

    /**
     * 判断是否有子菜单
     *
     * @param id 菜单编号
     * @return 是否有子菜单
     */
    boolean hasChildMenu(Long id);

}
