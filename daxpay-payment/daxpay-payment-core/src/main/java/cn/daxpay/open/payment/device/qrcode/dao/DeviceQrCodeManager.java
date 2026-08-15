package cn.daxpay.open.payment.device.qrcode.dao;

import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/// # 支付码牌管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DeviceQrCodeManager extends BaseManager<DeviceQrCodeMapper, DeviceQrCode> {

    /// 根据码牌编码查询
    public Optional<DeviceQrCode> findByCode(String code) {
        return findByField(DeviceQrCode::getCode, code);
    }

    /// 判断码牌编码是否存在(排除指定id, excludeId 为 null 时不排除)
    public boolean existsByCode(String code, Long excludeId) {
        if (excludeId == null) {
            return existedByField(DeviceQrCode::getCode, code);
        }
        return existedByField(DeviceQrCode::getCode, code, excludeId);
    }

    /// 判断批次号是否已存在
    public boolean existedByBatchNo(String batchNo) {
        return existedByField(DeviceQrCode::getBatchNo, batchNo);
    }

    /// 批量绑定商户与应用; storeNo 可空, 空则清空原门店(换绑商户防脏数据)
    public void bindMerchant(Collection<Long> ids, String mchNo, String appId, String storeNo) {
        lambdaUpdate()
                .set(DeviceQrCode::getMchNo, mchNo)
                .set(DeviceQrCode::getAppId, appId)
                .set(DeviceQrCode::getStoreNo, storeNo)
                // 分账开关跨商户边界重置(原商户的分账意图不随码牌带给新商户)
                .set(DeviceQrCode::getAllocation, false)
                .in(DeviceQrCode::getId, ids)
                .update();
    }

    /// 批量解绑商户与应用(回空白库存, 同步清空门店与分账开关)
    public void unbindMerchant(Collection<Long> ids) {
        lambdaUpdate()
                .set(DeviceQrCode::getMchNo, null)
                .set(DeviceQrCode::getAppId, null)
                .set(DeviceQrCode::getStoreNo, null)
                .set(DeviceQrCode::getAllocation, false)
                .in(DeviceQrCode::getId, ids)
                .update();
    }

    /// 批量绑定门店
    public void bindStore(Collection<Long> ids, String storeNo) {
        lambdaUpdate()
                .set(DeviceQrCode::getStoreNo, storeNo)
                .in(DeviceQrCode::getId, ids)
                .update();
    }

    /// 批量解绑门店(保留商户/应用)
    public void unbindStore(Collection<Long> ids) {
        lambdaUpdate()
                .set(DeviceQrCode::getStoreNo, null)
                .in(DeviceQrCode::getId, ids)
                .update();
    }

    /// 批量绑定应用(须已绑商户)
    public void bindApp(Collection<Long> ids, String appId) {
        lambdaUpdate()
                .set(DeviceQrCode::getAppId, appId)
                .in(DeviceQrCode::getId, ids)
                .update();
    }

    /// 批量解绑应用(仅清 appId, 支付时走默认应用)
    public void unbindApp(Collection<Long> ids) {
        lambdaUpdate()
                .set(DeviceQrCode::getAppId, null)
                .in(DeviceQrCode::getId, ids)
                .update();
    }

    /// 分页
    public Page<DeviceQrCode> page(PageParam pageParam, DeviceQrCodeQuery query) {
        Page<DeviceQrCode> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<DeviceQrCode> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
