package cn.daxpay.open.payment.app.merchant.service.channel;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantEditParam;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 商户移动端-通道商户服务
///
/// 转发至 core [ChannelMerchantService]；强制当前上下文 mchNo 过滤，
/// 写操作前显式校验归属（对照商户 Web 版 [cn.daxpay.open.payment.merchant.controller.channel.MchChannelMerchantController]）。
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

    /// 校验资源归属当前商户（TenantLine 兜底之外的显式防越权）
    private void assertOwned(ChannelMerchantResult result) {
        if (!Objects.equals(result.getMchNo(), requireMchNo())) {
            // 通道商户不属于当前商户（复用通用归属校验文案）
            throw new ConfigErrorException("error.payment.merchant.storeNoMatch");
        }
    }

    /// 分页查询（强制当前商户）
    public PageResult<ChannelMerchantResult> page(PageParam pageParam, ChannelMerchantQuery query) {
        // 强制当前商户，忽略客户端传入的 mchNo
        query.setMchNo(requireMchNo());
        return channelMerchantService.page(pageParam, query);
    }

    /// 查询详情（显式校验归属）
    public ChannelMerchantResult findById(Long id) {
        ChannelMerchantResult result = channelMerchantService.findById(id);
        this.assertOwned(result);
        return result;
    }

    /// 查询当前商户全部通道商户（通道路由基础配置等选择弹层候选，不按渠道过滤）
    public List<ChannelMerchantResult> findAllOfCurrentMch() {
        return channelMerchantService.findAllByMchNo(requireMchNo());
    }

    /// 更新启用状态（先校验归属再改状态）
    public void updateEnable(Long id, Boolean enable) {
        this.assertOwned(channelMerchantService.findById(id));
        channelMerchantService.updateEnable(id, enable);
    }

    /// 修改商户名称（先校验归属再更新）
    public void update(ChannelMerchantEditParam param) {
        this.assertOwned(channelMerchantService.findById(param.getId()));
        channelMerchantService.update(param);
    }
}
