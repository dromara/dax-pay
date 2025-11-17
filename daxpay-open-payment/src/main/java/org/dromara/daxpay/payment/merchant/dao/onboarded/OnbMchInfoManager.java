package org.dromara.daxpay.payment.merchant.dao.onboarded;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.merchant.entity.onboarded.OnbMchInfo;
import org.dromara.daxpay.payment.merchant.param.onboarded.OnbMchInfoQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 进件商户信息管理
 * @author xxm
 * @since 2025/11/11
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbMchInfoManager extends BaseManager<OnbMchInfoMapper, OnbMchInfo> {

    /**
     * 分页
     */
    public Page<OnbMchInfo> page(PageParam pageParam, OnbMchInfoQuery query) {
        Page<OnbMchInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<OnbMchInfo> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /**
     * 根据进件商户号判断是否存在
     */
    public boolean exists(String onbMchNo, String mchNo, String onbChannel) {
        return lambdaQuery()
                .eq(OnbMchInfo::getOnbMchNo, onbMchNo)
                .eq(OnbMchInfo::getMchNo, mchNo)
                .eq(OnbMchInfo::getOnbChannel, onbChannel)
                .exists();
    }

    /**
     * 根据进件商户号判断是否存在
     */
    public boolean existsByOnbMchNo(String onbMchNo) {
        return lambdaQuery()
                .eq(OnbMchInfo::getOnbMchNo, onbMchNo)
                .exists();
    }

    /**
     * 根据进件商户号查询
     */
    public OnbMchInfo findByOnbMchNo(String onbMchNo) {
        return lambdaQuery()
                .eq(OnbMchInfo::getOnbMchNo, onbMchNo)
                .one();
    }

    /**
     * 查询
     */
    public List<OnbMchInfo> findAllByChannel(String mchNo, String channel) {
        return lambdaQuery()
                .eq(OnbMchInfo::getMchNo, mchNo)
                .eq(OnbMchInfo::getOnbChannel, channel)
                .list();
    }

}
