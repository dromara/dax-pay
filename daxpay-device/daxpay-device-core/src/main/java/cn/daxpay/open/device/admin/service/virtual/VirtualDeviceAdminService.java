package cn.daxpay.open.device.admin.service.virtual;

import cn.daxpay.open.device.admin.param.common.DeviceBindMerchantParam;
import cn.daxpay.open.device.admin.param.common.DeviceBindStoreBatchParam;
import cn.daxpay.open.device.admin.param.virtual.VirtualDeviceGenerateParam;
import cn.daxpay.open.device.admin.param.virtual.VirtualDeviceParam;
import cn.daxpay.open.device.admin.service.common.DeviceBindSupport;
import cn.daxpay.open.device.audio.support.AmountSpeechComposer;
import cn.daxpay.open.device.comm.AudioBroadcastMessage;
import cn.daxpay.open.device.comm.PrintPushData;
import cn.daxpay.open.device.print.service.PrintPushService;
import cn.daxpay.open.device.virtual.client.service.VirtualDevicePushClient;
import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.daxpay.open.device.virtual.enums.VirtualTestMode;
import cn.daxpay.open.device.virtual.param.VirtualDeviceQuery;
import cn.daxpay.open.device.virtual.param.VirtualDeviceTestParam;
import cn.daxpay.open.device.virtual.result.VirtualDeviceResult;
import cn.daxpay.open.device.virtual.result.export.VirtualDeviceExportResult;
import cn.daxpay.open.device.virtual.service.VirtualDeviceCredentialService;
import cn.daxpay.open.device.virtual.service.VirtualDeviceNoService;
import cn.daxpay.open.device.virtual.service.VirtualDeviceQrService;
import cn.daxpay.open.device.virtual.service.VirtualDeviceRuntimeService;
import cn.daxpay.open.device.virtual.support.VirtualTestSampleSupport;
import cn.daxpay.open.payment.common.convert.export.CommonExportConvertHelper;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
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

/// # 虚拟设备管理(运营端)
///
/// 虚拟设备独立台账的生命周期入口: 原子生成(产号+入库一个事务)、归属维护(绑定/解绑商户,
/// 绑定可一并选应用与门店)、编辑(名称/启停)、删除、试播文案预览/试打端到端验证、
/// 激活二维码内容。激活凭证生命周期(踢线/清在线/清 secret)收敛在 [VirtualDeviceCredentialService]。
/// 开源版裁剪(对照 plus): 无代理商分拨/收回、无激活日志、无打印模板下拉(内置版式)、无监控大屏。
@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualDeviceAdminService {

    private final VirtualDeviceManager virtualDeviceManager;
    private final DeviceBindSupport deviceBindSupport;
    private final TransService transService;
    private final VirtualDeviceNoService virtualDeviceNoService;
    private final VirtualDeviceCredentialService virtualDeviceCredentialService;
    private final VirtualDeviceRuntimeService virtualDeviceRuntimeService;
    private final VirtualDeviceQrService virtualDeviceQrService;
    private final PrintPushService printPushService;
    private final VirtualDevicePushClient virtualDevicePushClient;
    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;

    /// 原子生成一批虚拟设备(产号 + 入库一个事务; 生成即入平台库存, 归属绑定走本页操作)
    ///
    /// 生成即启用(创建出来就是给用的); 需要暂停时走列表的停用操作。
    /// 批次号本表唯一, 设备号由 [VirtualDeviceNoService] 产号查重。
    @Transactional(rollbackFor = Exception.class)
    public List<String> generate(VirtualDeviceGenerateParam param) {
        String batchNo = StrUtil.trim(param.getBatchNo());
        if (virtualDeviceManager.existedByBatchNo(batchNo)) {
            // 设备: 批次号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.batchNoExists");
        }
        List<String> deviceNos = virtualDeviceNoService.generate(param.getCount());
        List<VirtualDevice> list = deviceNos.stream()
                .map(deviceNo -> new VirtualDevice()
                        .setName(deviceNo)
                        .setDeviceNo(deviceNo)
                        .setBatchNo(batchNo)
                        .setEnable(true))
                .toList();
        virtualDeviceManager.saveAll(list);
        log.info("生成 {} 台虚拟设备, 批次号: {}", list.size(), batchNo);
        return deviceNos;
    }

    /// 分页(全行组装运行态: 在线/激活/最后在线, Redis ZSET 批量 ZSCORE)
    public PageResult<VirtualDeviceResult> page(PageParam pageParam, VirtualDeviceQuery query) {
        Page<VirtualDevice> mpPage = virtualDeviceManager.page(pageParam, query);
        PageResult<VirtualDeviceResult> pageResult = MpUtil.toPageResult(mpPage);
        fillRuntime(mpPage.getRecords(), pageResult.getRecords());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 导出(服务端生成 xlsx, 查询条件与分页同参, 无时间范围必填要求)
    ///
    /// 复用 [page] 的取数口径: 商户/应用/门店名称已由翻译服务回填, 运行态三项由 Redis 批量组装;
    /// 行转换只做布尔状态词条文本与时间格式化, 不携带任何激活凭证字段。
    public void export(VirtualDeviceQuery query, HttpServletResponse response) {
        ExcelUtils.exportPaged(VirtualDeviceExportResult.class, "export.virtual_device.fileName", response,
                (pageNo, size) -> page(new PageParam(pageNo, size), query),
                this::toExportRows);
    }

    /// 分页结果 → 导出行
    private List<VirtualDeviceExportResult> toExportRows(List<VirtualDeviceResult> records) {
        return records.stream().map(this::toExportRow).toList();
    }

    /// 单行 → 导出行(布尔状态翻译为词条文本, 时间按当前用户时区格式化)
    private VirtualDeviceExportResult toExportRow(VirtualDeviceResult source) {
        return new VirtualDeviceExportResult()
                .setDeviceNo(source.getDeviceNo())
                .setName(source.getName())
                .setBatchNo(source.getBatchNo())
                .setMchNo(source.getMchNo())
                .setMchName(source.getMchName())
                .setAppId(source.getAppId())
                .setAppName(source.getAppName())
                .setStoreNo(source.getStoreNo())
                .setStoreName(source.getStoreName())
                .setActivated(this.toBoolText("export.virtual_device.activated", source.getActivated()))
                .setOnline(this.toBoolText("export.virtual_device.online", source.getOnline()))
                .setEnable(this.toBoolText("export.virtual_device.enable", source.getEnable()))
                .setLastOnlineTime(CommonExportConvertHelper.formatDateTime(source.getLastOnlineTime()))
                .setCreateTime(CommonExportConvertHelper.formatDateTime(source.getCreateTime()));
    }

    /// 布尔状态 → 国际化文本(空值输出空串, 词条形如 `export.virtual_device.activated.true`)
    private String toBoolText(String keyPrefix, Boolean value) {
        if (Objects.isNull(value)) {
            return "";
        }
        return I18nUtil.get(keyPrefix + "." + value);
    }

    /// 运行态组装(实体页与结果页按下标一一对应, 保序转换)
    ///
    /// online/lastOnlineTime 口径见 [VirtualDeviceRuntimeService];
    /// activated = secret 非空(实体在手边自行判定)。
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

    /// 修改设备(设备号/归属不可改, 归属走 bind/unbind)
    @Transactional(rollbackFor = Exception.class)
    public void update(VirtualDeviceParam param) {
        VirtualDevice entity = requireDevice(param.getId());
        entity.setName(param.getName());
        entity.setEnable(param.getEnable());
        virtualDeviceManager.updateById(entity);
    }

    /// 启用/停用设备(列表开关直切, 免整单编辑)
    @Transactional(rollbackFor = Exception.class)
    public void changeEnable(Long id, Boolean enable) {
        if (enable == null) {
            // 设备: 启用状态不可为空
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.enableNotNull");
        }
        VirtualDevice entity = requireDevice(id);
        virtualDeviceManager.changeEnable(entity.getId(), enable);
    }

    /// 删除设备(未激活且未绑定才可删: 已绑定/已激活须先解绑, 解绑会清空凭证回到可删状态)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        VirtualDevice entity = requireDevice(id);
        if (StrUtil.isNotBlank(entity.getMchNo()) || StrUtil.isNotBlank(entity.getSecret())) {
            // 设备: 设备已绑定商户或已激活, 请先解绑后再删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.deleteBound");
        }
        // 清在线 ZSET 残留成员(常规路径解绑时已清, 此处防御残留)
        virtualDeviceCredentialService.onDeleted(entity);
        virtualDeviceManager.deleteById(id);
    }

    /// 批量绑定商户与应用与门店(已绑定其他商户的设备须先解绑: 直接改绑会让旧 APP 凭证继续有效)
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(DeviceBindMerchantParam param) {
        deviceBindSupport.validateMerchant(param.getMchNo());
        for (VirtualDevice device : virtualDeviceManager.findAllByIds(param.getIds())) {
            if (StrUtil.isNotBlank(device.getMchNo()) && !device.getMchNo().equals(param.getMchNo())) {
                // 设备: 设备已绑定其他商户, 请先解绑后再绑定
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.boundOtherMch");
            }
        }
        String appId = deviceBindSupport.resolveOptionalAppId(param.getMchNo(), param.getAppId());
        String storeNo = deviceBindSupport.resolveOptionalStoreNo(param.getMchNo(), param.getStoreNo());
        virtualDeviceManager.bindMerchant(param.getIds(), param.getMchNo(), appId, storeNo);
    }

    /// 批量解绑商户(回平台库存; 同步清空凭证, 旧 APP 下一次心跳即失效)
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(List<Long> ids) {
        virtualDeviceCredentialService.revokeByIds(ids, "设备已被解绑");
        virtualDeviceManager.unbindMerchant(ids);
    }

    /// 批量绑定门店(运营端门店工作台「绑定收银设备到本店」磁贴; 单事务一次更新)
    ///
    /// 与 [bindMerchant] 的门店校验同口径: 设备须均已绑定商户且同属一个商户
    /// (未绑商户的库存设备无门店归属锚点), 门店须存在且归属该商户;
    /// 任一设备不存在则整批拒绝。
    @Transactional(rollbackFor = Exception.class)
    public void bindStoreBatch(DeviceBindStoreBatchParam param) {
        List<VirtualDevice> devices = virtualDeviceManager.findAllByIds(param.getDeviceIds());
        if (devices.size() != param.getDeviceIds().stream().distinct().count()) {
            // 设备: 部分虚拟设备不存在
            throw new DataNotExistException("error.device.virtual.batchNotFound");
        }
        String mchNo = this.requireSameAssignedMchOf(devices);
        String storeNo = deviceBindSupport.resolveOptionalStoreNo(mchNo, param.getStoreNo());
        virtualDeviceManager.changeStoreBatch(param.getDeviceIds(), storeNo);
        log.info("批量绑定 {} 台虚拟设备的门店: mchNo={}, storeNo={}", devices.size(), mchNo, storeNo);
    }

    /// 校验设备列表均已绑定商户且同一 mchNo, 返回该商户号
    private String requireSameAssignedMchOf(List<VirtualDevice> list) {
        String mchNo = null;
        for (VirtualDevice device : list) {
            if (StrUtil.isBlank(device.getMchNo())) {
                // 设备: 虚拟设备未绑定商户
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.mchNotAssigned");
            }
            if (mchNo == null) {
                mchNo = device.getMchNo();
            } else if (!mchNo.equals(device.getMchNo())) {
                // 设备: 批量操作须选择同一商户下的设备
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.batchMchMismatch");
            }
        }
        return mchNo;
    }

    /// 试播(下发 audio.test 指令, 设备按所选金额与方式播报; 返回 msgId 供回执比对)
    ///
    /// 与商户端同口径: 渠道归一化为终端词元键后下发, 金额上限统一 999,999.99 元;
    /// 设备须已激活(secret 非空), 未激活设备无指令通道。
    public String test(VirtualDeviceTestParam param) {
        VirtualTestSampleSupport.requireAnnounceMode(param.getMode());
        long amountFen = VirtualTestSampleSupport.resolveAmountFen(param.getAmountFen());
        String channel = VirtualTestSampleSupport.resolveChannel(param.getChannel());
        VirtualDevice device = requireActivated(param.getId());
        String msgId = virtualDevicePushClient.testBroadcast(device.getDeviceNo(), device.getSecret(),
                amountFen, VirtualTestSampleSupport.toDeviceChannel(channel));
        log.info("虚拟设备试播: deviceNo={}, amountFen={}, channel={}", device.getDeviceNo(), amountFen, channel);
        return msgId;
    }

    /// 试播文案预览(平台侧镜像终端词元合成规则, 供确认框展示「预计播报」)
    public String testSpeech(Long amountFen, String channel) {
        long amount = VirtualTestSampleSupport.resolveAmountFen(amountFen);
        String resolved = VirtualTestSampleSupport.resolveChannel(channel);
        return AmountSpeechComposer.fullText(
                AmountSpeechComposer.compose(VirtualTestSampleSupport.toDeviceChannel(resolved), amount));
    }

    /// 试打(平台按内置版式渲染样例小票下发; 支持播报+打印 / 纯打印两种输出)
    ///
    /// - PRINT_BOTH: pay.success 携带 printData 并附 `test=true`, 终端播报+打印但不记账;
    /// - PRINT_ONLY: print.ticket 纯打印指令并附 `manual=true`, 终端不校验"自动打印"开关。
    /// 返回 msgId 供回执轮询。
    public String testPrint(VirtualDeviceTestParam param) {
        VirtualTestMode mode = VirtualTestSampleSupport.resolvePrintMode(param.getMode());
        VirtualDevice device = requireActivated(param.getId());
        TestBuild build = buildTest(device, param);
        PrintPushData printData = printPushService.renderTest(device, build.mchName(), build.appName(), build.sample());
        if (printData == null) {
            // 设备: 小票渲染失败
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.renderFailed");
        }
        // 22 字符对齐真实单号 ORD+19 位雪花, 条码宽度仿真(17 字符样例条码偏窄误导观感)
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
        log.info("虚拟设备试打: deviceNo={}, mode={}, amountFen={}", device.getDeviceNo(), mode, build.sample().amountFen());
        return msgId;
    }

    /// 激活二维码内容(扫码激活协议 daxpay:activate:{serverBaseUrl}:{sn}, 前端负责渲染二维码图)
    public String qrContent(String deviceNo) {
        return virtualDeviceQrService.qrContent(deviceNo);
    }

    /// 试打样例装配(金额/渠道规整 + 商户与应用名 + 打印侧样例参数)
    ///
    /// 商户名/应用名取设备归属上下文(库存态设备两值为空串, 票头变量留空);
    /// 应用名不做默认应用回推(设备未绑应用时取商户默认应用名会误导票头, 留空更诚实)。
    private TestBuild buildTest(VirtualDevice device, VirtualDeviceTestParam param) {
        long amountFen = VirtualTestSampleSupport.resolveAmountFen(param.getAmountFen());
        String channel = VirtualTestSampleSupport.resolveChannel(param.getChannel());
        String mchName = "";
        String appName = "";
        if (StrUtil.isNotBlank(device.getMchNo())) {
            mchName = merchantInfoManager.findByMchNoNotTenant(device.getMchNo())
                    .map(MerchantInfo::getMchName)
                    .orElse("");
        }
        if (StrUtil.isNotBlank(device.getAppId())) {
            // 运营端无商户上下文, 忽略租户查应用(与上一行商户名取数同口径, 2026-10-24 联调实踩)
            appName = mchAppInfoManager.findByAppIdNotTenant(device.getAppId())
                    .map(MchAppInfo::getAppName)
                    .orElse("");
        }
        PrintPushService.TestSample sample = new PrintPushService.TestSample(
                amountFen,
                channel,
                StrUtil.nullToEmpty(param.getOutBizNo()),
                param.getTemplateCode(),
                param.getGoodsSample(),
                param.getPaperWidth());
        return new TestBuild(mchName, appName, sample, VirtualTestSampleSupport.toDeviceChannel(channel));
    }

    /// 按主键取设备(不存在抛错)
    private VirtualDevice requireDevice(Long id) {
        return virtualDeviceManager.findById(id)
                // 设备: 虚拟设备不存在
                .orElseThrow(() -> new DataNotExistException("error.device.virtual.notFound"));
    }

    /// 设备须存在且已激活(试播试打共用前置校验)
    private VirtualDevice requireActivated(Long id) {
        VirtualDevice device = requireDevice(id);
        if (StrUtil.isBlank(device.getSecret())) {
            throw VirtualDevicePushClient.notActivatedException();
        }
        return device;
    }

    /// 试打装配结果(商户/应用名 + 打印侧样例 + 终端词元渠道)
    private record TestBuild(String mchName, String appName, PrintPushService.TestSample sample,
                             String deviceChannel) {
    }
}
