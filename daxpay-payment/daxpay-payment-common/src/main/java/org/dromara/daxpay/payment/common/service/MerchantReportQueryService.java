package org.dromara.daxpay.payment.common.service;

import org.dromara.daxpay.payment.old.pay.param.report.TradeReportQuery;
import org.dromara.daxpay.payment.old.pay.result.report.MerchantReportResult;

/// # 商户报表查询服务
///
public interface MerchantReportQueryService {

    /// 商户数量统计
    MerchantReportResult merchantCount(TradeReportQuery query);
}
