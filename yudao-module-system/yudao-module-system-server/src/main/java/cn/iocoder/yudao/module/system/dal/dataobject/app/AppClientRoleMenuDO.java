package cn.iocoder.yudao.module.system.dal.dataobject.app;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * App客户端角色菜单关联 DO
 *
 * @author 芋道源码
 */
@TableName("app_client_role_menu")
@KeySequence("app_client_role_menu_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppClientRoleMenuDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID(复用system_menu)
     */
    private Long menuId;

}
