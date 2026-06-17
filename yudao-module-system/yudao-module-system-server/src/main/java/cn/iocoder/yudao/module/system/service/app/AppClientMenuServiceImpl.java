package cn.iocoder.yudao.module.system.service.app;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.app.vo.menu.AppClientMenuSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;
import cn.iocoder.yudao.module.system.dal.mysql.app.AppClientMenuMapper;
import cn.iocoder.yudao.module.system.enums.permission.MenuTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO.ID_ROOT;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * App客户端菜单 Service 实现
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class AppClientMenuServiceImpl implements AppClientMenuService {

    @Resource
    private AppClientMenuMapper appClientMenuMapper;

    @Override
    public Long createMenu(AppClientMenuSaveReqVO createReqVO) {
        // 校验父菜单存在
        validateParentMenu(createReqVO.getParentId(), null);
        // 校验菜单名称
        validateMenuName(createReqVO.getParentId(), createReqVO.getName(), null);
        validateMenuComponentName(createReqVO.getComponentName(), null);

        // 插入数据库
        AppClientMenuDO menu = BeanUtils.toBean(createReqVO, AppClientMenuDO.class);
        initMenuProperty(menu);
        appClientMenuMapper.insert(menu);
        // 返回
        return menu.getId();
    }

    @Override
    public void updateMenu(AppClientMenuSaveReqVO updateReqVO) {
        // 校验更新的菜单是否存在
        if (appClientMenuMapper.selectById(updateReqVO.getId()) == null) {
            throw exception(CLIENT_MENU_NOT_EXISTS);
        }
        // 校验父菜单存在
        validateParentMenu(updateReqVO.getParentId(), updateReqVO.getId());
        // 校验菜单名称
        validateMenuName(updateReqVO.getParentId(), updateReqVO.getName(), updateReqVO.getId());
        validateMenuComponentName(updateReqVO.getComponentName(), updateReqVO.getId());

        // 更新到数据库
        AppClientMenuDO updateObj = BeanUtils.toBean(updateReqVO, AppClientMenuDO.class);
        initMenuProperty(updateObj);
        appClientMenuMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        // 校验是否还有子菜单
        if (appClientMenuMapper.selectCountByParentId(id) > 0) {
            throw exception(CLIENT_MENU_EXISTS_CHILDREN);
        }
        // 校验删除的菜单是否存在
        if (appClientMenuMapper.selectById(id) == null) {
            throw exception(CLIENT_MENU_NOT_EXISTS);
        }
        // 标记删除
        appClientMenuMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuList(List<Long> ids) {
        // 校验是否还有子菜单
        ids.forEach(id -> {
            if (appClientMenuMapper.selectCountByParentId(id) > 0) {
                throw exception(CLIENT_MENU_EXISTS_CHILDREN);
            }
        });

        // 标记删除
        appClientMenuMapper.deleteByIds(ids);
    }

    @Override
    public List<AppClientMenuDO> getMenuList() {
        return appClientMenuMapper.selectList();
    }

    @Override
    public List<AppClientMenuDO> filterDisableMenus(List<AppClientMenuDO> menuList) {
        if (CollUtil.isEmpty(menuList)){
            return Collections.emptyList();
        }
        Map<Long, AppClientMenuDO> menuMap = convertMap(menuList, AppClientMenuDO::getId);

        // 遍历 menu 菜单，查找不是禁用的菜单，添加到 enabledMenus 结果
        List<AppClientMenuDO> enabledMenus = new ArrayList<>();
        Set<Long> disabledMenuCache = new HashSet<>(); // 存下递归搜索过被禁用的菜单，防止重复的搜索
        for (AppClientMenuDO menu : menuList) {
            if (isMenuDisabled(menu, menuMap, disabledMenuCache)) {
                continue;
            }
            enabledMenus.add(menu);
        }
        return enabledMenus;
    }

    private boolean isMenuDisabled(AppClientMenuDO menu, Map<Long, AppClientMenuDO> menuMap, Set<Long> disabledMenuCache) {
        // 如果该菜单本身被禁用，则返回禁用
        if (CommonStatusEnum.isDisable(menu.getStatus())) {
            disabledMenuCache.add(menu.getId());
            return true;
        }
        // 递归校验父菜单是否禁用
        if (menu.getParentId() != null && !menu.getParentId().equals(ID_ROOT)) {
            AppClientMenuDO parentMenu = menuMap.get(menu.getParentId());
            if (parentMenu != null && isMenuDisabled(parentMenu, menuMap, disabledMenuCache)) {
                // 如果父菜单被禁用，则将该菜单也标记为禁用
                disabledMenuCache.add(menu.getId());
                return true;
            }
        }
        // 未被禁用
        return false;
    }

    @Override
    public AppClientMenuDO getMenu(Long id) {
        return appClientMenuMapper.selectById(id);
    }

    @Override
    public List<AppClientMenuDO> getMenuList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return appClientMenuMapper.selectListByIds(ids);
    }

    @Override
    public boolean hasChildMenu(Long id) {
        return appClientMenuMapper.selectCountByParentId(id) > 0;
    }

    // ==================== 私有校验方法 ====================

    private void validateParentMenu(Long parentId, Long menuId) {
        if (parentId == null || ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父菜单
        if (parentId.equals(menuId)) {
            throw exception(CLIENT_MENU_PARENT_ERROR, menuId);
        }
        // 2. 父菜单不存在
        AppClientMenuDO parent = appClientMenuMapper.selectById(parentId);
        if (parent == null) {
            throw exception(CLIENT_MENU_PARENT_NOT_EXISTS);
        }
        // 3. 父菜单不能是按钮
        if (MenuTypeEnum.BUTTON.getType().equals(parent.getType())) {
            throw exception(CLIENT_MENU_PARENT_NOT_MENU);
        }
    }

    private void validateMenuName(Long parentId, String name, Long menuId) {
        AppClientMenuDO menu = appClientMenuMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        // 如果 menuId 为空，说明新增情况
        if (menuId == null) {
            throw exception(CLIENT_MENU_NAME_DUPLICATE, name);
        }
        // 如果不匹配，说明重名了
        if (!menu.getId().equals(menuId)) {
            throw exception(CLIENT_MENU_NAME_DUPLICATE, name);
        }
    }

    private void validateMenuComponentName(String componentName, Long menuId) {
        if (StrUtil.isBlank(componentName)) {
            return;
        }
        AppClientMenuDO menu = appClientMenuMapper.selectByComponentName(componentName);
        if (menu == null) {
            return;
        }
        if (menuId == null || !menu.getId().equals(menuId)) {
            throw exception(CLIENT_MENU_COMPONENT_NAME_DUPLICATE, componentName);
        }
    }

    private void initMenuProperty(AppClientMenuDO menu) {
        // 菜单为目录时，隐藏
        if (MenuTypeEnum.DIR.getType().equals(menu.getType())) {
            menu.setVisible(false);
        }
    }

}
