package cn.daxpay.open.channel.alipay.entity.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectAppConvert;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用
///
/// 表示支付宝直连模式下商户注册的应用实体，每个应用关联一个通道商户号，拥有独立的支付宝应用ID。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_direct_app", autoResultMap = true)
public class AlipayDirectApp extends MchBaseEntity implements ToResult<AlipayDirectAppResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 应用名称
    private String appName;

    /// 支付宝应用ID
    private String aliAppId;

    /** 应用类型: mini_program-小程序 mobile_app-移动应用 web_app-网站应用 */
    private String appType;

    /// 转换
    @Override
    public AlipayDirectAppResult toResult() {
        return AlipayDirectAppConvert.CONVERT.toResult(this);
    }
}
