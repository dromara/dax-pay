package cn.daxpay.open.payment.merchant.entity.config;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.config.MchAppNotifyConfigConvert;
import cn.daxpay.open.payment.merchant.result.config.MchAppNotifyConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户应用事件通知配置
///
/// 应用级通用事件通知配置, 与支付订单级回调并行, 不局限于支付领域
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("mch_app_notify_config")
public class MchAppNotifyConfig extends MchBaseEntity implements ToResult<MchAppNotifyConfigResult> {

    /// 应用ID
    private String appId;

    /// 回调地址 (https)
    private String notifyUrl;

    /// 通知方式 (http-HTTP异步回调)
    private String notifyWay = "http";

    /// 订阅事件类型 (逗号分隔, TradeTypeEnum的code: pay/cashouts/settle)
    private String subscribedEvents;

    /// 启用状态
    private Boolean status = false;

    /// 备注
    private String remark;

    /// 转换
    @Override
    public MchAppNotifyConfigResult toResult() {
        return MchAppNotifyConfigConvert.CONVERT.toResult(this);
    }
}
