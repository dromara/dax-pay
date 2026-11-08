package cn.daxpay.open.payment.merchant.service.device;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
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
import cn.daxpay.open.payment.merchant.result.device.MchDeviceQrCodeExportResult;
import cn.daxpay.open.payment.trade.util.CurrencyAmountUtil;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /// 勾选导出的单次主键上限(总量上限另由 ExcelUtils 的 [ExcelUtils#EXPORT_MAX_ROWS] 护栏兜底)
    private static final int EXPORT_SELECTED_MAX = 5000;

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

    /// 导出(服务端生成 xlsx, 查询条件与分页同参, 无时间范围必填要求)
    ///
    /// 勾选优先: ids 非空时只导勾选行, 忽略其余筛选条件; ids 为空沿用查询条件全量导出。
    /// 复用 [page] 的取数口径: 商户/应用/门店名称已由翻译服务回填;
    /// 行转换做金额分→元、枚举与布尔词条翻译、时间按当前用户时区格式化。
    public void export(DeviceQrCodeQuery query, String ids, HttpServletResponse response) {
        ExcelUtils.exportPaged(MchDeviceQrCodeExportResult.class, "export.qrcode.fileName", response,
                (pageNo, size) -> pageForExport(new PageParam(pageNo, size), query, ids),
                this::toExportRows);
    }

    /// 导出分页(勾选优先: ids 非空时只导勾选行, 忽略其余筛选条件)
    ///
    /// ids 为空直接复用 [page](商户隔离与名称翻译口径与列表完全一致); ids 非空按主键分页取数,
    /// 商户条件在 SQL 层强制叠加——不属于当前商户的勾选行静默过滤(不报错、不泄露存在性)。
    /// 分页须逐页返回当前页数据、总数用真实 count
    /// ([ExcelUtils#exportPaged] 依赖首页总数判超限、尾页判结束)。
    public PageResult<DeviceQrCodeResult> pageForExport(PageParam pageParam, DeviceQrCodeQuery query, String ids) {
        if (StrUtil.isBlank(ids)) {
            return this.page(pageParam, query);
        }
        List<Long> idList = this.parseExportIds(ids);
        if (idList.isEmpty()) {
            // ids 全是空串等无效内容时按未勾选口径导全量, 避免空导出
            return this.page(pageParam, query);
        }
        PageResult<DeviceQrCodeResult> pageResult = MpUtil.toPageResult(
                deviceQrCodeManager.pageByIds(pageParam, idList, requireMchNo()));
        // 翻译商户/应用/门店名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 解析导出选中的码牌主键(逗号分隔, 去重; 单次上限 [#EXPORT_SELECTED_MAX] 条)
    private List<Long> parseExportIds(String ids) {
        List<Long> idList = new ArrayList<>();
        for (String token : StrUtil.split(ids, ',')) {
            String trimmed = StrUtil.trim(token);
            if (StrUtil.isBlank(trimmed)) {
                continue;
            }
            try {
                idList.add(Long.valueOf(trimmed));
            } catch (NumberFormatException e) {
                // 导出: 导出选中的主键格式不正确
                throw new BizInfoException(CommonCode.FAIL_CODE, "error.excel.exportSelectedInvalid");
            }
        }
        List<Long> distinctIds = idList.stream().distinct().toList();
        if (distinctIds.size() > EXPORT_SELECTED_MAX) {
            // 导出: 单次最多导出 5000 条
            throw new BizInfoException(CommonCode.FAIL_CODE,
                    "error.excel.exportSelectedLimitExceed", EXPORT_SELECTED_MAX);
        }
        return distinctIds;
    }

    /// 分页结果 → 导出行
    private List<MchDeviceQrCodeExportResult> toExportRows(List<DeviceQrCodeResult> records) {
        return records.stream().map(this::toExportRow).toList();
    }

    /// 单行 → 导出行(固定金额分→元, 枚举/布尔翻译为词条文本, 时间按当前用户时区格式化)
    ///
    /// 商户端导出全为本人数据, 不输出商户号/商户名称/应用号/门店号(冗余), 只保留名称列。
    private MchDeviceQrCodeExportResult toExportRow(DeviceQrCodeResult source) {
        return new MchDeviceQrCodeExportResult()
                .setCode(source.getCode())
                .setName(source.getName())
                .setBatchNo(source.getBatchNo())
                .setAppName(source.getAppName())
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

    /// 认领空白码牌(平台未分配库存码 → 当前商户名下, 可同时落可选归属)
    ///
    /// 归属可选: 应用/门店有值则校验归属后落库, 空则写 null(支付时取商户默认应用/默认门店)。
    /// 并发场景由 [DeviceQrCodeManager#claimBlank] 的条件更新兜底, 仅一个认领生效
    @Transactional(rollbackFor = Exception.class)
    public void claim(DeviceQrCodeClaimParam param) {
        String mchNo = requireMchNo();
        DeviceQrCode qrCode = deviceQrCodeManager.findByCode(param.getCode())
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        // 可选归属: 非空须归属当前商户, 空保留 null 语义(不指定, 支付时兜底默认)
        String appId = supportService.resolveOptionalAppId(mchNo, param.getAppId());
        String storeNo = supportService.resolveStoreNoForBind(param.getStoreNo(), mchNo);
        // 空白码才可认领; 已分配商户(含已归属自己)按已被认领处理, 提示走运营划拨渠道
        if (StrUtil.isNotBlank(qrCode.getMchNo())
                || !deviceQrCodeManager.claimBlank(qrCode.getId(), mchNo, appId, storeNo)) {
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
