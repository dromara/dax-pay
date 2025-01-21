package cn.bootx.platform.iam.result.captcha;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 验证码数据
 *
 * @author xxm
 * @since 2021/8/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "验证码数据")
public class CaptchaDataResult {

    @Schema(description = "验证码标识KEY")
    private String captchaKey;

    @Schema(description = "验证码base64数据")
    private String captchaData;

}
