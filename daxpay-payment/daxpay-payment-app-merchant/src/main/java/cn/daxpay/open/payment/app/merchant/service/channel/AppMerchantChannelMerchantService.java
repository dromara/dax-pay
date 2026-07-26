package cn.daxpay.open.payment.app.merchant.service.channel;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-通道商户服务
///
/// 转发至 core [ChannelMerchantService]；强制当前上下文 mchNo 过滤。
@Service
@RequiredArgsConstructor
public class AppMerchantChannelMerchantService {

    private final ChannelMerchantService channelMerchantService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 分页查询（强制当前商户）
    public PageResult<ChannelMerchantResult> page(PageParam pageParam, ChannelMerchantQuery query) {
        // 强制当前商户，忽略客户端传入的 mchNo
        query.setMchNo(requireMchNo());
        return channelMerchantService.page(pageParam, query);
    }

    /// 查询详情
    public ChannelMerchantResult findById(Long id) {
        return channelMerchantService.findById(id);
    }
}
