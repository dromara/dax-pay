package cn.daxpay.open.channel.mock.entity.isv;

import cn.daxpay.open.channel.mock.convert.isv.MockIsvChannelMerchantConvert;
import cn.daxpay.open.channel.mock.result.isv.MockIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商通道商户绑定
///
/// 服务商形态通道商户(mock_isv 产品)归属到具体模拟服务商([MockIsvInfo])的私有绑定表,
/// 归属在创建时选定、锁死不可改; 支付/回调/分账按本行 isv_id 反解服务商凭证。
/// channel_mch_no 唯一(未删除行部分唯一索引兜底)。
///
/// 本表是支付/回调/分账装配反解服务商的事实源(通用主表 isv_no 为建档时固化的同源冗余,
/// 供列表筛选/详情直读); 运营端建档必须逐行显式
/// [setMchNo](admin 不装载商户上下文, 依赖 Fill 会抛上下文缺失异常)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_isv_channel_merchant", autoResultMap = true)
public class MockIsvChannelMerchant extends MchBaseEntity implements ToResult<MockIsvChannelMerchantResult> {

    /// 通道商户号(登记模拟通道控制台创建的商户号, 与通用主表 mch_channel_merchant.channel_mch_no 同值)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 所属支付产品(恒为 mock_isv)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String product;

    /// 归属模拟服务商ID(系统主键, 指向 mock_isv_info.id, 创建后不可改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long isvId;

    /// 转换
    @Override
    public MockIsvChannelMerchantResult toResult() {
        return MockIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
