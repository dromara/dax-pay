package cn.bootx.platform.daxpay.service.core.channel.wechat.domain;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信资金账单汇总
 * @author xxm
 * @since 2024/1/18
 */
@Data
@Accessors(chain = true)
public class WxReconcileFundFlowTotal {

    // 资金流水总笔数,收入笔数,收入金额,支出笔数,支出金额
    @Alias("资金流水总笔数")
    private String totalNum;
    @Alias("收入笔数")
    private String incomeNum;
    @Alias("收入金额")
    private String incomeAmount;
    @Alias("支出笔数")
    private String expenditureNum;
    @Alias("支出金额")
    private String expenditureAmount;
}
