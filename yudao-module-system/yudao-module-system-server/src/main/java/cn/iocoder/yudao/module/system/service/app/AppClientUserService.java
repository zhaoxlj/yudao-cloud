package cn.iocoder.yudao.module.system.service.app;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.app.client.vo.AppClientSecretRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserDO;
import jakarta.validation.Valid;

/**
 * App客户端用户 Service 接口
 *
 * @author 芋道源码
 */
public interface AppClientUserService {

    /**
     * 为Web端用户生成客户端密钥
     *
     * @param userId Web端用户ID
     * @return 密钥生成结果(包含明文密钥)
     */
    AppClientSecretRespVO generateClientSecret(Long userId);

    /**
     * 重置客户端密钥
     *
     * @param userId Web端用户ID
     * @return 密钥生成结果(包含明文密钥)
     */
    AppClientSecretRespVO resetClientSecret(Long userId);

    /**
     * 根据用户名查询客户端用户
     *
     * @param username 用户名
     * @return 客户端用户
     */
    AppClientUserDO getClientUserByUsername(String username);

    /**
     * 验证客户端密钥
     *
     * @param rawSecret 未加密的密钥
     * @param encodedSecret 加密后的密钥
     * @return 是否匹配
     */
    boolean isClientSecretMatch(String rawSecret, String encodedSecret);

    /**
     * 更新客户端用户登录信息
     *
     * @param id 客户端用户ID
     * @param loginIp 登录IP
     */
    void updateClientUserLogin(Long id, String loginIp);

    /**
     * 获取客户端用户
     *
     * @param id 客户端用户ID
     * @return 客户端用户
     */
    AppClientUserDO getClientUser(Long id);

    /**
     * 获取客户端用户的密钥信息（AES解密后的明文）
     *
     * @param userId Web端用户ID
     * @return 密钥信息，未生成时返回 null
     */
    AppClientSecretRespVO getClientSecretInfo(Long userId);

    /**
     * 分页查询客户端用户
     *
     * @param pageParam 分页参数
     * @param username 用户名(可选)
     * @param status 状态(可选)
     * @return 客户端用户分页结果
     */
    PageResult<AppClientUserDO> getAppClientUserPage(PageParam pageParam, String username, Integer status);

}
