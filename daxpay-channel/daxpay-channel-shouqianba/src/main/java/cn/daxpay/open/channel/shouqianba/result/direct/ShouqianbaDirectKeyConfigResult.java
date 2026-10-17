package cn.daxpay.open.channel.shouqianba.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收钱吧直连密钥配置返回结果
@Data
@Accessors(chain = true)
public class ShouqianbaDirectKeyConfigResult {

    /// 通道商户号
    private String channelMchNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 收钱吧终端号(terminal_sn)
    private String terminalSn;

    /// 收钱吧终端密钥(脱敏返回)
    @SensitiveInfo(front = 2, end = 2)
    private String terminalKey;

    /// 操作员
    private String operator;

    /// 终端密钥是否已配置
    private boolean terminalKeyConfigured;
}
