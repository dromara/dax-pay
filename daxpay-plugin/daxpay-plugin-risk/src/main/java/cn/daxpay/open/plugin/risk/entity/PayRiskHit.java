package cn.daxpay.open.plugin.risk.entity;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.plugin.risk.convert.PayRiskHitConvert;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitPhaseEnum;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitSceneEnum;
import cn.daxpay.open.plugin.risk.result.PayRiskHitResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 支付风险命中记录
///
/// 记录事前拦截与事后命中，供运营预警与处置。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_risk_hit")
public class PayRiskHit extends MpBaseEntity implements ToResult<PayRiskHitResult> {

    /// 阶段
    /// @see PayRiskHitPhaseEnum
    private String phase;

    /// 命中类型（与黑名单 type 一致）
    private String hitType;

    /// 命中值快照
    private String hitValue;

    /// 关联名单 ID（可空）
    private Long blacklistId;

    /// 商户号（业务字段，非租户行）
    private String mchNo;

    /// 应用号
    private String appId;

    /// 平台交易号
    private String tradeNo;

    /// 容器单号
    private String orderNo;

    /// 商户业务单号
    private String bizOrderNo;

    /// 交易类型
    private String tradeType;

    /// 支付方式
    private String method;

    /// 支付产品
    private String product;

    /// 支付通道
    private String channel;

    /// 客户端 IP 快照
    private String clientIp;

    /// 下单 openId 快照
    private String openid;

    /// 通道 buyerId 快照
    private String buyerId;

    /// 来源场景
    /// @see PayRiskHitSceneEnum
    private String scene;

    /// 备注
    private String remark;

    /// 客户端 IP 归属城市（ip2region 解析快照）
    private String clientCity;

    /// 门店所在城市（围栏命中快照）
    private String storeCity;

    /// 门店号（围栏命中快照）
    private String storeNo;

    /// 地理围栏命中时生效的策略（strict/balanced/loose, 围栏命中快照）
    private String geoFenceStrategy;

    @Override
    public PayRiskHitResult toResult() {
        return PayRiskHitConvert.CONVERT.toResult(this);
    }
}
