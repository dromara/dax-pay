package cn.daxpay.open.payment.merchant.entity.gateway;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.payment.common.enums.CashierItemResolveModeEnum;
import cn.daxpay.open.payment.common.enums.CashierSceneEnum;
import cn.daxpay.open.payment.common.enums.GatewayCashierTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 网关收银台支付项配置(应用级)
///
/// 一张表按 cashier_type + scene 分桶:
/// - H5: scene 必填(browser/wechat_pay/alipay/union_pay/douyin), 每终端一套支付项
/// - WEB: scene 为空, 一套扁平支付项
/// 支付解析按项 resolve_mode:
/// - METHOD: 填 method
/// - DIRECT: 填 channelMchNo + capability
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_cashier_item")
public class GatewayCashierItem extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 收银台类型: h5/web
    /// @see GatewayCashierTypeEnum
    private String cashierType;

    /// H5 终端场景; WEB 为空
    /// @see CashierSceneEnum
    private String scene;

    /// 前台展示名称
    private String name;

    /// 图标编码
    private String icon;

    /// 是否推荐
    private Boolean recommend;

    /// 排序号(越小越前)
    private Integer sortNo;

    /// 解析模式: method/direct
    /// @see CashierItemResolveModeEnum
    private String resolveMode;

    /// 支付方式(METHOD 模式)
    /// @see PayMethodEnum
    private String method;

    /// 通道商户号(DIRECT 模式)
    private String channelMchNo;

    /// 支付能力(DIRECT 模式)
    /// @see PayCapabilityEnum
    private String capability;
}
