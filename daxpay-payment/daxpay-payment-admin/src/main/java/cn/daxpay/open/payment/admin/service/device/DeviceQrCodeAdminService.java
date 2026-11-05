package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBatchParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindMerchantParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeParam;
import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.device.qrcode.result.export.DeviceQrCodeExportResult;
import cn.daxpay.open.payment.device.qrcode.service.DeviceQrCodeSupportService;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.trade.util.CurrencyAmountUtil;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/// # 支付码牌管理(运营端)
///
/// 维护码牌台账, 支持批量创建空白码、划拨绑定商户与门店。
/// 扫码后的支付编排由 unipay 模块 CodePayAssistService 承担。
/// 与商户端([cn.daxpay.open.payment.merchant.service.device.MchDeviceQrCodeService])的同源业务逻辑
/// 下沉至 [DeviceQrCodeSupportService], 本类只保留运营端特有能力与显式传参的校验编排。
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceQrCodeAdminService {

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final DeviceQrCodeSupportService supportService;
    private final MerchantInfoManager merchantInfoManager;
    private final TransService transService;

    /// 批量创建空白码牌(不绑商户, 进入平台库存)
    @Transactional(rollbackFor = Exception.class)
    public void createBatch(DeviceQrCodeBatchParam param) {
        // 批次号唯一
        if (deviceQrCodeManager.existedByBatchNo(param.getBatchNo())) {
            // 码牌: 批次号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.batchNoExists");
        }
        // 落地程序类型(创建后不可改)
        QrCodeProgramTypeEnum programType = QrCodeProgramTypeEnum.findByCode(param.getProgramType());
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(param.getAmountType());
        supportService.validateFixedAmount(amountType, param.getFixedAmount());
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
                        .setProgramType(programType.getCode())
                        .setAmountType(amountType.getCode())
                        .setFixedAmount(fixedAmount)
                        .setStatus(status)
                        // 空白码默认非分账, 划拨后由商户/运营按需开启
                        .setAllocation(false))
                .toList();
        deviceQrCodeManager.saveAll(list);
    }

    /// 判断批次号是否已存在
    public boolean existsByBatchNo(String batchNo) {
        return deviceQrCodeManager.existedByBatchNo(batchNo);
    }

    /// 分账能力预警: 按当前网关支付配置解析各扫码场景路由出的产品, 返回不支持分账的场景清单
    ///
    /// 码牌开启分账开关前的预检提示(不阻断保存); 实现见 [DeviceQrCodeSupportService#allocCapabilityWarning]
    public List<DeviceQrCodeAllocWarningResult> allocCapabilityWarning(String mchNo, String appId) {
        return supportService.allocCapabilityWarning(mchNo, appId);
    }

    /// 批量绑定商户(可覆盖已绑定归属; appId/storeNo 均可空, 空写 null 支付时再 resolve)
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(DeviceQrCodeBindMerchantParam param) {
        // 商户存在性校验(appId/storeNo 为空时不会触达归属校验, 此处兜底防幽灵商户号落库)
        merchantInfoManager.findByMchNo(param.getMchNo())
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotExist"));
        // 应用: 有值则校验归属; 空则写 null(支付 resolveApp 取默认应用)
        String appId = supportService.resolveOptionalAppId(param.getMchNo(), param.getAppId());
        // 门店: 有值则校验归属新商户; 无值写 null 防止跨商户脏数据
        String storeNo = supportService.resolveStoreNoForBind(param.getStoreNo(), param.getMchNo());
        deviceQrCodeManager.bindMerchant(param.getIds(), param.getMchNo(), appId, storeNo);
    }

    /// 批量解绑商户(回空白库存, 保留编码/批次/金额配置; 同步清空应用与门店)
    ///
    /// 仅对已绑定的牌执行解绑(混合勾选时未绑定牌跳过); 全部未绑定时无任何变更, 直接拒绝避免"假成功"
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        List<Long> boundIds = deviceQrCodeManager.findAllByIds(ids).stream()
                .filter(qrCode -> StrUtil.isNotBlank(qrCode.getMchNo()))
                .map(DeviceQrCode::getId)
                .toList();
        if (boundIds.isEmpty()) {
            // 码牌: 所选码牌均未绑定商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.noneAssigned");
        }
        deviceQrCodeManager.unbindMerchant(boundIds);
    }

    /// 批量绑定应用(须已绑商户且勾选同商户)
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(DeviceQrCodeBindAppParam param) {
        String mchNo = requireSameAssignedMch(param.getIds());
        // 校验应用归属并取规范化 appId
        String appId = supportService.resolveAppId(mchNo, param.getAppId());
        deviceQrCodeManager.bindApp(param.getIds(), appId);
    }

    /// 批量解绑应用(仅清 appId, 支付时走默认应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        deviceQrCodeManager.unbindApp(ids);
    }

    /// 批量绑定门店(须已绑商户且勾选同商户; 仅校验存在+归属, 启用态在下单校验)
    @Transactional(rollbackFor = Exception.class)
    public void bindStore(DeviceQrCodeBindStoreParam param) {
        String mchNo = requireSameAssignedMch(param.getIds());
        // 门店存在且归属商户
        supportService.validateStoreBelongToMch(param.getStoreNo(), mchNo);
        deviceQrCodeManager.bindStore(param.getIds(), param.getStoreNo());
    }

    /// 批量解绑门店(保留商户/应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindStore(List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        deviceQrCodeManager.unbindStore(ids);
    }

    /// 修改码牌(编码/归属不可改; 归属走 bind/unbind)
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceQrCodeParam param) {
        DeviceQrCode entity = deviceQrCodeManager.findById(param.getId())
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        supportService.applyUpdate(entity, param.getName(), param.getAmountType(),
                param.getFixedAmount(), param.getAllocation(), param.getRemark());
    }

    /// 分页
    public PageResult<DeviceQrCodeResult> page(PageParam pageParam, DeviceQrCodeQuery query) {
        PageResult<DeviceQrCodeResult> pageResult = MpUtil.toPageResult(deviceQrCodeManager.page(pageParam, query));
        // 翻译商户名称(mchNo -> mchName)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 导出(服务端生成 xlsx, 查询条件与分页同参, 无时间范围必填要求)
    ///
    /// 复用 [page] 的取数口径: 商户/应用/门店名称已由翻译服务回填;
    /// 行转换做金额分→元、枚举与布尔词条翻译、时间按当前用户时区格式化。
    public void export(DeviceQrCodeQuery query, HttpServletResponse response) {
        ExcelUtils.exportPaged(DeviceQrCodeExportResult.class, "export.qrcode.fileName", response,
                (pageNo, size) -> page(new PageParam(pageNo, size), query),
                this::toExportRows);
    }

    /// 分页结果 → 导出行
    private List<DeviceQrCodeExportResult> toExportRows(List<DeviceQrCodeResult> records) {
        return records.stream().map(this::toExportRow).toList();
    }

    /// 单行 → 导出行(固定金额分→元, 枚举/布尔翻译为词条文本, 时间按当前用户时区格式化)
    private DeviceQrCodeExportResult toExportRow(DeviceQrCodeResult source) {
        return new DeviceQrCodeExportResult()
                .setCode(source.getCode())
                .setName(source.getName())
                .setBatchNo(source.getBatchNo())
                .setMchNo(source.getMchNo())
                .setMchName(source.getMchName())
                .setAppId(source.getAppId())
                .setAppName(source.getAppName())
                .setStoreNo(source.getStoreNo())
                .setStoreName(source.getStoreName())
                .setProgramType(this.toEnumName(QrCodeProgramTypeEnum.values(), source.getProgramType()))
                .setAmountType(this.toEnumName(QrCodeAmountTypeEnum.values(), source.getAmountType()))
                .setFixedAmount(this.toYuan(source.getFixedAmount()))
                .setStatus(this.toEnumName(QrCodeStatusEnum.values(), source.getStatus()))
                .setAllocation(this.toBoolText("export.qrcode.allocation", source.getAllocation()))
                .setRemark(source.getRemark())
                .setCreateTime(CommonExportConvertHelper.formatDateTime(source.getCreateTime()));
    }

    /// 枚举编码 → 国际化名称(未知编码输出空串, 不让单行脏数据打崩整次导出)
    private <E extends Enum<E> & I18nSupport> String toEnumName(E[] values, String code) {
        if (StrUtil.isBlank(code)) {
            return "";
        }
        for (E value : values) {
            if (value.getCode().equals(code)) {
                return I18nUtil.getEnumName(value);
            }
        }
        return "";
    }

    /// 布尔状态 → 国际化文本(空值输出空串, 词条形如 `export.qrcode.allocation.true`)
    private String toBoolText(String keyPrefix, Boolean value) {
        if (Objects.isNull(value)) {
            return "";
        }
        return I18nUtil.get(keyPrefix + "." + value);
    }

    /// 固定金额分 → 元(码牌固定金额固定人民币, 非固定金额为空)
    private String toYuan(Long amountFen) {
        if (Objects.isNull(amountFen)) {
            return "";
        }
        return CurrencyAmountUtil.minorToMajorStr(amountFen, CurrencyEnum.CNY);
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

    /// 删除(仅停用状态可删, 启用中的码牌可能已投放使用)
    public void delete(Long id) {
        DeviceQrCode entity = deviceQrCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        if (QrCodeStatusEnum.ENABLED.getCode().equals(entity.getStatus())) {
            // 码牌: 码牌启用中, 请先停用后再删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.enabledCannotDelete");
        }
        deviceQrCodeManager.deleteById(id);
    }

    /// 修改状态(启用/停用)
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, String status) {
        DeviceQrCode entity = deviceQrCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        supportService.applyStatus(entity, status);
    }

    /// 获取码牌扫码链接(path 分流实现见 [DeviceQrCodeSupportService#buildCodeLink])
    public String getCodeLink(String code) {
        DeviceQrCode qrCode = deviceQrCodeManager.findByCode(code)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        return supportService.buildCodeLink(code, qrCode.getProgramType());
    }

    /// 校验勾选码牌已分配商户且同一 mchNo, 返回该商户号
    private String requireSameAssignedMch(List<Long> ids) {
        List<DeviceQrCode> list = deviceQrCodeManager.findAllByIds(ids);
        if (list.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        String mchNo = null;
        for (DeviceQrCode qrCode : list) {
            if (StrUtil.isBlank(qrCode.getMchNo())) {
                // 码牌: 请先绑定商户
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.notAssigned");
            }
            if (Objects.isNull(mchNo)) {
                mchNo = qrCode.getMchNo();
            } else if (!Objects.equals(mchNo, qrCode.getMchNo())) {
                // 码牌: 批量操作须选择同一商户下的码牌
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.storeBindMchInconsistent");
            }
        }
        return mchNo;
    }
}
