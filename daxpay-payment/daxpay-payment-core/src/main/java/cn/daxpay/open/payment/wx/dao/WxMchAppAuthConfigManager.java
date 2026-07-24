package cn.daxpay.open.payment.wx.dao;

import cn.daxpay.open.payment.wx.entity.WxMchAppAuthConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户微信应用授权认证配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class WxMchAppAuthConfigManager extends BaseManager<WxMchAppAuthConfigMapper, WxMchAppAuthConfig> {

    /// 根据商户应用ID查询授权认证配置
    public Optional<WxMchAppAuthConfig> findByWxMchAppId(Long wxMchAppId) {
        return lambdaQuery()
                .eq(WxMchAppAuthConfig::getWxMchAppId, wxMchAppId)
                .oneOpt();
    }

    /// 根据商户应用ID删除授权认证配置
    public void deleteByWxMchAppId(Long wxMchAppId) {
        lambdaUpdate()
                .eq(WxMchAppAuthConfig::getWxMchAppId, wxMchAppId)
                .remove();
    }
}
