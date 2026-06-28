package cn.daxpay.open.payment.merchant.dao.appinfo;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 商户应用信息管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class MchAppInfoManager extends BaseManager<MchAppInfoMapper, MchAppInfo> {

    /// 根据应用AppId查询, 商户端使用会过滤租户
    public Optional<MchAppInfo> findByAppId(String appId) {
        return this.findByField(MchAppInfo::getAppId, appId);
    }
    
    /// 根据应用AppId查询， 忽略租户拦截
    @IgnoreTenant
    public Optional<MchAppInfo> findByAppIdNotTenant(String appId) {
        return this.findByField(MchAppInfo::getAppId, appId);
    }

    /// 按应用号加载商户应用，不存在则抛配置异常（忽略租户）
    @IgnoreTenant
    public MchAppInfo requireByAppIdNotTenant(String appId) {
        return findByAppIdNotTenant(appId)
                // 商户: 未找到指定应用的配置
                .orElseThrow(() -> new ConfigNotExistException("error.payment.merchant.specifiedAppConfigNotFound"));
    }

    /// 按应用号解析商户号（忽略租户）
    @IgnoreTenant
    public String requireMchNoByAppIdNotTenant(String appId) {
        return requireByAppIdNotTenant(appId).getMchNo();
    }

    /// 分页
    public Page<MchAppInfo> page(PageParam pageParam, MchAppInfoQuery query) {
        Page<MchAppInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<MchAppInfo> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /// 查询默认应用
    public Optional<MchAppInfo> findDefaultByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(MchAppInfo::isDefaultApp, true)
                .eq(MchAppInfo::getMchNo, mchNo)
                .oneOpt();
    }

    /// 查询默认应用 忽略租户
    @IgnoreTenant
    public Optional<MchAppInfo> findDefaultByMchNoNotTenant(String mchNo) {
        return lambdaQuery()
                .eq(MchAppInfo::isDefaultApp, true)
                .eq(MchAppInfo::getMchNo, mchNo)
                .oneOpt();
    }

    /// 根据商户号查询启用的应用
    public List<MchAppInfo> findAllByMchNoAndEnable(String mchNo) {
        return lambdaQuery()
                .eq(MchAppInfo::getMchNo, mchNo)
                .eq(MchAppInfo::getStatus, MchAppStatusEnum.ENABLE.getCode())
                .list();
    }

    /// 根据商户号查询应用
    public List<MchAppInfo> findAllByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(MchAppInfo::getMchNo, mchNo)
                .list();
    }

    /// 根据商户号判断是否存在
    public boolean existsByAppId(String appId) {
        return existedByField(MchAppInfo::getAppId, appId);
    }

    /// 清除默认应用
    public void clearDefault(String mchNo) {
        lambdaUpdate()
                .eq(MchAppInfo::getMchNo, mchNo)
                .set(MchAppInfo::isDefaultApp, false)
                .update();
    }

}
