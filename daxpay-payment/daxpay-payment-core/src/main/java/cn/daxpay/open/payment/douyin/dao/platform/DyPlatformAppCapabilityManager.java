package cn.daxpay.open.payment.douyin.dao.platform;

import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformAppCapability;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/// # 平台抖音应用默认能力绑定 Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DyPlatformAppCapabilityManager extends BaseManager<DyPlatformAppCapabilityMapper, DyPlatformAppCapability> {

    /// 按支付产品查询关联(按创建时间升序)
    public List<DyPlatformAppCapability> listByProduct(String product) {
        if (StrUtil.isBlank(product)) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .eq(DyPlatformAppCapability::getProduct, product)
                .orderByAsc(DyPlatformAppCapability::getCreateTime)
                .orderByAsc(DyPlatformAppCapability::getId)
                .list();
    }

    /// 根据支付产品 + 能力查询单条关联
    public Optional<DyPlatformAppCapability> findByProductAndCapability(String product, String capability) {
        if (StrUtil.hasBlank(product, capability)) {
            return Optional.empty();
        }
        return lambdaQuery()
                .eq(DyPlatformAppCapability::getProduct, product)
                .eq(DyPlatformAppCapability::getCapability, capability)
                .oneOpt();
    }

    /// 根据平台应用ID删除关联
    public void deleteByDyPlatformAppId(Long dyPlatformAppId) {
        lambdaUpdate()
                .eq(DyPlatformAppCapability::getDyPlatformAppId, dyPlatformAppId)
                .remove();
    }

    /// 清空指定产品下全部关联(批量保存时先清后插)
    public void deleteByProduct(String product) {
        if (StrUtil.isBlank(product)) {
            return;
        }
        lambdaUpdate()
                .eq(DyPlatformAppCapability::getProduct, product)
                .remove();
    }

    /// 是否存在指向该平台应用的能力绑定
    public boolean existsByDyPlatformAppId(Long dyPlatformAppId) {
        return lambdaQuery()
                .eq(DyPlatformAppCapability::getDyPlatformAppId, dyPlatformAppId)
                .exists();
    }
}
