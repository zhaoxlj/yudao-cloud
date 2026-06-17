package cn.iocoder.yudao.module.system.controller.admin.app;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.app.vo.menu.AppClientMenuRespVO;
import cn.iocoder.yudao.module.system.controller.admin.app.vo.menu.AppClientMenuSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;
import cn.iocoder.yudao.module.system.service.app.AppClientMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - App客户端菜单")
@RestController
@RequestMapping("/system/app-client/menu")
@Validated
public class AppClientMenuController {

    @Resource
    private AppClientMenuService appClientMenuService;

    @PostMapping("/create")
    @Operation(summary = "创建客户端菜单")
    @PreAuthorize("@ss.hasPermission('system:app-client-menu:create')")
    public CommonResult<Long> createMenu(@Valid @RequestBody AppClientMenuSaveReqVO createReqVO) {
        Long menuId = appClientMenuService.createMenu(createReqVO);
        return success(menuId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改客户端菜单")
    @PreAuthorize("@ss.hasPermission('system:app-client-menu:update')")
    public CommonResult<Boolean> updateMenu(@Valid @RequestBody AppClientMenuSaveReqVO updateReqVO) {
        appClientMenuService.updateMenu(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户端菜单")
    @Parameter(name = "id", description = "菜单编号", required= true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:app-client-menu:delete')")
    public CommonResult<Boolean> deleteMenu(@RequestParam("id") Long id) {
        appClientMenuService.deleteMenu(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除客户端菜单")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@ss.hasPermission('system:app-client-menu:delete')")
    public CommonResult<Boolean> deleteMenuList(@RequestParam("ids") List<Long> ids) {
        appClientMenuService.deleteMenuList(ids);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取客户端菜单列表", description = "用于【客户端菜单管理】界面")
    @PreAuthorize("@ss.hasPermission('system:app-client-menu:query')")
    public CommonResult<List<AppClientMenuRespVO>> getMenuList() {
        List<AppClientMenuDO> list = appClientMenuService.getMenuList();
        list.sort(Comparator.comparing(AppClientMenuDO::getSort));
        return success(BeanUtils.toBean(list, AppClientMenuRespVO.class));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取客户端菜单精简信息列表",
            description = "只包含被开启的菜单，用于【客户端角色分配菜单】功能的选项")
    public CommonResult<List<AppClientMenuRespVO>> getSimpleMenuList() {
        List<AppClientMenuDO> list = appClientMenuService.getMenuList();
        // 过滤禁用的菜单
        list = appClientMenuService.filterDisableMenus(list);
        list.sort(Comparator.comparing(AppClientMenuDO::getSort));
        return success(BeanUtils.toBean(list, AppClientMenuRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获取客户端菜单信息")
    @PreAuthorize("@ss.hasPermission('system:app-client-menu:query')")
    public CommonResult<AppClientMenuRespVO> getMenu(@RequestParam("id") Long id) {
        AppClientMenuDO menu = appClientMenuService.getMenu(id);
        return success(BeanUtils.toBean(menu, AppClientMenuRespVO.class));
    }

}
