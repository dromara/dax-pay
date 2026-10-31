package cn.daxpay.open.channel.mock.entity.isv;

import cn.daxpay.open.channel.mock.convert.isv.MockIsvInfoConvert;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商信息
///
/// mock 通道服务商(ISV)模式的主数据: 模拟服务商不对接真实三方, 无密钥配置,
/// 仅承载「服务商编号 + 支付开关」语义, 供服务商形态通道商户(mock_isv 产品)归属与
/// 支付闸门([cn.daxpay.open.channel.mock.service.isv.MockIsvPaymentSwitchService])使用。
/// 全局面数据(非商户隔离), 形态参照微信服务商信息大幅裁剪。
///
/// open 版通用通道商户主表(mch_channel_merchant)无 isv_no 列, 服务商归属只落在
/// 私有绑定表 [MockIsvChannelMerchant], 由
/// [cn.daxpay.open.channel.mock.service.isv.MockIsvConfigAssembler] 按通道商户号反解。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_isv_info", autoResultMap = true)
public class MockIsvInfo extends MpBaseEntity implements ToResult<MockIsvInfoResult> {

    /// 服务商编号(MISV+雪花ID, 创建时自动生成不可改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String isvNo;

    /// 服务商名称
    private String isvName;

    /// 是否允许新建服务商通道商户(停用后不影响已有通道商户支付)
    private Boolean enable;

    /// 是否启用服务商支付(停用后仅阻止新支付下单, 不影响存量交易处理)
    private Boolean payEnable;

    /// 转换
    @Override
    public MockIsvInfoResult toResult() {
        return MockIsvInfoConvert.CONVERT.toResult(this);
    }
}
