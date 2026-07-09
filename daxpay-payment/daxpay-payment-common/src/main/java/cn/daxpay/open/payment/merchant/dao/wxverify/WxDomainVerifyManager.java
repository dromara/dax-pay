package cn.daxpay.open.payment.merchant.dao.wxverify;

import cn.daxpay.open.payment.merchant.entity.wxverify.WxDomainVerify;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 微信域名验证文件管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class WxDomainVerifyManager extends BaseManager<WxDomainVerifyMapper, WxDomainVerify> {

    /// 根据验证码查询，忽略租户拦截（供网关响应使用）
    @IgnoreTenant
    public Optional<WxDomainVerify> findByVerifyCodeNotTenant(String verifyCode) {
        return this.findByField(WxDomainVerify::getVerifyCode, verifyCode);
    }

    /// 判断验证码是否存在（查重）
    public boolean existsByVerifyCode(String verifyCode) {
        return existedByField(WxDomainVerify::getVerifyCode, verifyCode);
    }

    /// 分页（忽略租户隔离，平台可查看所有商户的验证文件）
    @IgnoreTenant
    public Page<WxDomainVerify> page(PageParam pageParam, WxDomainVerifyQuery query) {
        Page<WxDomainVerify> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<WxDomainVerify> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /// 根据商户号查询全部
    public List<WxDomainVerify> findAllByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(WxDomainVerify::getMchNo, mchNo)
                .list();
    }

}
