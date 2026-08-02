package cn.daxpay.open.payment.merchant.dao.info;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    /// 根据商户号集合批量查询, 忽略租户(运营端跨租户翻译商户名称用)
    @IgnoreTenant
    public List<MerchantInfo> findAllByMchNosNotTenant(Collection<String> mchNos) {
        if (mchNos == null || mchNos.isEmpty()) {
            return List.of();
        }
        return this.lambdaQuery()
                .in(MerchantInfo::getMchNo, mchNos)
                .list();
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

}
