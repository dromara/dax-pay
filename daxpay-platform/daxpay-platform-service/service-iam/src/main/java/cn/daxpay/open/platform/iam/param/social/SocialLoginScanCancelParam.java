package cn.daxpay.open.platform.iam.param.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 取消扫码登录参数
///
/// 调用方是 H5 发起页确认页: H5 的 http 封装会把 POST 的 params 并入 JSON body,
/// 故以 body 接参(同 [SocialScanExchangeParam]); 票据为空或不可取消时空操作, 不做校验拦截.
@Data
@Accessors(chain = true)
@Schema(title = "取消扫码登录参数")
public class SocialLoginScanCancelParam {

    /// 扫码登录票据
    @Schema(description = "扫码登录票据")
    private String ticket;
}
