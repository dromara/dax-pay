package cn.daxpay.open.device.virtual.param;

import cn.daxpay.open.device.virtual.enums.VirtualTestMode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备试播/试打参数(运营端与商户端共用)
///
/// 试播只消费 [amountFen] + [channel]; 试打另消费 [outBizNo]/[templateCode]/[goodsSample]/[mode]
/// /[paperWidth]。金额与渠道的缺省值和取值范围收敛在
/// [cn.daxpay.open.device.virtual.support.VirtualTestSampleSupport], 两端校验口径一致。
/// 版式代码可空(空=应用配置版式 > 内置 standard, 见 [cn.daxpay.open.device.print.service.PrintPushService])。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备试播试打参数")
public class VirtualDeviceTestParam {

    /// 设备主键
    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "设备主键")
    private Long id;

    /// 金额(分); 空=12.50 元
    @Schema(description = "金额(分), 空=1250")
    private Long amountFen;

    /// 收款方式(PayProviderEnum code); 空=wechat
    @Schema(description = "收款方式(wechat/alipay/douyin/union_pay 等), 空=wechat")
    private String channel;

    /// 商户订单号(小票变量 outBizNo; 空=票面留空)
    @Schema(description = "商户订单号(试打)")
    private String outBizNo;

    /// 指定打印版式代码(试打; 空=应用配置版式 > 内置 standard)
    @Schema(description = "打印版式代码(试打, 空=应用配置版式)")
    private String templateCode;

    /// 是否注入样例商品明细(试打); 空=true(便于验证商品区版式)
    @Schema(description = "是否注入样例商品明细, 空=true")
    private Boolean goodsSample;

    /// 输出模式(试打); 空=PRINT_BOTH
    @Schema(description = "输出模式(PRINT_BOTH/PRINT_ONLY), 空=PRINT_BOTH")
    private VirtualTestMode mode;

    /// 本次纸宽覆盖(58/80); 空=58mm(虚拟设备不维护纸宽)
    @Schema(description = "纸宽覆盖(58/80), 空=58mm")
    private Integer paperWidth;
}
