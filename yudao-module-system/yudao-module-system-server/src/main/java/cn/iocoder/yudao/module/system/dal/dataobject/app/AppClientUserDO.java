package cn.iocoder.yudao.module.system.dal.dataobject.app;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * App客户端用户 DO
 *
 * @author 芋道源码
 */
@TableName("app_client_user")
@KeySequence("app_client_user_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppClientUserDO extends BaseDO {

    /**
     * 关联的system_users用户ID(作为主键)
     */
    @TableId
    private Long userId;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 客户端密钥(AES加密存储)
     */
    private String clientSecret;
    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
