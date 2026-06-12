package org.dromara.daxpay.payment.channel.dao.mch;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.channel.entity.mch.ChannelMerchant;
import org.dromara.daxpay.payment.channel.param.mch.ChannelMerchantQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 通道商户管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMerchantManager extends BaseManager<ChannelMerchantMapper, ChannelMerchant> {

    /// 分页
    public Page<ChannelMerchant> page(PageParam pageParam, ChannelMerchantQuery query){
        Page<ChannelMerchant> mpPage = MpUtil.getMpPage(pageParam, ChannelMerchant.class);
        QueryWrapper<ChannelMerchant> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /// 判断商户是否拥有该通道商户
    public boolean checkMchHasChannel(String mchNo, String channelMchNo, String product){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .eq(ChannelMerchant::getProduct, product)
                .exists();
    }

    /// 根据商户号和支付产品下拉
    public List<ChannelMerchant> findAllByMchNoAndProduct(String mchNo, String product){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .eq(ChannelMerchant::getProduct, product)
                .list();
    }

    /// 根据商户号查询所有通道商户
    public List<ChannelMerchant> findAllByMchNo(String mchNo){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .list();
    }
}
