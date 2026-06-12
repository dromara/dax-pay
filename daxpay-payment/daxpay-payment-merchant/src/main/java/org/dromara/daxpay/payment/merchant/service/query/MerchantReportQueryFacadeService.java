package org.dromara.daxpay.payment.merchant.service.query;

import org.dromara.daxpay.payment.common.service.MerchantReportQueryService;
import org.dromara.daxpay.payment.merchant.dao.appinfo.MchAppInfoManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantInfoManager;
import org.dromara.daxpay.payment.merchant.entity.appinfo.MchAppInfo;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import org.dromara.daxpay.payment.pay.param.report.TradeReportQuery;
import org.dromara.daxpay.payment.pay.result.report.MerchantReportResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 商户报表查询门面
///
@Service
@RequiredArgsConstructor
public class MerchantReportQueryFacadeService implements MerchantReportQueryService {
    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;

    @Override
    public MerchantReportResult merchantCount(TradeReportQuery query) {
        Long normalCount = merchantInfoManager.lambdaQuery()
                .count();
        Long normalAppCount = mchAppInfoManager.lambdaQuery()
                .count();
        return new MerchantReportResult()
                .setNormalCount(normalCount.intValue())
                .setPartnerCount(0)
                .setNormalAppCount(normalAppCount.intValue())
                .setPartnerAppCount(0);
    }
}
