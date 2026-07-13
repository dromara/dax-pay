package cn.daxpay.open.payment.app.admin.service.merchant.channel;

import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantEditParam;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantService;
import cn.daxpay.open.payment.masterdata.result.channel.PayChannelResult;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 运营移动端-通道商户服务
///
/// 转发至 [ChannelMerchantService]
@Service
@RequiredArgsConstructor
public class AppAdminChannelMerchantService {

    private final ChannelMerchantService channelMerchantService;

    /// 分页
    public PageResult<ChannelMerchantResult> page(PageParam pageParam, ChannelMerchantQuery query) {
        return channelMerchantService.page(pageParam, query);
    }

    /// 详情
    public ChannelMerchantResult findById(Long id) {
        return channelMerchantService.findById(id);
    }

    /// 按商户号查全部
    public List<ChannelMerchantResult> findAllByMchNo(String mchNo) {
        return channelMerchantService.findAllByMchNo(mchNo);
    }

    /// 更新启用状态
    public void updateEnable(Long id, Boolean enable) {
        channelMerchantService.updateEnable(id, enable);
    }

    /// 修改
    public void update(ChannelMerchantEditParam param) {
        channelMerchantService.update(param);
    }

    /// 通道商户下拉
    public List<LabelValue> dropdown(String mchNo, String channel) {
        return channelMerchantService.dropdown(mchNo, channel);
    }

    /// 按商户号通道下拉
    public List<PayChannelResult> dropdownByMchNo(String mchNo) {
        return channelMerchantService.dropdownByMchNo(mchNo);
    }
}
