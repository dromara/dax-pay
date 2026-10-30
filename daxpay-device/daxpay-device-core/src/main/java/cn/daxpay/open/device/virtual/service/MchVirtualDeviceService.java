package cn.daxpay.open.device.virtual.service;

import cn.daxpay.open.device.audio.support.AmountSpeechComposer;
import cn.daxpay.open.device.comm.AudioBroadcastMessage;
import cn.daxpay.open.device.comm.PrintPushData;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindAppParam;
import cn.daxpay.open.device.merchant.param.audio.MchAudioDeviceBindStoreParam;
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
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/// # 收款设备(虚拟设备)商户自助管理
///
/// 商户端能力: 查看名下虚拟设备、调整应用/门店、启停、试播试打、激活二维码内容;
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

    /// 调整使用应用(appId 空=全部应用)
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(MchAudioDeviceBindAppParam param) {
        VirtualDevice device = requireOwned(param.getId());
        String appId = resolveAppIdOrNull(device.getMchNo(), param.getAppId());
        virtualDeviceManager.changeApp(device.getId(), appId);
    }

    /// 调整使用门店
    @Transactional(rollbackFor = Exception.class)
    public void bindStore(MchAudioDeviceBindStoreParam param) {
        VirtualDevice device = requireOwned(param.getId());
        validateStoreBelongToMch(param.getStoreNo(), device.getMchNo());
        virtualDeviceManager.changeStore(device.getId(), param.getStoreNo());
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
