package cn.daxpay.open.payment.merchant.service.device;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.device.qrcode.service.DeviceQrCodeSupportService;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeClaimParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeParam;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/// # 支付码牌管理(商户端)
///
/// 商户自助管理名下码牌: 业务配置编辑、启停、应用/门店绑定调整、码图链接获取、空白码认领。
/// 归属变更(划拨/回收)与码牌生成为运营端能力([cn.daxpay.open.payment.admin.service.device.DeviceQrCodeAdminService])。
/// 与运营端的同源业务逻辑下沉至 [DeviceQrCodeSupportService], 本类只保留登录态商户隔离与归属校验。
///
/// [DeviceQrCode] 继承 [cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity],
/// 不参与租户拦截与 mchNo 自动填充, 所有读写均须显式走 [PaymentContext] 归属校验
@Slf4j
@Service
@RequiredArgsConstructor
public class MchDeviceQrCodeService {

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final PaymentContext paymentContext;
    private final TransService transService;
    private final DeviceQrCodeSupportService supportService;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            // 商户上下文未装载, 无法进行数据隔离操作
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验码牌归属当前商户(空白码 mchNo 为空, 同样视为不可操作)
    private void checkQrCode(DeviceQrCode entity) {
        if (!Objects.equals(entity.getMchNo(), requireMchNo())) {
            // 码牌: 码牌不属于当前商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.mchNoMatch");
        }
    }

    /// 批量加载并校验归属当前商户
    private List<DeviceQrCode> requireOwnedQrCodes(List<Long> ids) {
        List<DeviceQrCode> list = deviceQrCodeManager.findAllByIds(ids);
        if (list.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        for (DeviceQrCode qrCode : list) {
            checkQrCode(qrCode);
        }
        return list;
    }

    /// 按主键加载并校验归属
    private DeviceQrCode requireOwnedQrCode(Long id) {
        DeviceQrCode entity = deviceQrCodeManager.findById(id)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        checkQrCode(entity);
        return entity;
    }

    /// 分页(强制隔离到当前商户)
    public PageResult<DeviceQrCodeResult> page(PageParam pageParam, DeviceQrCodeQuery query) {
        // 无论前端是否传入, 一律覆盖为当前商户号
        query.setMchNo(requireMchNo());
        PageResult<DeviceQrCodeResult> pageResult = MpUtil.toPageResult(deviceQrCodeManager.page(pageParam, query));
        // 翻译商户/应用/门店名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 根据id查询
    public DeviceQrCodeResult findById(Long id) {
        DeviceQrCodeResult result = requireOwnedQrCode(id).toResult();
        // 翻译商户/应用/门店名称
        transService.translate(result);
        return result;
    }

    /// 修改码牌业务配置(编码/归属不可改; 归属走 bind/unbind)
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceQrCodeParam param) {
        DeviceQrCode entity = requireOwnedQrCode(param.getId());
        supportService.applyUpdate(entity, param.getName(), param.getAmountType(),
                param.getFixedAmount(), param.getAllocation(), param.getRemark());
    }

    /// 修改状态(启用/停用)
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, String status) {
        DeviceQrCode entity = requireOwnedQrCode(id);
        supportService.applyStatus(entity, status);
    }

    /// 批量绑定应用(校验应用归属当前商户; 空值走支付时默认应用)
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(DeviceQrCodeBindAppParam param) {
        String mchNo = requireMchNo();
        requireOwnedQrCodes(param.getIds());
        // 校验应用归属并取规范化 appId
        String appId = supportService.resolveAppId(mchNo, param.getAppId());
        deviceQrCodeManager.bindApp(param.getIds(), appId);
    }

    /// 批量解绑应用(仅清 appId, 支付时走默认应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(List<Long> ids) {
        requireOwnedQrCodes(ids);
        deviceQrCodeManager.unbindApp(ids);
    }

    /// 批量绑定门店(校验门店归属当前商户; 启用态在下单校验)
    @Transactional(rollbackFor = Exception.class)
    public void bindStore(DeviceQrCodeBindStoreParam param) {
        String mchNo = requireMchNo();
        requireOwnedQrCodes(param.getIds());
        // 门店存在且归属商户
        supportService.validateStoreBelongToMch(param.getStoreNo(), mchNo);
        deviceQrCodeManager.bindStore(param.getIds(), param.getStoreNo());
    }

    /// 批量解绑门店(保留商户/应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindStore(List<Long> ids) {
        requireOwnedQrCodes(ids);
        deviceQrCodeManager.unbindStore(ids);
    }

    /// 认领空白码牌(平台未分配库存码 → 当前商户名下)
    ///
    /// 并发场景由 [DeviceQrCodeManager#claimBlank] 的条件更新兜底, 仅一个认领生效
    @Transactional(rollbackFor = Exception.class)
    public void claim(DeviceQrCodeClaimParam param) {
        DeviceQrCode qrCode = deviceQrCodeManager.findByCode(param.getCode())
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        // 空白码才可认领; 已分配商户(含已归属自己)按已被认领处理, 提示走运营划拨渠道
        if (StrUtil.isNotBlank(qrCode.getMchNo()) || !deviceQrCodeManager.claimBlank(qrCode.getId(), requireMchNo())) {
            // 码牌: 码牌已被认领或已分配商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.alreadyAssigned");
        }
    }

    /// 获取码牌扫码链接(归属校验后委托公共构建; path 分流见 [DeviceQrCodeSupportService#buildCodeLink])
    public String getCodeLink(String code) {
        DeviceQrCode qrCode = deviceQrCodeManager.findByCode(code)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        checkQrCode(qrCode);
        return supportService.buildCodeLink(code, qrCode.getProgramType());
    }

    /// 分账能力预警(码牌开启分账前预检, 不阻断), 商户号取当前登录商户
    ///
    /// 实现见 [DeviceQrCodeSupportService#allocCapabilityWarning]
    public List<DeviceQrCodeAllocWarningResult> allocCapabilityWarning(String appId) {
        String mchNo = requireMchNo();
        return supportService.allocCapabilityWarning(mchNo, appId);
    }
}
