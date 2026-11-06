package cn.daxpay.open.payment.elf.result.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 精灵收银配置选项结果
///
/// PC 收款精灵首启引导与收银窗切换门店的数据源: 门店只返回启用门店, 应用只返回启用应用;
/// 门店的 appId/appName 为门店默认应用, 可为 null(表示跟随商户默认应用)。
@Data
@Accessors(chain = true)
@Schema(title = "精灵收银配置选项结果")
public class ElfCashierConfigOptionsResult {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称
    @Schema(description = "商户名称")
    private String mchName;

    /// 启用门店清单(默认门店在前, 其次按门店号升序)
    @Schema(description = "启用门店清单")
    private List<StoreOption> stores;

    /// 启用应用清单(默认应用在前, 其次按应用号升序)
    @Schema(description = "启用应用清单")
    private List<AppOption> apps;

    /// 启用终端清单(全商户维度返回, 精灵端按已选门店过滤; 默认终端在前, 其次按终端号升序)
    @Schema(description = "启用终端清单")
    private List<TerminalOption> terminals;

    /// 门店选项
    @Data
    @Accessors(chain = true)
    @Schema(title = "精灵收银门店选项")
    public static class StoreOption {

        /// 门店号
        @Schema(description = "门店号")
        private String storeNo;

        /// 门店名称
        @Schema(description = "门店名称")
        private String storeName;

        /// 门店默认支付应用号(空则跟随商户默认应用)
        @Schema(description = "门店默认支付应用号(空则跟随商户默认应用)")
        private String appId;

        /// 门店默认支付应用名称
        @Schema(description = "门店默认支付应用名称")
        private String appName;

        /// 是否默认门店
        @Schema(description = "是否默认门店")
        private Boolean defaultStore;
    }

    /// 应用选项
    @Data
    @Accessors(chain = true)
    @Schema(title = "精灵收银应用选项")
    public static class AppOption {

        /// 应用号
        @Schema(description = "应用号")
        private String appId;

        /// 应用名称
        @Schema(description = "应用名称")
        private String appName;

        /// 是否默认应用
        @Schema(description = "是否默认应用")
        private Boolean defaultApp;
    }

    /// 终端选项
    @Data
    @Accessors(chain = true)
    @Schema(title = "精灵收银终端选项")
    public static class TerminalOption {

        /// 终端号
        @Schema(description = "终端号")
        private String terminalNo;

        /// 终端名称
        @Schema(description = "终端名称")
        private String name;

        /// 绑定门店号(可空; 精灵端按已选门店过滤)
        @Schema(description = "绑定门店号")
        private String storeNo;

        /// 是否门店默认终端(不传终端号的收银请求回落使用)
        @Schema(description = "是否门店默认终端")
        private Boolean defaultTerminal;
    }
}
