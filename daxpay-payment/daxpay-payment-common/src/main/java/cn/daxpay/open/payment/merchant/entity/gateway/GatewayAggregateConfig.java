package cn.daxpay.open.payment.merchant.entity.gateway;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 网关聚合扫码配置(应用级)
///
/// 按场景映射支付产品与支付方式, 扫码识别环境后用于建 Trade。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_aggregate_config")
public class GatewayAggregateConfig extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 微信场景支付产品
    private String wxProduct;

    /// 微信场景支付方式
    private String wxMethod;

    /// 支付宝场景支付产品
    private String alipayProduct;

    /// 支付宝场景支付方式
    private String alipayMethod;

    /// 云闪付场景支付产品
    private String unionProduct;

    /// 云闪付场景支付方式
    private String unionMethod;

    /// 是否自动拉起支付
    private Boolean autoLaunch;
}
