package org.dromara.daxpay.channel.union.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.annotation.BigField;
import cn.bootx.platform.core.util.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.channel.union.code.UnionPayCode;
import org.dromara.daxpay.channel.union.convert.UnionPayConfigConvert;
import org.dromara.daxpay.channel.union.result.UnionPayConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.entity.config.ChannelConfig;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 云闪付支付配置
 *
 * @author xxm
 * @since 2022/3/11
 */
@Data
@Accessors(chain = true)
public class UnionPayConfig implements ToResult<UnionPayConfigResult> {

    /** 主键 */
    private Long id;

    /** 云闪付商户号 */
    private String unionMachId;

    /** 是否启用, 只影响支付和退款操作 */
    private Boolean enable;

    /** 支付限额 */
    private BigDecimal limitAmount;

    /**
     * 商户收款账号
     */
    private String seller;

    /**
     * 签名类型
     * @see UnionPayCode.SignType
     */
    private String signType;

    /**
     * 是否为证书签名
     */
    private boolean certSign;

    /**
     * 应用私钥证书 字符串
     */
    @BigField
    private String keyPrivateCert;
    /**
     * 私钥证书对应的密码
     */
    private String keyPrivateCertPwd;

    /**
     * 中级证书
     */
    @BigField
    private String acpMiddleCert;
    /**
     * 根证书
     */
    private String acpRootCert;

    /** 是否沙箱环境 */
    private boolean sandbox;


    /** 商户AppId */
    private String appId;


    public Boolean getEnable() {
        return Objects.equals(true,enable);
    }

    /**
     * 转换为通道配置
     */
    public ChannelConfig toChannelConfig() {
        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.setId(this.getId());
        channelConfig.setOutMchNo(this.getUnionMachId());
        channelConfig.setAppId(this.getAppId());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setChannel(ChannelEnum.UNION_PAY.getCode());
        UnionPayConfig copy = UnionPayConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setAppId(null).setEnable(null).setUnionMachId(null).setAppId(null);
        String jsonStr = JsonUtil.toJsonStr(copy);
        channelConfig.setExt(jsonStr);
        return channelConfig;
    }

    /**
     * 从通道配置转换为支付宝配置
     */
    public static UnionPayConfig convertConfig(ChannelConfig channelConfig) {
        UnionPayConfig config = JsonUtil.toBean(channelConfig.getExt(), UnionPayConfig.class);
        config.setId(channelConfig.getId())
                .setUnionMachId(channelConfig.getOutMchNo())
                .setEnable(channelConfig.isEnable());
        return config;
    }

    /**
     * 转换
     */
    @Override
    public UnionPayConfigResult toResult() {
        return UnionPayConfigConvert.CONVERT.toResult(this);
    }
}
