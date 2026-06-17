package cn.iocoder.yudao.module.system.controller.app.client.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "App客户端 - 密钥生成响应 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppClientSecretRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long userId;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String username;

    @Schema(description = "客户端密钥(明文，仅首次生成返回)", requiredMode = Schema.RequiredMode.REQUIRED, example = "xxx")
    private String clientSecret;

    @Schema(description = "生成时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime generateTime;

}
