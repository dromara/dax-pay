package cn.daxpay.open.payment.merchant.dao.store;

import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.enums.merchant.StoreStatusEnum;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 门店信息管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class MchStoreInfoManager extends BaseManager<MchStoreInfoMapper, MchStoreInfo> {

    /// 根据门店号查询
    public Optional<MchStoreInfo> findByStoreNo(String storeNo) {
        return this.findByField(MchStoreInfo::getStoreNo, storeNo);
    }

    /// 根据门店号查询（忽略租户, 供 JMS 消费线程等无商户上下文链路取门店名）
    @IgnoreTenant
    public Optional<MchStoreInfo> findByStoreNoNotTenant(String storeNo) {
        return this.findByField(MchStoreInfo::getStoreNo, storeNo);
    }

    /// 判断门店号是否存在
    public boolean existsByStoreNo(String storeNo) {
        return existedByField(MchStoreInfo::getStoreNo, storeNo);
    }

    /// 分页
    public Page<MchStoreInfo> page(PageParam pageParam, MchStoreInfoQuery query) {
        Page<MchStoreInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<MchStoreInfo> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /// 根据商户号查询所有门店
    public List<MchStoreInfo> findAllByMchNo(String mchNo) {
        return this.findAllByField(MchStoreInfo::getMchNo, mchNo);
    }

    /// 根据商户号查询启用状态的门店(门店选择等下拉用)
    public List<MchStoreInfo> findEnableByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(MchStoreInfo::getMchNo, mchNo)
                .eq(MchStoreInfo::getStatus, StoreStatusEnum.ENABLE.getCode())
                .list();
    }

    /// 查询商户默认门店
    public Optional<MchStoreInfo> findDefaultByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(MchStoreInfo::isDefaultStore, true)
                .eq(MchStoreInfo::getMchNo, mchNo)
                .oneOpt();
    }

    /// 商户下是否已有门店
    public boolean existsByMchNo(String mchNo) {
        return existedByField(MchStoreInfo::getMchNo, mchNo);
    }

    /// 清除商户下全部默认门店标记
    public void clearDefault(String mchNo) {
        lambdaUpdate()
                .eq(MchStoreInfo::getMchNo, mchNo)
                .set(MchStoreInfo::isDefaultStore, false)
                .update();
    }

    /// 按商户号脱敏改写(主体注销前置)
    ///
    /// 门店名改占位符; 联系电话/地址/照片引用/备注等 PII 置空——
    /// 照片附件文件本体由注销主流程在置空前收集引用、事务提交后经 [cn.daxpay.open.platform.capability.file.service.PlatformFileService#deleteByFilename] 删除。
    public void sanitizeByMchNo(String mchNo) {
        lambdaUpdate()
                .set(MchStoreInfo::getStoreName, "已删除门店")
                .set(MchStoreInfo::getContactPhone, null)
                .set(MchStoreInfo::getAddress, null)
                .set(MchStoreInfo::getLogoUrl, null)
                .set(MchStoreInfo::getFacadeUrl, null)
                .set(MchStoreInfo::getInteriorUrl, null)
                .set(MchStoreInfo::getRemark, null)
                .eq(MchStoreInfo::getMchNo, mchNo)
                .update();
    }
}
