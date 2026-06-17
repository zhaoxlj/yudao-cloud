package cn.iocoder.yudao.module.system.controller.app.client.vo;

import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientMenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientRoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "App客户端 - 权限信息响应 VO")
@Data
public class AppClientPermissionInfoRespVO {

    @Schema(description = "用户信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private AppClientUserDO user;

    @Schema(description = "角色列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AppClientRoleDO> roles;

    @Schema(description = "菜单列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AppClientMenuDO> menus;

}
