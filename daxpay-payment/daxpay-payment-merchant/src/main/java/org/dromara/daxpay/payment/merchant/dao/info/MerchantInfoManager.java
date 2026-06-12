package org.dromara.daxpay.payment.merchant.dao.info;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import org.dromara.daxpay.platform.core.enums.merchant.MerchantStatusEnum;
import org.dromara.daxpay.payment.merchant.param.info.MerchantInfoQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 商户信息
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantInfoManager extends BaseManager<MerchantInfoMapper, MerchantInfo> {

    /// 根据商户号查询
    public Optional<MerchantInfo> findByMchNo(String mchNo) {
        return this.findByField(MerchantInfo::getMchNo, mchNo);
    }

    /// 根据商户号查询, 忽略租户
    @IgnoreTenant
    public Optional<MerchantInfo> findByMchNoNotTenant(String mchNo) {
        return this.findByField(MerchantInfo::getMchNo, mchNo);
    }

    /// 分页
    public Page<MerchantInfo> page(PageParam pageParam, MerchantInfoQuery query) {
        Page<MerchantInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<MerchantInfo> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /// 查询启用的商户
    public List<MerchantInfo> findAllByEnable() {
        return this.lambdaQuery()
                .eq(MerchantInfo::getStatus, MerchantStatusEnum.ENABLE.getCode())
                .list();
    }

    /// 查询商户号是否存在
    public boolean existedByMchNo(String mchNo) {
        return existedByField(MerchantInfo::getMchNo, mchNo);
    }

}
