package cn.daxpay.open.channel.mock.entity.isv;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商通道商户绑定
///
/// 服务商形态通道商户(mock_isv 产品)归属到全局唯一模拟服务商([MockIsvInfo])的私有绑定表,
/// mock_isv 建档时随通用主表同事务写入一行(归属锁死为单例, 不可改)。
/// channel_mch_no 唯一(未删除行部分唯一索引兜底)。
///
/// 多服务商模式取消后支付/凭证装配不再按本表反查服务商(直接取单例 isvNo),
/// 本表保留为 mock_isv 建档留痕与通道商户删除时的清理对象
/// ([cn.daxpay.open.channel.mock.strategy.MockIsvChannelMerchantCleanupStrategy])。
/// 运营端建档必须逐行显式
/// [setMchNo](admin 不装载商户上下文, 依赖 Fill 会抛上下文缺失异常)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_isv_channel_merchant", autoResultMap = true)
public class MockIsvChannelMerchant extends MchBaseEntity {

    /// 通道商户号(登记模拟通道控制台创建的商户号, 与通用主表 mch_channel_merchant.channel_mch_no 同值)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 所属支付产品(恒为 mock_isv)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String product;

    /// 归属模拟服务商ID(系统主键, 恒指向全局唯一模拟服务商 mock_isv_info.id, 创建后不可改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long isvId;
}
