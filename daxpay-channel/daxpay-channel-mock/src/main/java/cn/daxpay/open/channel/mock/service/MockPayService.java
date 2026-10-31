package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.enums.MockPayBodyType;
import cn.daxpay.open.channel.mock.client.req.MockPayReq;
import cn.daxpay.open.channel.mock.client.resp.MockPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.GoodsDetail;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.ChannelResultUnknownException;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/// # Mock 通道支付执行业务服务
///
/// 通过 [MockChannelClient] 调用独立 mock 子应用完成模拟支付下单,
/// 请求构建、响应解析全部在本类中完成; 机制对齐易支付
/// [cn.daxpay.open.channel.easypay.service.payment.EasyPayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockPayService {

    /// mock 通道支付方式: 扫码(挂起, 返回确认页地址)—— 一期兼容值, 现行按 way 枚举映射
    private static final String METHOD_QR = "qr";

    /// mock 通道支付方式: 付款码(即时定结果)—— 一期兼容值, 现行按 way 枚举映射
    private static final String METHOD_BAR = "bar";

    private final MockChannelClient mockChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final MockCredentialSupport mockCredentialSupport;

    /// 执行模拟支付
    ///
    /// @param order       支付订单(tradeNo 作为商户订单号透传)
    /// @param payParam    支付参数
    /// @param normalOrder 普通支付业务容器(doPay 前已填充, appId 回落取值用)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, NormalPayOrder normalOrder) {
        // 通道商户号: 支付路由回填值优先, 空则回落支付单快照(建单时已固化, 勿假设 param 恒有值)
        String channelMchNo = StrUtil.blankToDefault(payParam.getChannelMchNo(), order.getChannelMchNo());
        // 构建请求(凭证按通道商户号取商户级两件套, 支付单无 product 走通道商户号反查)
        MockPayReq req = new MockPayReq();
        // 使用支付交易号作为商户订单号透传给 mock, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setSubject(payParam.getTitle());
        // 商户订单信息补传: 业务单号/描述/附加参数原样透传, mock 侧留痕展示用
        req.setBizOrderNo(payParam.getBizOrderNo());
        req.setDescription(payParam.getDescription());
        // 商品明细: 统一支付参数的 List<GoodsDetail> 一次性序列化为 JSON 数组字符串(空/null 不设值), 下游 String 原样透传
        req.setGoodsDetail(toGoodsDetailJson(payParam.getGoodsDetail()));
        req.setAttach(payParam.getAttach());
        // 买家 openid 透传(微信 JSAPI/小程序下单必填, 子应用微信校验链按 appId+openId 双维反查档案)
        req.setOpenId(payParam.getOpenId());
        // 通道应用 AppId: 优先请求显式指定的 channelAppId(微信 JSAPI/小程序单经 doBeforePay 解析后恒非空),
        // 空则回落容器应用号快照(该回落分支只服务扫码/付款码/H5/APP 等非 JSAPI 场景)
        req.setAppId(StrUtil.isNotBlank(payParam.getChannelAppId())
                ? payParam.getChannelAppId()
                : (normalOrder == null ? null : normalOrder.getAppId()));
        // 过期时间格式化为 yyyy-MM-dd HH:mm:ss 字符串(对外报文固定东八区), null 不格式化直接传 null
        req.setExpireTime(formatExpireTime(payParam.getExpiredTime()));
        // 平台支付方式(PayMethodEnum code)映射为 mock 子应用 way:
        // 同时携带一期兼容 method(qr/bar), 子应用侧 way 优先、method 兜底
        WayMapping wayMapping = mapWay(payParam.getMethod());
        req.setWay(wayMapping.way());
        req.setMethod(wayMapping.method());
        // 支付渠道(PayProviderEnum → 子应用枚举名): 由支付方式所属渠道反推;
        // 聚合支付等子应用无对应渠道的映射留空(子应用侧按付款码前缀识别或落 UNKNOWN 统计桶)
        req.setProvider(mapProvider(payParam.getMethod()));
        // 付款码码值透传(被扫场景; 子应用仅落脱敏留痕, 并按前缀识别渠道/码值规则判定)
        req.setAuthCode(payParam.getAuthCode());
        // 通道通知地址: 始终使用平台生成的回调地址(mock→平台), 不使用 payParam.notifyUrl(语义为平台→商户)
        req.setNotifyUrl(this.buildNotifyUrl(order, channelMchNo));
        req.setMchNo(payParam.getMchNo());
        req.setChannelMchNo(channelMchNo);
        req.setCredential(mockCredentialSupport.build(channelMchNo));

        // 调用 mock 子应用
        DaxResult<MockPayResp> result = mockChannelClient.pay(req);
        // mock 返回"结果未知"(付款码处理中/延迟成功): 保持 PROCESSING 交由同步纠正
        if (result.getCode() == ChannelResultUnknownException.RESULT_UNKNOWN_CODE) {
            throw new ChannelResultUnknownException("pay.error.channelResultUnknown",
                    new RuntimeException(result.getMsg()));
        }
        if (result.getCode() != 0) {
            // 模拟通道支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.mock.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成 mock 通道异步通知地址(mock→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/mock`
    /// 机制与易支付一致(backendBaseUrl 来自平台端点配置 [PlatformUrlConfigService],
    /// 支付/退款共用同一回调端点)。
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 平台后端访问地址(backendBaseUrl)未配置, 无法生成回调地址
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/mock",
                base, order.getMchNo(), channelMchNo);
    }

    /// 订单过期时间格式化为 mock 契约字符串(yyyy-MM-dd HH:mm:ss, 东八区)
    ///
    /// 对外报文固定 GMT+8, 不跟随用户时区; null 不格式化直接返回 null, 与 mock 子应用侧可空口径一致
    private static String formatExpireTime(OffsetDateTime expiredTime) {
        if (expiredTime == null) {
            return null;
        }
        return DateTimeUtil.formatByZone(expiredTime, DateTimeUtil.ZONE_CST);
    }

    /// 商品明细列表序列化为 mock 契约 JSON 数组字符串(与 mock 子应用 MockPayReq#goodsDetail 契约对齐)
    ///
    /// null 或空列表返回 null(不设值); 序列化元素键为 camelCase
    /// (goodsId/goodsName/quantity/unitPrice/category/description/showUrl, unitPrice 单位分),
    /// hutool 默认忽略 null 字段
    private static String toGoodsDetailJson(List<GoodsDetail> goodsDetail) {
        if (CollUtil.isEmpty(goodsDetail)) {
            return null;
        }
        return JSONUtil.toJsonStr(goodsDetail);
    }

    /// 平台支付方式([PayMethodEnum] code) -> mock 子应用支付方式(way 枚举名 + 一期兼容 method)
    ///
    /// 覆盖平台四家钱包全部真实支付方式(21 项, 微信 7/支付宝 6/银联 4/抖音 4), 由
    /// MockPayMethodAlignmentTest 守住与产品策略声明的一致性:
    /// - 扫码族 QR: 四渠道原生扫码; 聚合扫码同形态(能力未挂, 仅映射备用);
    /// - 付款码族 BARCODE: 微信/支付宝/银联被扫(平台无抖音付款码);
    /// - H5 族: 手机浏览器跳转; PC 族: 支付宝电脑网站(桌面浏览器跳转, 独立 way 便于单据区分);
    /// - APP 族: 微信/支付宝/抖音 APP 拉起; JSAPI 族: 网页内拉起(四渠道);
    /// - 小程序族 MINI_APP: 微信小程序与微信小程序收银台(小程序内拉起)。
    ///
    /// 数字人民币/卡组/无固定渠道(OTHER)不属本通道模拟范围, 显式抛不支持;
    /// switch 刻意穷举全部枚举值(不写 default), 平台新增支付方式时本文件编译失败, 强制同步对齐口径。
    static WayMapping mapWay(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 扫码族(子应用按 way=QR 存单挂起返回确认页地址); 聚合扫码同一形态
            case WECHAT_QR, ALIPAY_QR, UNION_QR, DOUYIN_QR, AGGREGATE_PAY_QRCODE -> new WayMapping("QR", METHOD_QR);
            // 付款码族(被扫, 子应用按 way=BARCODE 即时判定; 平台无抖音付款码方法)
            case WECHAT_BARCODE, ALIPAY_BARCODE, UNION_BARCODE -> new WayMapping("BARCODE", METHOD_BAR);
            // H5 族(手机浏览器跳转)
            case WECHAT_H5, ALIPAY_H5, UNION_H5, DOUYIN_H5 -> new WayMapping("H5", null);
            // PC 族(支付宝电脑网站, 桌面浏览器跳转)
            case ALIPAY_PC -> new WayMapping("PC", null);
            // APP 族(APP 内拉起, 返回确认页地址, 由确认页模拟 APP 内完成支付)
            case WECHAT_APP, ALIPAY_APP, DOUYIN_APP -> new WayMapping("APP", null);
            // JSAPI 族(网页内拉起, 返回拟真 prepay 结构)
            case WECHAT_JSAPI, ALIPAY_JSAPI, UNION_JSAPI, DOUYIN_JSAPI -> new WayMapping("JSAPI", null);
            // 小程序族(小程序内拉起; 微信小程序收银台同为小程序内支付场景)
            case WECHAT_MINI, WECHAT_CASHIER -> new WayMapping("MINI_APP", null);
            // 数字人民币/卡组/无固定渠道方式不属本通道模拟范围
            case ECNY_QR, VISA_CARD_GATEWAY, VISA_CARD_PRESENT, MASTERCARD_CARD_GATEWAY, MASTERCARD_CARD_PRESENT,
                    OTHER -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.mock.unsupportedPayMethod", methodCode);
        };
    }

    /// 平台支付渠道([PayProviderEnum], 由支付方式所属渠道反推) -> mock 子应用渠道枚举名
    ///
    /// UNION_PAY→UNION; AGGREGATE_PAY→AGGREGATE(聚合支付, 子应用按聚合单留痕并在确认时选实际钱包);
    /// 数字人民币/卡组/无固定渠道(OTHER 等)留空不传(子应用无对应枚举)
    static String mapProvider(String methodCode) {
        PayProviderEnum provider = PayMethodEnum.findByCode(methodCode).getProvider();
        if (provider == null) {
            return null;
        }
        return switch (provider) {
            case WECHAT -> "WECHAT";
            case ALIPAY -> "ALIPAY";
            case UNION_PAY -> "UNION";
            case DOUYIN -> "DOUYIN";
            case AGGREGATE_PAY -> "AGGREGATE";
            case ECNY, VISA, MASTERCARD -> null;
        };
    }

    /// 是否微信 JSAPI/小程序族支付方式(触发微信应用解析的唯一条件族)
    ///
    /// [PayMethodEnum#WECHAT_JSAPI]、[PayMethodEnum#WECHAT_MINI] 与 [PayMethodEnum#WECHAT_CASHIER]
    /// 需要微信 wxAppId(网页内/小程序内/小程序收银台拉起都靠 appId+openId 反查档案); 扫码/付款码/H5/APP/支付宝系等方式
    /// 一律不解析, 供支付策略的 doBeforePay 与本服务共用同一判定口径。
    ///
    /// 未知方式编码交由 [PayMethodEnum#findByCode] 抛不支持异常(与 [mapWay] 口径一致)
    public static boolean isWxJsapiFamily(String methodCode) {
        PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
        return method == PayMethodEnum.WECHAT_JSAPI
                || method == PayMethodEnum.WECHAT_MINI
                || method == PayMethodEnum.WECHAT_CASHIER;
    }

    /// mock 子应用渠道枚举名 -> 平台支付渠道(实际付款钱包; [mapProvider] 的逆向映射)
    ///
    /// 仅消费"实际钱包"语义(聚合单确认时选择的 WECHAT/ALIPAY/UNION/DOUYIN);
    /// 子应用聚合渠道名(AGGREGATE)与未知值一律返回 null(不代表实际钱包, 不回填平台订单)。
    /// public 供 [cn.daxpay.open.channel.mock.service.callback.MockCallbackService] 等子包复用。
    public static PayProviderEnum resolveActualProvider(String subAppProvider) {
        if (StrUtil.isBlank(subAppProvider)) {
            return null;
        }
        return switch (subAppProvider.trim()) {
            case "WECHAT" -> PayProviderEnum.WECHAT;
            case "ALIPAY" -> PayProviderEnum.ALIPAY;
            case "UNION" -> PayProviderEnum.UNION_PAY;
            case "DOUYIN" -> PayProviderEnum.DOUYIN;
            default -> null;
        };
    }

    /// way + 兼容 method 映射结果(扫码/付款码两族保留一期 method 值, 新方式仅传 way)
    record WayMapping(String way, String method) {
    }

    /// 解析 mock 响应为支付结果 BO
    private PayTradeResultBo toPayResult(MockPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射:
        // URL→LINK 跳转链接(QR/SCAN/H5/PC/APP 确认页; 前端可直接打开或渲染成二维码);
        // PARAMS→IDENTIFIER 标识码(APP 历史形态, 映射保留兼容历史订单);
        // JSON→JSAPI 对象(JSAPI/小程序/公众号 prepay 结构, 小程序收银台仅认 jsapi 一种, 不可映射为 JSON)
        if (resp.getPayBodyType() != null) {
            bo.setPayBodyType(switch (resp.getPayBodyType()) {
                case URL -> PayBodyTypeEnum.LINK;
                case PARAMS -> PayBodyTypeEnum.IDENTIFIER;
                case JSON -> PayBodyTypeEnum.JSAPI;
            });
        }

        // 完成时间与金额(bar 即时成功时返回; open 侧时间链路恒为 OffsetDateTime)
        bo.setFinishTime(resp.getFinishTime());
        bo.setRealAmount(resp.getAmount());
        bo.setTotalAmount(resp.getAmount());
        bo.setBuyerPayAmount(resp.getAmount());
        return bo;
    }
}
