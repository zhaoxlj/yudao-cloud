package cn.iocoder.yudao.module.system.controller.app.client.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "App客户端 - 登录请求 VO")
@Data
public class AppClientLoginReqVO {

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    @NotEmpty(message = "用户账号不能为空")
    private String username;

    @Schema(description = "客户端密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "xxx")
    @NotEmpty(message = "客户端密钥不能为空")
    @Size(min = 4, max = 64, message = "客户端密钥长度为 4-64 位")
    private String clientSecret;

}
