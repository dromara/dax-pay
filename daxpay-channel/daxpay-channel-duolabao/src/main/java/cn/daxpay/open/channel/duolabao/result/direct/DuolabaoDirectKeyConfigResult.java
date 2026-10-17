package cn.daxpay.open.channel.duolabao.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 哆啦宝直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class DuolabaoDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 商户编号(customerNum)
    private String customerNum;

    /// 店铺编号(shopNum)
    private String shopNum;

    /// 代理商编号(agentNum)
    private String agentNum;

    /// 公钥(accessKey, 脱敏返回)
    @SensitiveInfo(front = 4, end = 4)
    private String accessKey;

    /// 私钥(secretKey, 脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String secretKey;

    /// 公钥是否已配置
    private boolean accessKeyConfigured;

    /// 私钥是否已配置
    private boolean secretKeyConfigured;
}
