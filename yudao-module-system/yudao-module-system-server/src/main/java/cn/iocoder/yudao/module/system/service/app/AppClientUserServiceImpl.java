package cn.iocoder.yudao.module.system.service.app;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.system.controller.app.client.vo.AppClientSecretRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.app.AppClientUserDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.app.AppClientUserMapper;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * App客户端用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class AppClientUserServiceImpl implements AppClientUserService {

    @Resource
    private AppClientUserMapper AppClientUserMapper;

    @Resource
    private AdminUserService adminUserService;

    @Value("${mybatis-plus.encryptor.password}")
    private String encryptorPassword;

    private AES getAes() {
        return SecureUtil.aes(encryptorPassword.getBytes());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppClientSecretRespVO generateClientSecret(Long userId) {
        // 1. 校验Web端用户是否存在
        AdminUserDO adminUser = adminUserService.getUser(userId);
        if (adminUser == null) {
            throw exception(USER_NOT_EXISTS);
        }

        // 2. 检查是否已生成过客户端密钥
        AppClientUserDO existUser = AppClientUserMapper.selectByUserId(userId);
        if (existUser != null) {
            throw exception(CLIENT_USER_ALREADY_EXISTS);
        }

        // 3. 生成随机密钥
        String rawSecret = generateRandomSecret();

        // 4. 创建客户端用户记录
        AppClientUserDO clientUser = AppClientUserDO.builder()
                .userId(userId)
                .username(adminUser.getUsername())
                .nickname(adminUser.getNickname())
                .clientSecret(getAes().encryptBase64(rawSecret))
                .status(CommonStatusEnum.ENABLE.getStatus())
                .build();
        AppClientUserMapper.insert(clientUser);

        // 5. 返回结果
        return AppClientSecretRespVO.builder()
                .userId(userId)
                .username(adminUser.getUsername())
                .clientSecret(rawSecret)
                .generateTime(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppClientSecretRespVO resetClientSecret(Long userId) {
        // 1. 检查客户端用户是否存在
        AppClientUserDO clientUser = AppClientUserMapper.selectByUserId(userId);
        if (clientUser == null) {
            throw exception(CLIENT_USER_NOT_EXISTS);
        }

        // 2. 生成新密钥
        String rawSecret = generateRandomSecret();

        // 3. 更新密钥
        AppClientUserDO updateObj = new AppClientUserDO();
        updateObj.setUserId(userId);
        updateObj.setClientSecret(getAes().encryptBase64(rawSecret));
        AppClientUserMapper.updateById(updateObj);

        // 4. 返回结果
        return AppClientSecretRespVO.builder()
                .userId(userId)
                .username(clientUser.getUsername())
                .clientSecret(rawSecret)
                .generateTime(LocalDateTime.now())
                .build();
    }

    @Override
    public AppClientUserDO getClientUserByUsername(String username) {
        return AppClientUserMapper.selectByUsername(username);
    }

    @Override
    public boolean isClientSecretMatch(String rawSecret, String encodedSecret) {
        String decryptedSecret = getAes().decryptStr(encodedSecret);
        return rawSecret.equals(decryptedSecret);
    }

    @Override
    public void updateClientUserLogin(Long userId, String loginIp) {
        // 客户端用户表不再记录登录IP和时间
        // 该方法保留空实现,用于后续扩展
    }

    @Override
    public AppClientUserDO getClientUser(Long id) {
        return AppClientUserMapper.selectById(id);
    }

    @Override
    public AppClientSecretRespVO getClientSecretInfo(Long userId) {
        AppClientUserDO clientUser = AppClientUserMapper.selectById(userId);
        if (clientUser == null || clientUser.getClientSecret() == null) {
            return null;
        }
        String plainSecret = getAes().decryptStr(clientUser.getClientSecret());
        return AppClientSecretRespVO.builder()
                .userId(clientUser.getUserId())
                .username(clientUser.getUsername())
                .clientSecret(plainSecret)
                .generateTime(clientUser.getCreateTime())
                .build();
    }

    @Override
    public PageResult<AppClientUserDO> getAppClientUserPage(PageParam pageParam, String username, Integer status) {
        return AppClientUserMapper.selectPage(pageParam, username, status);
    }

    /**
     * 生成随机密钥
     *
     * @return 随机密钥字符串
     */
    private String generateRandomSecret() {
        // 使用UUID生成随机密钥,去掉横线
        return UUID.randomUUID().toString().replace("-", "");
    }

}
