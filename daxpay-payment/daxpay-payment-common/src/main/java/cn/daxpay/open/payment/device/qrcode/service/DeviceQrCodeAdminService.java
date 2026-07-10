package cn.daxpay.open.payment.device.qrcode.service;

import cn.daxpay.open.payment.common.service.MerchantContextLoader;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeBatchParam;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeBindMerchantParam;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeParam;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.merchant.dto.MchAppInfoAccessInfo;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/// # 支付码牌管理(运营端)
///
/// 维护码牌台账, 支持批量创建空白码与划拨绑定商户。
/// 扫码后的支付编排由 [CodePayAssistService] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceQrCodeAdminService {

    /// 码牌编码前缀(单条快捷新增)
    private static final String CODE_PREFIX = "QR";

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MerchantContextLoader merchantContextLoader;
    private final TransService transService;

    /// 新增码牌(快捷路径: 直接绑定商户, 自动生成编码)
    @Transactional(rollbackFor = Exception.class)
    public void add(DeviceQrCodeParam param) {
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(param.getAmountType());
        // 固定金额: fixed 类型必填且大于 0
        validateFixedAmount(amountType, param.getFixedAmount());
        // 应用解析: appId 空则取商户默认应用, 否则校验启用与归属, 回填 appId
        String appId = resolveAppId(param.getMchNo(), param.getAppId());
        DeviceQrCode entity = new DeviceQrCode()
                .setCode(generateCode())
                .setName(param.getName())
                .setMchNo(param.getMchNo())
                .setAppId(appId)
                .setAmountType(amountType.getCode())
                .setFixedAmount(amountType == QrCodeAmountTypeEnum.FIXED ? param.getFixedAmount() : null)
                .setStatus(QrCodeStatusEnum.ENABLED.getCode())
                .setRemark(param.getRemark());
        deviceQrCodeManager.save(entity);
    }

    /// 批量创建空白码牌(不绑商户, 进入平台库存)
    @Transactional(rollbackFor = Exception.class)
    public void createBatch(DeviceQrCodeBatchParam param) {
        // 批次号唯一
        if (deviceQrCodeManager.existedByBatchNo(param.getBatchNo())) {
            // 码牌: 批次号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.batchNoExists");
        }
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(param.getAmountType());
        validateFixedAmount(amountType, param.getFixedAmount());
        Long fixedAmount = amountType == QrCodeAmountTypeEnum.FIXED ? param.getFixedAmount() : null;
        // 状态: 空则默认启用
        String status = StrUtil.isBlank(param.getStatus())
                ? QrCodeStatusEnum.ENABLED.getCode()
                : QrCodeStatusEnum.findByCode(param.getStatus()).getCode();
        // 编码: 批次号 + 三位序号(001-999)
        List<DeviceQrCode> list = IntStream.rangeClosed(1, param.getCount())
                .mapToObj(i -> new DeviceQrCode()
                        .setCode(param.getBatchNo() + String.format("%03d", i))
                        .setName(param.getName())
                        .setBatchNo(param.getBatchNo())
                        .setAmountType(amountType.getCode())
                        .setFixedAmount(fixedAmount)
                        .setStatus(status)
                        .setRemark(param.getRemark()))
                .toList();
        deviceQrCodeManager.saveAll(list);
    }

    /// 判断批次号是否已存在
    public boolean existsByBatchNo(String batchNo) {
        return deviceQrCodeManager.existedByBatchNo(batchNo);
    }

    /// 批量绑定商户(可覆盖已绑定归属)
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(DeviceQrCodeBindMerchantParam param) {
        // 应用解析: appId 空则取商户默认应用
        String appId = resolveAppId(param.getMchNo(), param.getAppId());
        deviceQrCodeManager.bindMerchant(param.getIds(), param.getMchNo(), appId);
    }

    /// 批量解绑商户(回空白库存, 保留编码/批次/金额配置)
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        deviceQrCodeManager.unbindMerchant(ids);
    }

    /// 修改码牌(编码/归属不可改; 归属走 bind/unbind)
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceQrCodeParam param) {
        DeviceQrCode entity = deviceQrCodeManager.findById(param.getId())
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(param.getAmountType());
        validateFixedAmount(amountType, param.getFixedAmount());
        // 仅更新业务配置, 不改 mchNo/appId
        entity.setName(param.getName())
                .setAmountType(amountType.getCode())
                .setFixedAmount(amountType == QrCodeAmountTypeEnum.FIXED ? param.getFixedAmount() : null)
                .setRemark(param.getRemark());
        deviceQrCodeManager.updateById(entity);
    }

    /// 分页
    public PageResult<DeviceQrCodeResult> page(PageParam pageParam, DeviceQrCodeQuery query) {
        PageResult<DeviceQrCodeResult> pageResult = MpUtil.toPageResult(deviceQrCodeManager.page(pageParam, query));
        // 翻译商户名称(mchNo -> mchName)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 根据id查询
    public DeviceQrCodeResult findById(Long id) {
        DeviceQrCodeResult result = deviceQrCodeManager.findById(id)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"))
                .toResult();
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 删除
    public void delete(Long id) {
        deviceQrCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        deviceQrCodeManager.deleteById(id);
    }

    /// 修改状态(启用/停用)
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, String status) {
        QrCodeStatusEnum statusEnum = QrCodeStatusEnum.findByCode(status);
        DeviceQrCode entity = deviceQrCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        entity.setStatus(statusEnum.getCode());
        deviceQrCodeManager.updateById(entity);
    }

    /// 生成码牌编码: 前缀 + 雪花ID
    private String generateCode() {
        return CODE_PREFIX + IdUtil.getSnowflakeNextId();
    }

    /// 应用解析: 复用 [MerchantContextLoader.resolveApp], appId 空取商户默认应用, 返回解析后的 appId
    private String resolveAppId(String mchNo, String appId) {
        MchAppInfoAccessInfo mchApp = merchantContextLoader.resolveApp(mchNo, appId);
        return mchApp.getAppId();
    }

    /// 固定金额校验: fixed 类型必填且大于 0
    private void validateFixedAmount(QrCodeAmountTypeEnum amountType, Long fixedAmount) {
        if (amountType == QrCodeAmountTypeEnum.FIXED) {
            if (fixedAmount == null || fixedAmount <= 0) {
                // 码牌: 固定金额必须大于 0
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.fixedAmountInvalid");
            }
        }
    }
}
