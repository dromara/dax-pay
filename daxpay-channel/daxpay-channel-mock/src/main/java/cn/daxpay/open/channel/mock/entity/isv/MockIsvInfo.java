package cn.daxpay.open.channel.mock.entity.isv;

import cn.daxpay.open.channel.mock.convert.isv.MockIsvInfoConvert;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商信息(全局唯一)
///
/// mock 通道服务商(ISV)模式的单例主数据: 模拟服务商不对接真实三方, 主数据仅承载
/// 「服务商编号 + 支付开关」语义, 供服务商形态通道商户(mock_isv 产品)归属留痕与
/// 支付闸门([cn.daxpay.open.channel.mock.service.isv.MockIsvPaymentSwitchService])使用。
/// 多服务商模式已取消: 编号可运营端修改(须与 mock 网关控制台商户档案一致), 名称固定, 由
/// [cn.daxpay.open.channel.mock.service.isv.MockIsvInfoService#getSingleton] get-or-create;
/// 接入密钥收敛为全局单份([cn.daxpay.open.channel.mock.entity.credential.MockGatewayCredential])。
/// 全局面数据(非商户隔离), 形态参照微信服务商信息大幅裁剪。
///
/// 服务商归属落私有绑定表 [MockIsvChannelMerchant](存本表主键), 仅作建档留痕与删除清理。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mock_isv_info", autoResultMap = true)
public class MockIsvInfo extends MpBaseEntity implements ToResult<MockIsvInfoResult> {

    /// 服务商编号(缺省 MISV1000000000001, 可运营端修改, 须与 mock 网关控制台商户档案 isv_no 一致)
    private String isvNo;

    /// 服务商名称(全局固定「模拟服务商」)
    private String isvName;

    /// 是否允许新建服务商通道商户(固定 true, 多服务商模式取消后不再启停)
    private Boolean enable;

    /// 是否启用服务商支付(停用后仅阻止新支付下单, 不影响存量交易处理)
    private Boolean payEnable;

    /// 转换
    @Override
    public MockIsvInfoResult toResult() {
        return MockIsvInfoConvert.CONVERT.toResult(this);
    }
}
