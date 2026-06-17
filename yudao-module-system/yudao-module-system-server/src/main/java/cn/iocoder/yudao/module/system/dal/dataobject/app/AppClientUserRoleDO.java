package cn.iocoder.yudao.module.system.dal.dataobject.app;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * App客户端用户角色关联 DO
 *
 * @author 芋道源码
 */
@TableName("app_client_user_role")
@KeySequence("app_client_user_role_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppClientUserRoleDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 用户ID(关联app_client_user.user_id)
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;

}
