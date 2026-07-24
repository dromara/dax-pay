package cn.daxpay.open.payment.wx.dao;

import cn.daxpay.open.payment.wx.entity.WxPlatformAppCapability;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/// # 平台微信应用默认能力绑定 Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class WxPlatformAppCapabilityManager extends BaseManager<WxPlatformAppCapabilityMapper, WxPlatformAppCapability> {

    /// 按支付产品查询关联(按创建时间升序)
    public List<WxPlatformAppCapability> listByProduct(String product) {
        if (StrUtil.isBlank(product)) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .eq(WxPlatformAppCapability::getProduct, product)
                .orderByAsc(WxPlatformAppCapability::getCreateTime)
                .orderByAsc(WxPlatformAppCapability::getId)
                .list();
    }

    /// 根据支付产品 + 能力查询单条关联
    public Optional<WxPlatformAppCapability> findByProductAndCapability(String product, String capability) {
        if (StrUtil.hasBlank(product, capability)) {
            return Optional.empty();
        }
        return lambdaQuery()
                .eq(WxPlatformAppCapability::getProduct, product)
                .eq(WxPlatformAppCapability::getCapability, capability)
                .oneOpt();
    }

    /// 根据平台应用ID删除关联
    public void deleteByWxPlatformAppId(Long wxPlatformAppId) {
        lambdaUpdate()
                .eq(WxPlatformAppCapability::getWxPlatformAppId, wxPlatformAppId)
                .remove();
    }

    /// 清空指定产品下全部关联(批量保存时先清后插)
    public void deleteByProduct(String product) {
        if (StrUtil.isBlank(product)) {
            return;
        }
        lambdaUpdate()
                .eq(WxPlatformAppCapability::getProduct, product)
                .remove();
    }

    /// 是否存在指向该平台应用的能力绑定
    public boolean existsByWxPlatformAppId(Long wxPlatformAppId) {
        return lambdaQuery()
                .eq(WxPlatformAppCapability::getWxPlatformAppId, wxPlatformAppId)
                .exists();
    }
}
