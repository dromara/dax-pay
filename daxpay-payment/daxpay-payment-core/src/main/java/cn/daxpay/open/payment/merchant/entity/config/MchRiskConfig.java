package cn.daxpay.open.payment.merchant.entity.config;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.config.MchRiskConfigConvert;
import cn.daxpay.open.payment.merchant.result.config.MchRiskConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户风控配置
///
/// 商户级风控配置, 与商户 1:1。本期承载地理围栏 opt-in 开关, 预留扩展其他商户级风控。
/// 围栏两级门控: 平台总闸 [cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPaySecurityConfig#getGeoFenceEnabled]
/// AND 本表 geoFenceEnabled(商户级 opt-in) 同时开启才生效; 围栏策略取平台全局配置
/// [cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPaySecurityConfig#getGeoFenceStrategy]。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("mch_risk_config")
public class MchRiskConfig extends MchBaseEntity implements ToResult<MchRiskConfigResult> {

    /// 是否启用地理围栏（商户级 opt-in, 默认关闭）
    private Boolean geoFenceEnabled = Boolean.FALSE;

    @Override
    public MchRiskConfigResult toResult() {
        return MchRiskConfigConvert.CONVERT.toResult(this);
    }
}
