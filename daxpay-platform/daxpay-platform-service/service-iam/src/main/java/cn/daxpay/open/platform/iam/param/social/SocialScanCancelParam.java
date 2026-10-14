package cn.daxpay.open.platform.iam.param.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 取消扫码参数(绑定/登录共用)
///
/// 调用方是 H5 发起页确认页: H5 的 http 封装会把 POST 的 params 并入 JSON body,
/// 故以 body 接参(同 [SocialScanExchangeParam]); 票据为空或不可取消时空操作, 不做校验拦截.
@Data
@Accessors(chain = true)
@Schema(title = "取消扫码参数")
public class SocialScanCancelParam {

    /// 扫码票据(绑定/登录)
    @Schema(description = "扫码票据")
    private String ticket;
}
