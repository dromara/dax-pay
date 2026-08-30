package cn.daxpay.open.payment.app.merchant.service.device;

import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeClaimParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeParam;
import cn.daxpay.open.payment.merchant.service.device.MchDeviceQrCodeService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 商户移动端-支付码牌服务
///
/// 转发至 [MchDeviceQrCodeService], 商户隔离逻辑同处一源
@Service
@RequiredArgsConstructor
public class AppMerchantDeviceQrCodeService {

    private final MchDeviceQrCodeService mchDeviceQrCodeService;

    /// 码牌分页
    public PageResult<DeviceQrCodeResult> page(PageParam pageParam, DeviceQrCodeQuery query) {
        return mchDeviceQrCodeService.page(pageParam, query);
    }

    /// 根据id查询码牌
    public DeviceQrCodeResult findById(Long id) {
        return mchDeviceQrCodeService.findById(id);
    }

    /// 获取码牌扫码链接
    public String getCodeLink(String code) {
        return mchDeviceQrCodeService.getCodeLink(code);
    }

    /// 分账能力预警
    public List<DeviceQrCodeAllocWarningResult> allocCapabilityWarning(String appId) {
        return mchDeviceQrCodeService.allocCapabilityWarning(appId);
    }

    /// 修改码牌
    public void update(DeviceQrCodeParam param) {
        mchDeviceQrCodeService.update(param);
    }

    /// 修改码牌状态
    public void changeStatus(Long id, String status) {
        mchDeviceQrCodeService.changeStatus(id, status);
    }

    /// 批量绑定应用
    public void bindApp(DeviceQrCodeBindAppParam param) {
        mchDeviceQrCodeService.bindApp(param);
    }

    /// 批量解绑应用
    public void unbindApp(List<Long> ids) {
        mchDeviceQrCodeService.unbindApp(ids);
    }

    /// 批量绑定门店
    public void bindStore(DeviceQrCodeBindStoreParam param) {
        mchDeviceQrCodeService.bindStore(param);
    }

    /// 批量解绑门店
    public void unbindStore(List<Long> ids) {
        mchDeviceQrCodeService.unbindStore(ids);
    }

    /// 认领空白码牌
    public void claim(DeviceQrCodeClaimParam param) {
        mchDeviceQrCodeService.claim(param);
    }
}
