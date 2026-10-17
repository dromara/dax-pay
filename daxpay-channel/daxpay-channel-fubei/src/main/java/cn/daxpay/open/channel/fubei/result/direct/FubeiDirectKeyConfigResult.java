package cn.daxpay.open.channel.fubei.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 付呗直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class FubeiDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 付呗开放平台应用ID(app_id)
    private String appId;

    /// 付呗商户ID(merchant_id)
    private String merchantId;

    /// 付呗门店ID(store_id)
    private String storeId;

    /// 接口密钥(app_secret, 脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String appSecret;

    /// 接口密钥是否已配置
    private boolean appSecretConfigured;
}
