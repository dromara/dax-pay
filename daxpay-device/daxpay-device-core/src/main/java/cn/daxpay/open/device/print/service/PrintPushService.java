package cn.daxpay.open.device.print.service;

import cn.daxpay.open.device.comm.DeviceNotifyPayload;
import cn.daxpay.open.device.comm.PrintPushData;
import cn.daxpay.open.device.print.PrintTerminal;
import cn.daxpay.open.device.print.driver.escpos.EscPosCompiler;
import cn.daxpay.open.device.print.support.PaperWidthView;
import cn.daxpay.open.device.print.support.TestPrintPreviewResult;
import cn.daxpay.open.device.print.template.PrintTemplateConstants;
import cn.daxpay.open.device.print.template.engine.PrintTemplateRenderer;
import cn.daxpay.open.device.print.template.engine.PrintVars;
import cn.daxpay.open.device.print.template.engine.model.LineModel;
import cn.daxpay.open.device.print.template.engine.model.PrintGoodsContext;
import cn.daxpay.open.device.print.template.engine.model.RenderResult;
import cn.daxpay.open.device.print.template.support.PrintTemplateRenderSupport;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/// # 小票推送服务(支付成功旁路渲染, 内置版式)
///
/// 由 [cn.daxpay.open.device.audio.consumer.DevicePaySuccessConsumer] 在 pay.success 旁路
/// 逐设备调用: 组装版式变量 → 按设备纸宽渲染 → 输出 ESC/POS 指令流(虚拟设备一体打印)。
/// 版式解析链 = 指定版式代码(试打) > 应用配置版式代码([DeviceNotifyPayload#getTemplateCode])
/// > 内置默认「标准收款小票」—— 开源版无模板表(拍板 2), 版式为代码内置常量
/// ([PrintTemplateConstants]), 解析恒命中不落空, 渲染失败跳过本次打印(旁路, 不影响支付主链路)。
/// 58/80 双流备宽: 台账宽度非 80 时补一份 80mm 流(虚拟设备实际纸宽平台不可知, 终端按本地纸宽挑流)。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 适配点: 模板表→内置常量表;
/// 实体时间 OffsetDateTime → 小票支付时间固定东八区墙钟(对外票据口径, 不跟随用户时区);
/// 厂商云标记文本出口(renderLineModel 族)不移植(开源版无云打印厂商)。
@Slf4j
@Service
@RequiredArgsConstructor
public class PrintPushService {

    /// 兜底纸宽(mm): 设备未配置时按 58mm 渲染, 保证版式在两种纸宽下都不溢出
    private static final int DEFAULT_PAPER_WIDTH = 58;

    /// 支付时间格式(小票票面固定东八区墙钟, 与对外报文时间口径一致)
    private static final DateTimeFormatter PAID_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.ofHours(8));

    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 渲染设备小票为 ESC/POS 推送数据(支付链路主入口: payload 携带应用配置版式与门店名变量)
    public PrintPushData render(DeviceNotifyPayload payload, PrintTerminal device, PrintGoodsContext goods) {
        return render(payload, device, goods, null);
    }

    /// 渲染设备小票为 ESC/POS 推送数据(指定版式代码: 试打/演示链路, 优先于应用配置)
    ///
    /// 渲染与试打预览共用同一次版式解析, 保证"预览的就是打出来的"。
    public PrintPushData render(DeviceNotifyPayload payload, PrintTerminal device, PrintGoodsContext goods,
                                String overrideTemplateCode) {
        Rendering rendering = renderTicket(payload, device, goods, overrideTemplateCode);
        if (rendering == null) {
            return null;
        }
        int paperWidth = normalizePaperWidth(device.getPaperWidth());
        // 双流备宽: 台账宽度非 80 时补一份 80mm 流(虚拟设备实际纸宽平台不可知, 终端按本地纸宽设置挑流);
        // 备宽渲染失败只弃备流不影响主流下发(终端回落 58mm 流, 与单流时代行为一致)
        String wideBase64 = null;
        if (paperWidth != 80) {
            try {
                Rendering wide = renderTicket(payload, PaperWidthView.w80(device), goods, overrideTemplateCode);
                wideBase64 = wide == null ? null : compileBase64(wide.model());
            } catch (Exception e) {
                // 打印: 备宽流渲染异常, 仅下发主流
                log.warn("小票备宽流渲染异常, 仅下发主流: deviceNo={}", device.getDeviceNo(), e);
            }
        }
        return new PrintPushData(compileBase64(rendering.model()), paperWidth, wideBase64);
    }

    /// 行模型 → 编译并 Base64(主流与备宽流共用同一编译出口)
    private static String compileBase64(LineModel model) {
        return RenderResult.encodeBase64(EscPosCompiler.compile(model));
    }

    /// 渲染小票(行模型): ESC/POS 出口与试打预览共用, 保证"预览的就是打出来的"
    ///
    /// 渲染失败本次跳过打印(旁路, 不影响支付主链路, 播报照常)。
    private Rendering renderTicket(DeviceNotifyPayload payload, PrintTerminal device, PrintGoodsContext goods,
                                   String overrideTemplateCode) {
        int paperWidth = normalizePaperWidth(device.getPaperWidth());
        ResolvedTemplate resolved;
        try {
            // 版式解析也在 try 内: 解析失败同样按跳过打印处理, 不能把异常抛到支付旁路
            resolved = resolveTemplate(overrideTemplateCode, payload);
        } catch (Exception e) {
            // 打印: 版式解析失败, 本次跳过打印
            log.warn("小票版式解析失败, 本次跳过打印: deviceNo={}, tradeNo={}",
                    device.getDeviceNo(), payload.getTrade().getTradeNo(), e);
            return null;
        }
        Map<String, String> vars = buildVars(payload, device);
        try {
            return new Rendering(
                    PrintTemplateRenderer.preview(resolved.content(), vars, goods, paperWidth),
                    hasGoodsArea(resolved.content()));
        } catch (Exception e) {
            // 打印: 版式渲染失败, 本次跳过打印(旁路, 不影响支付主链路)
            log.warn("小票版式渲染失败, 本次跳过打印: deviceNo={}, tradeNo={}, templateCode={}",
                    device.getDeviceNo(), payload.getTrade().getTradeNo(), resolved.templateCode(), e);
            return null;
        }
    }

    /// 组最小渲染载荷(试打/补打链路: 无应用配置上下文, 版式代码与门店名变量为空)
    static DeviceNotifyPayload minimalPayload(PayTrade trade, String mchName, String appName, String outBizNo) {
        return new DeviceNotifyPayload()
                .setTrade(trade)
                .setMchName(mchName)
                .setAppName(appName)
                .setOutBizNo(outBizNo);
    }

    /// 试打小票样例单(固定金额 12.50, 渠道微信, 不影响真实订单)
    static PayTrade sampleTrade() {
        return sampleTrade(1250L, PayProviderEnum.WECHAT.getCode());
    }

    /// 试打小票样例单(指定金额与收款方式; 单号 TEST+时间戳+5 位随机 = 22 字符,
    /// 长度对齐真实单号——条码宽度由单号长度决定, 样例必须仿真; 不落库不影响真实订单)
    static PayTrade sampleTrade(long amountFen, String channel) {
        return new PayTrade()
                .setTradeNo("TEST" + System.currentTimeMillis() + RandomUtil.randomNumbers(5))
                .setAmount(amountFen)
                .setProvider(channel)
                .setChannel(channel)
                .setPayTime(OffsetDateTime.now(ZoneOffset.UTC));
    }

    /// 渲染试打小票(参数化版: 金额/渠道/商户单号/指定版式/样例明细/纸宽覆盖)
    ///
    /// 与 [previewTest] 共用同一套变量组装与版式解析, 保证"预览的那张"就是"打出来的那张"。
    public PrintPushData renderTest(PrintTerminal device, String mchName, String appName, TestSample sample) {
        ResolvedTemplate resolved = resolveTestTemplate(sample.templateCode());
        Rendering rendering = renderTestModel(device, mchName, appName, sample, resolved);
        if (rendering == null) {
            return null;
        }
        int paperWidth = testPaperWidth(device, sample);
        // 双流备宽: 与 [render] 同口径, 复用同一份版式解析; 样例纸宽强制 80(优先于请求覆盖, 覆盖值 58 时备流仍须 80)
        String wideBase64 = null;
        if (paperWidth != 80) {
            Rendering wide = renderTestModel(device, mchName, appName, sample.withPaperWidth(80), resolved);
            wideBase64 = wide == null ? null : compileBase64(wide.model());
        }
        return new PrintPushData(compileBase64(rendering.model()), paperWidth, wideBase64);
    }

    /// 试打预览(纸面/行模型视图 + 版式命中信息; 不下发、不出纸)
    ///
    /// 变量池取**该设备的真实上下文**(商户名/应用名/设备别名/门店名/纸宽), 与 [renderTest]
    /// 完全同源; 预览纸宽可按请求覆盖(58/80), 仅影响本次渲染, 不写台账。
    /// 指令字节流不在预览出参中(APP 试打等真字节消费方走编排层另行编译, 见
    /// [cn.daxpay.open.device.virtual.client.service.VirtualDeviceClientService#previewPrint])。
    public TestPrintPreviewResult previewTest(PrintTerminal device, String mchName, String appName, TestSample sample) {
        ResolvedTemplate resolved = resolveTestTemplate(sample.templateCode());
        Rendering rendering = renderTestModel(device, mchName, appName, sample, resolved);
        if (rendering == null) {
            return null;
        }
        return new TestPrintPreviewResult(rendering.model(),
                PrintTemplateRenderSupport.qrBlocks(rendering.model()),
                resolved.templateCode(), resolved.templateName(),
                testPaperWidth(device, sample), rendering.goodsArea());
    }

    /// 试打版式解析(指定代码走 [PrintTemplateConstants#requireByCode]: 未登记代码显性报错,
    /// 空白回落内置默认 standard; 同步运营操作不静默兜底)
    private ResolvedTemplate resolveTestTemplate(String templateCode) {
        return resolveTemplate(templateCode, null);
    }

    /// 试打渲染(行模型; 渲染失败返回 null 由调用方拒发)
    private Rendering renderTestModel(PrintTerminal device, String mchName, String appName,
                                      TestSample sample, ResolvedTemplate resolved) {
        PayTrade trade = sampleTrade(sample.amountFen(), sample.channel());
        DeviceNotifyPayload payload = minimalPayload(trade, mchName, appName, StrUtil.nullToEmpty(sample.outBizNo()));
        Map<String, String> vars = buildVars(payload, device);
        PrintGoodsContext goods = Boolean.FALSE.equals(sample.goodsSample())
                ? PrintGoodsContext.empty() : PrintGoodsContext.sample();
        int paperWidth = testPaperWidth(device, sample);
        try {
            return new Rendering(
                    PrintTemplateRenderer.preview(resolved.content(), vars, goods, paperWidth),
                    hasGoodsArea(resolved.content()));
        } catch (Exception e) {
            // 打印: 试打版式渲染失败, 本次试打放弃
            log.warn("试打版式渲染失败: deviceNo={}, templateCode={}", device.getDeviceNo(), resolved.templateCode(), e);
            return null;
        }
    }

    /// 试打纸宽(请求覆盖优先, 其次设备台账, 归一化仅 80 生效)
    private int testPaperWidth(PrintTerminal device, TestSample sample) {
        Integer override = sample.paperWidth();
        return normalizePaperWidth(override != null ? override : device.getPaperWidth());
    }

    /// 试打样例(试播试打参数的打印侧投影, 不依赖虚拟设备参数类保持打印域独立)
    public record TestSample(
            long amountFen,
            String channel,
            String outBizNo,
            String templateCode,
            Boolean goodsSample,
            Integer paperWidth) {

        /// 纸宽覆盖副本(双流备宽用: 同一样例按 80mm 再渲染一遍)
        public TestSample withPaperWidth(int paperWidthMm) {
            return new TestSample(amountFen, channel, outBizNo, templateCode, goodsSample, paperWidthMm);
        }
    }

    /// 版式命中结果(内容 + 代码与展示名, 供试打预览回显"用了哪张票")
    public record ResolvedTemplate(String content, String templateCode, String templateName) {
    }

    /// 渲染产物: 行模型(ESC/POS 出口与预览共用同一次解析) + 版式是否含商品区
    private record Rendering(LineModel model, boolean goodsArea) {
    }

    /// 版式是否含商品区(goods 元素或商品合计/差额变量; 试打预览回显, 前端据此显隐"注入样例商品"开关)
    ///
    /// 轻量文本检测不做完整解析: 这些字面量即使出现在文本元素里, 渲染时同样会被变量替换,
    /// 视为"支持商品区"没有误报; 去空白只为容忍手写 JSON 的 `"type": "goods"` 间隔。
    static boolean hasGoodsArea(String content) {
        String text = StrUtil.nullToEmpty(content).replace(" ", "");
        return text.contains("\"type\":\"goods\"")
                || text.contains("${" + PrintVars.GOODS_TOTAL + "}")
                || text.contains("${" + PrintVars.GOODS_DIFF + "}");
    }

    /// 版式解析链(无表常量版): 指定代码(试打, 须已登记) > 应用配置代码 > 内置默认 standard。
    ///
    /// 指定代码未登记时抛错(同步运营操作显性暴露配置错误); 应用配置代码未登记(历史脏数据)
    /// 回落默认版式不落空(旁路不阻断播报打印)。
    ResolvedTemplate resolveTemplate(String overrideTemplateCode, DeviceNotifyPayload payload) {
        if (StrUtil.isNotBlank(overrideTemplateCode)) {
            PrintTemplateConstants.TemplateDef def = PrintTemplateConstants.requireByCode(overrideTemplateCode);
            return new ResolvedTemplate(def.content(), def.code(), def.name());
        }
        String appCode = payload == null ? null : payload.getTemplateCode();
        PrintTemplateConstants.TemplateDef def = PrintTemplateConstants.resolveOrDefault(appCode);
        return new ResolvedTemplate(def.content(), def.code(), def.name());
    }

    /// 商户订单号(版式变量 outBizNo): 取业务容器 bizOrderNo, 网关单与普通单分表
    public String resolveOutBizNo(PayTrade trade) {
        if (trade.getContainerId() == null) {
            return "";
        }
        try {
            if (PayTradeTypeEnum.GATEWAY.getCode().equals(trade.getTradeType())) {
                return gatewayPayOrderManager.findById(trade.getContainerId())
                        .map(GatewayPayOrder::getBizOrderNo)
                        .orElse("");
            }
            return normalPayOrderManager.findById(trade.getContainerId())
                    .map(NormalPayOrder::getBizOrderNo)
                    .orElse("");
        } catch (Exception e) {
            // 打印: 业务容器查询失败, 商户订单号留空(不阻断小票渲染)
            log.warn("小票商户订单号查询失败: tradeNo={}, containerId={}",
                    trade.getTradeNo(), trade.getContainerId(), e);
            return "";
        }
    }

    /// 设备纸宽归一化(仅 80 生效, 其余按 58 兜底)
    static int normalizePaperWidth(Integer paperWidth) {
        return paperWidth != null && paperWidth == 80 ? 80 : DEFAULT_PAPER_WIDTH;
    }

    /// 渠道中文名: 优先 C 端钱包(provider), 聚合支付/未知时回退接入通道(channel)
    static String resolveChannelName(PayTrade trade) {
        String name = PrintVars.asChannelName(trade.getProvider());
        if ("扫码收款".equals(name) && trade.getChannel() != null) {
            name = PrintVars.asChannelName(trade.getChannel());
        }
        return name;
    }

    /// 支付上下文 → 版式变量池(payload 版: 含门店名; 缺项填空串, 占位符不会渲染出 null 字面量)
    static Map<String, String> buildVars(DeviceNotifyPayload payload, PrintTerminal device) {
        PayTrade trade = payload.getTrade();
        Map<String, String> vars = buildVars(trade, device, payload.getMchName(), payload.getAppName(), payload.getOutBizNo());
        vars.put(PrintVars.STORE_NAME, text(payload.getStoreName()));
        return vars;
    }

    /// 支付上下文 → 版式变量池(缺项填空串, 占位符不会渲染出 null 字面量)
    ///
    /// 支付时间为 [PayTrade] 的 OffsetDateTime, 票面按东八区墙钟输出(固定口径, 见类头)。
    static Map<String, String> buildVars(PayTrade trade, PrintTerminal device,
                                         String mchName, String appName, String outBizNo) {
        long amountFen = trade.getAmount() == null ? 0L : trade.getAmount();
        OffsetDateTime payTime = trade.getPayTime() == null ? OffsetDateTime.now(ZoneOffset.UTC) : trade.getPayTime();
        String deviceName = device.getAlias() != null && !device.getAlias().isBlank()
                ? device.getAlias()
                : device.getName();
        Map<String, String> vars = new LinkedHashMap<>();
        vars.put(PrintVars.MCH_NAME, text(mchName));
        vars.put(PrintVars.APP_NAME, text(appName));
        vars.put(PrintVars.DEVICE_NAME, text(deviceName));
        vars.put(PrintVars.DEVICE_NO, device == null ? "" : text(device.getDeviceNo()));
        vars.put(PrintVars.CHANNEL_NAME, resolveChannelName(trade));
        vars.put(PrintVars.AMOUNT_FEN, String.valueOf(amountFen));
        vars.put(PrintVars.AMOUNT_YUAN, PrintVars.asAmountYuan(amountFen));
        vars.put(PrintVars.BIZ_NO, text(trade.getTradeNo()));
        vars.put(PrintVars.OUT_BIZ_NO, text(outBizNo));
        vars.put(PrintVars.PAID_TIME, PAID_TIME_FORMATTER.format(payTime));
        return vars;
    }

    private static String text(String value) {
        return value == null ? "" : value;
    }
}
