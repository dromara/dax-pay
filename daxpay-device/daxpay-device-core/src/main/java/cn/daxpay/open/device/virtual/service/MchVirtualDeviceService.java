package cn.daxpay.open.device.virtual.service;

import cn.daxpay.open.device.audio.support.AmountSpeechComposer;
import cn.daxpay.open.device.comm.AudioBroadcastMessage;
import cn.daxpay.open.device.comm.PrintPushData;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindAppBatchParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindAppParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindStoreBatchParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindStoreParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceUpdateParam;
import cn.daxpay.open.device.merchant.result.audio.MchAudioDeviceExportResult;
import cn.daxpay.open.device.print.service.PrintPushService;
import cn.daxpay.open.device.virtual.client.service.VirtualDevicePushClient;
import cn.daxpay.open.device.virtual.client.service.VirtualTestAckTracker;
import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.daxpay.open.device.virtual.enums.VirtualTestMode;
import cn.daxpay.open.device.virtual.param.VirtualDeviceQuery;
import cn.daxpay.open.device.virtual.param.VirtualDeviceTestParam;
import cn.daxpay.open.device.virtual.result.VirtualDeviceResult;
import cn.daxpay.open.device.virtual.support.VirtualTestSampleSupport;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/// # 收款设备(虚拟设备)商户自助管理
///
/// 商户端能力: 查看名下虚拟设备、导出、改名、调整应用/门店(支持批量)、启停、试播试打、激活二维码内容;
/// 设备归属绑定由运营端完成, 商户在 APP 端凭运营发放的设备号激活。
/// 设备表继承 MpBaseEntity(库存行 mchNo 为空, 不参与 mch_no 行级隔离),
/// 所有读写均显式走 [PaymentContext] 归属校验。
/// 门店空 = 未分配, 不参与门店级播报/打印匹配(避免多门店串播/串打)。
/// 开源版裁剪(对照 plus): 无打印模板下拉(内置版式)、无试打预览端点。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchVirtualDeviceService {

    private final VirtualDeviceManager virtualDeviceManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MerchantContextLoader merchantContextLoader;
    private final PaymentContext paymentContext;
    private final TransService transService;
    private final VirtualDevicePushClient virtualDevicePushClient;
    private final VirtualTestAckTracker virtualTestAckTracker;
    private final VirtualDeviceRuntimeService virtualDeviceRuntimeService;
    private final VirtualDeviceQrService virtualDeviceQrService;
    private final PrintPushService printPushService;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            // 商户上下文未装载, 无法进行数据隔离操作
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 分页(强制当前商户)
    public PageResult<VirtualDeviceResult> page(PageParam pageParam, VirtualDeviceQuery query) {
        query.setMchNo(requireMchNo());
        Page<VirtualDevice> mpPage = virtualDeviceManager.page(pageParam, query);
        PageResult<VirtualDeviceResult> pageResult = MpUtil.toPageResult(mpPage);
        // 运行态组装(在线 tag 数据源: online/activated/lastOnlineTime, Redis ZSET 批量 ZSCORE)
        fillRuntime(mpPage.getRecords(), pageResult.getRecords());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 导出(服务端生成 xlsx, 查询条件与分页同参, 无时间范围必填要求)
    ///
    /// 复用 [page] 的取数口径: 分页已强制当前商户, 应用/门店名称由翻译服务回填, 运行态由 Redis 组装;
    /// 行转换只做布尔状态词条文本与时间格式化, 不携带任何激活凭证字段。
    public void export(VirtualDeviceQuery query, HttpServletResponse response) {
        ExcelUtils.exportPaged(MchAudioDeviceExportResult.class, "export.audio_device.fileName", response,
                (pageNo, size) -> page(new PageParam(pageNo, size), query),
                this::toExportRows);
    }

    /// 分页结果 → 导出行
    private List<MchAudioDeviceExportResult> toExportRows(List<VirtualDeviceResult> records) {
        return records.stream().map(this::toExportRow).toList();
    }

    /// 单行 → 导出行(布尔状态翻译为词条文本, 时间按当前用户时区格式化)
    private MchAudioDeviceExportResult toExportRow(VirtualDeviceResult source) {
        return new MchAudioDeviceExportResult()
                .setDeviceNo(source.getDeviceNo())
                .setName(source.getName())
                .setBatchNo(source.getBatchNo())
                .setAppName(source.getAppName())
                .setStoreName(source.getStoreName())
                .setActivated(this.toBoolText("export.audio_device.activated", source.getActivated()))
                .setOnline(this.toBoolText("export.audio_device.online", source.getOnline()))
                .setEnable(this.toBoolText("export.audio_device.enable", source.getEnable()))
                .setLastOnlineTime(CommonExportConvertHelper.formatDateTime(source.getLastOnlineTime()))
                .setCreateTime(CommonExportConvertHelper.formatDateTime(source.getCreateTime()));
    }

    /// 布尔状态 → 国际化文本(空值输出空串, 词条形如 `export.audio_device.activated.true`)
    private String toBoolText(String keyPrefix, Boolean value) {
        if (Objects.isNull(value)) {
            return "";
        }
        return I18nUtil.get(keyPrefix + "." + value);
    }

    /// 虚拟设备运行态组装(实体页与结果页按下标一一对应, 保序转换)
    private void fillRuntime(List<VirtualDevice> entities, List<VirtualDeviceResult> results) {
        if (entities.isEmpty()) {
            return;
        }
        List<VirtualDeviceRuntimeService.RuntimeInput> inputs = entities.stream()
                .map(entity -> new VirtualDeviceRuntimeService.RuntimeInput(
                        entity.getDeviceNo(), entity.getLastOnlineTime()))
                .toList();
        List<VirtualDeviceRuntimeService.VirtualRuntime> runtimes = virtualDeviceRuntimeService.load(inputs);
        for (int i = 0; i < entities.size(); i++) {
            VirtualDeviceResult result = results.get(i);
            VirtualDeviceRuntimeService.VirtualRuntime runtime = runtimes.get(i);
            result.setOnline(runtime.online());
            result.setActivated(StrUtil.isNotBlank(entities.get(i).getSecret()));
            result.setLastOnlineTime(runtime.lastOnlineTime());
        }
    }

    /// 详情
    public VirtualDeviceResult findById(Long id) {
        VirtualDevice entity = requireOwned(id);
        VirtualDeviceResult result = entity.toResult();
        // 试播/试打回执快照(Redis, 1 小时内有效; 与收款回执三列语义分开)
        virtualTestAckTracker.load(entity.getDeviceNo()).ifPresent(ack -> result
                .setLastTestAckTime(ack.time())
                .setLastTestAckMsgId(ack.msgId())
                .setLastTestAckCmd(ack.cmd())
                .setLastTestAckOk(ack.ok()));
        transService.translate(result);
        return result;
    }

    /// 修改设备名称(商户端仅开放改名, 启停走 change-enable, 归属走 bind-app/bind-store-batch)
    @Transactional(rollbackFor = Exception.class)
    public void update(MchAudioDeviceUpdateParam param) {
        VirtualDevice device = requireOwned(param.getId());
        device.setName(param.getName());
        virtualDeviceManager.updateById(device);
    }

    /// 调整使用应用(appId 空=全部应用)
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(MchAudioDeviceBindAppParam param) {
        VirtualDevice device = requireOwned(param.getId());
        String appId = resolveAppIdOrNull(device.getMchNo(), param.getAppId());
        virtualDeviceManager.changeApp(device.getId(), appId);
    }

    /// 批量调整使用应用(appId 空=全部应用)
    ///
    /// 逐个校验设备归属当前商户(跨商户 id 直接拒绝), 再校验应用归属并单事务一次更新。
    @Transactional(rollbackFor = Exception.class)
    public void bindAppBatch(MchAudioDeviceBindAppBatchParam param) {
        String mchNo = requireMchNo();
        for (Long id : param.getIds()) {
            requireOwned(id);
        }
        String appId = resolveAppIdOrNull(mchNo, param.getAppId());
        virtualDeviceManager.bindApp(param.getIds(), appId);
    }

    /// 调整使用门店
    @Transactional(rollbackFor = Exception.class)
    public void bindStore(MchAudioDeviceBindStoreParam param) {
        VirtualDevice device = requireOwned(param.getId());
        validateStoreBelongToMch(param.getStoreNo(), device.getMchNo());
        virtualDeviceManager.changeStore(device.getId(), param.getStoreNo());
    }

    /// 批量调整使用门店(门店号空=解绑, 置为未分配)
    ///
    /// 逐个校验设备归属当前商户(跨商户 id 直接拒绝); 门店有值时须归属当前商户, 空则清空门店关联。
    @Transactional(rollbackFor = Exception.class)
    public void bindStoreBatch(MchAudioDeviceBindStoreBatchParam param) {
        String mchNo = requireMchNo();
        for (Long id : param.getIds()) {
            requireOwned(id);
        }
        if (StrUtil.isNotBlank(param.getStoreNo())) {
            validateStoreBelongToMch(param.getStoreNo(), mchNo);
        }
        virtualDeviceManager.changeStoreBatch(param.getIds(), StrUtil.blankToDefault(param.getStoreNo(), null));
    }

    /// 解绑门店(置为未分配, 不参与门店级匹配)
    @Transactional(rollbackFor = Exception.class)
    public void unbindStore(Long id) {
        VirtualDevice device = requireOwned(id);
        virtualDeviceManager.changeStore(device.getId(), null);
    }

    /// 启用/停用设备
    @Transactional(rollbackFor = Exception.class)
    public void changeEnable(Long id, Boolean enable) {
        if (enable == null) {
            // 设备: 启用状态不可为空
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.enableNotNull");
        }
        VirtualDevice device = requireOwned(id);
        virtualDeviceManager.changeEnable(device.getId(), enable);
    }

    /// 试播(可指定金额与收款方式; 返回 msgId 供回执比对)
    ///
    /// 与运营端同口径: 渠道归一化为终端词元键后下发, 金额上限统一 999,999.99 元。
    public String testBroadcast(VirtualDeviceTestParam param) {
        VirtualTestSampleSupport.requireAnnounceMode(param.getMode());
        long amountFen = VirtualTestSampleSupport.resolveAmountFen(param.getAmountFen());
        String channel = VirtualTestSampleSupport.resolveChannel(param.getChannel());
        VirtualDevice device = requireOwned(param.getId());
        requireActivated(device);
        return virtualDevicePushClient.testBroadcast(device.getDeviceNo(), device.getSecret(),
                amountFen, VirtualTestSampleSupport.toDeviceChannel(channel));
    }

    /// 试播文案预览(平台镜像终端词元规则)
    public String testSpeech(Long amountFen, String channel) {
        long amount = VirtualTestSampleSupport.resolveAmountFen(amountFen);
        String resolved = VirtualTestSampleSupport.resolveChannel(channel);
        return AmountSpeechComposer.fullText(
                AmountSpeechComposer.compose(VirtualTestSampleSupport.toDeviceChannel(resolved), amount));
    }

    /// 试打(平台按内置版式渲染样例小票; 播报+打印 / 纯打印两种输出)
    public String testPrint(VirtualDeviceTestParam param) {
        VirtualTestMode mode = VirtualTestSampleSupport.resolvePrintMode(param.getMode());
        VirtualDevice device = requireOwned(param.getId());
        requireActivated(device);
        TestBuild build = buildTest(device, param);
        PrintPushData printData = printPushService.renderTest(device, build.mchName(), build.appName(), build.sample());
        if (printData == null) {
            // 设备: 小票渲染失败
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.renderFailed");
        }
        // 22 字符对齐真实单号, 条码宽度仿真(17 字符样例条码偏窄误导观感)
        String bizNo = "TEST" + System.currentTimeMillis() + RandomUtil.randomNumbers(5);
        String msgId = "test-print:" + device.getDeviceNo() + ":" + bizNo;
        if (mode == VirtualTestMode.PRINT_ONLY) {
            virtualDevicePushClient.printTicket(device.getDeviceNo(), device.getSecret(), msgId, printData, true);
        } else {
            AudioBroadcastMessage message = new AudioBroadcastMessage()
                    .setMsgId(msgId)
                    .setEvent("pay.success")
                    .setBizNo(bizNo)
                    .setChannel(build.deviceChannel())
                    .setAmount(build.sample().amountFen())
                    .setMchNo(device.getMchNo())
                    .setMchName(build.mchName())
                    .setTs(System.currentTimeMillis())
                    .setPrintData(printData);
            virtualDevicePushClient.broadcastTest(device.getDeviceNo(), device.getSecret(), message);
        }
        log.info("收款设备试打: deviceNo={}, mchNo={}, mode={}", device.getDeviceNo(), device.getMchNo(), mode);
        return msgId;
    }

    /// 激活二维码内容(归属校验后生成, 前端渲染二维码图)
    public String qrContent(String deviceNo) {
        VirtualDevice device = virtualDeviceManager.findOwnedByDeviceNo(deviceNo, requireMchNo())
                // 设备: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.device.virtual.notFound"));
        return virtualDeviceQrService.qrContent(device);
    }

    /// 试打样例装配(金额/渠道规整 + 商户与应用名 + 打印侧样例参数)
    private TestBuild buildTest(VirtualDevice device, VirtualDeviceTestParam param) {
        long amountFen = VirtualTestSampleSupport.resolveAmountFen(param.getAmountFen());
        String channel = VirtualTestSampleSupport.resolveChannel(param.getChannel());
        String mchName = merchantInfoManager.findByMchNo(device.getMchNo())
                .map(MerchantInfo::getMchName)
                .orElse("");
        // 应用名取设备绑定应用(未绑应用留空, 不做默认应用回推)
        String appName = StrUtil.isBlank(device.getAppId()) ? ""
                : mchAppInfoManager.findByAppId(device.getAppId())
                        .map(MchAppInfo::getAppName)
                        .orElse("");
        PrintPushService.TestSample sample = new PrintPushService.TestSample(
                amountFen,
                channel,
                StrUtil.nullToEmpty(param.getOutBizNo()),
                param.getTemplateCode(),
                param.getGoodsSample(),
                param.getPaperWidth());
        return new TestBuild(mchName, appName, sample, VirtualTestSampleSupport.toDeviceChannel(channel));
    }

    /// 试打装配结果(商户/应用名 + 打印侧样例 + 终端词元渠道)
    private record TestBuild(String mchName, String appName, PrintPushService.TestSample sample,
                             String deviceChannel) {
    }

    /// 按主键加载当前商户名下虚拟设备
    private VirtualDevice requireOwned(Long id) {
        return virtualDeviceManager.findOwnedById(id, requireMchNo())
                // 设备: 设备不存在
                .orElseThrow(() -> new DataNotExistException("error.device.virtual.notFound"));
    }

    /// 设备须已激活(secret 非空)
    private void requireActivated(VirtualDevice device) {
        if (StrUtil.isBlank(device.getSecret())) {
            throw VirtualDevicePushClient.notActivatedException();
        }
    }

    /// 应用号解析: 空=全部应用; 非空须归属当前商户
    private String resolveAppIdOrNull(String mchNo, String appId) {
        if (StrUtil.isBlank(appId)) {
            return null;
        }
        return merchantContextLoader.resolveApp(mchNo, appId).getAppId();
    }

    /// 门店存在且归属指定商户
    private void validateStoreBelongToMch(String storeNo, String mchNo) {
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (!Objects.equals(store.getMchNo(), mchNo)) {
            // 商户: 门店不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeNoMatch");
        }
    }
}
