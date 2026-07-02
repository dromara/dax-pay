package cn.daxpay.open.payment.channel.service.info;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.merchant.service.permission.MerchantPermissionService;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.channel.param.mch.ChannelMerchantEditParam;
import cn.daxpay.open.payment.channel.param.mch.ChannelMerchantQuery;
import cn.daxpay.open.payment.channel.result.info.ChannelMerchantResult;
import cn.daxpay.open.payment.masterdata.constants.channel.service.PayChannelService;
import cn.daxpay.open.payment.masterdata.constants.channel.result.PayChannelResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// # 通道商户管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMerchantService {
    private final ChannelMerchantManager channelMerchantManager;
    private final PayChannelService payChannelService;
    private final MerchantPermissionService merchantPermissionService;

    /// 分页
    public PageResult<ChannelMerchantResult> page(PageParam pageParam, ChannelMerchantQuery query){
        return MpUtil.toPageResult(channelMerchantManager.page(pageParam,query));
    }

    /// 查询详情
    public ChannelMerchantResult findById(Long id){
        return channelMerchantManager.findById(id)
                .map(ChannelMerchant::toResult)
                // 通道: 通道商户不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 根据商户号查询通道
    public List<PayChannelResult> dropdownByMchNo(String mchNo) {
        List<PayChannelResult> channelList = payChannelService.listAll();
        // 商户权限过滤
        var availableChannel = merchantPermissionService.getAvailableChannel(mchNo);
        return channelList.stream()
                .filter(o->availableChannel.contains(o.getCode()))
                .toList();
    }

    /// 编辑
    public void update(ChannelMerchantEditParam param){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        mchInfo.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchantManager.updateById(mchInfo);
    }

    /// 删除
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(id).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        channelMerchantManager.deleteById(id);
    }

    /// 根据商户和支付产品查询通道商户号列表, 多数支付通道配置使用
    public List<LabelValue> dropdown(String mchNo, String product){
        return channelMerchantManager.findAllByMchNoAndProduct(mchNo, product).stream()
                .map(mch -> new LabelValue(mch.getChannelMerchantName(), mch.getChannelMchNo()))
                .toList();
    }

    /// 根据商户号查询所有通道商户
    public List<ChannelMerchantResult> findAllByMchNo(String mchNo){
        return channelMerchantManager.findAllByMchNo(mchNo).stream()
                .map(ChannelMerchant::toResult)
                .toList();
    }

    /// 更新启用状态
    public void updateEnable(Long id, Boolean enable){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(id).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        mchInfo.setEnable(enable);
        channelMerchantManager.updateById(mchInfo);
    }

}
