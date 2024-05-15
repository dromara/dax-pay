package cn.bootx.platform.starter.wechat.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2022/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(title = "登录二维码")
public class WeChatLoginQrCode {

    @Schema(description = "key")
    private String qrCodeKey;

    @Schema(description = "qrCode")
    private String qrCodeUrl;

}
