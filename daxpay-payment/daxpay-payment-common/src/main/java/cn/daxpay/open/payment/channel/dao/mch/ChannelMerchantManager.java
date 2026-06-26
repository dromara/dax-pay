package cn.daxpay.open.payment.channel.dao.mch;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.channel.param.mch.ChannelMerchantQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    /// 根据商户号与通道商户号查询唯一通道商户(不存在返回 empty)
    public Optional<ChannelMerchant> findByMchNoAndChannelMchNo(String mchNo, String channelMchNo){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
