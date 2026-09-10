package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatAllocReceiverReq;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAllocReceiverManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAllocReceiver;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverBindParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverCreateParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverQuery;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAllocReceiverResult;
import cn.daxpay.open.channel.wechat.service.payment.alloc.WechatAllocReceiverChannelService;
import cn.daxpay.open.payment.trade.alloc.AllocReceiverFailSupport;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocRelationTypeEnum;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # 微信服务商分账接收方服务
///
/// 接收方档案 CRUD 与通道侧绑定编排(特约商户 sub_mchid 维度):
/// 新增一步绑定(落库留痕 → 组装服务商凭证 → 调子应用 → 回写状态),
/// 绑定失败记录保留(fail)可修正后重新绑定; 解绑保留记录(unbound)。
///
/// 绑定所用 sp/sub 应用落库, 重新绑定时复用组装凭证。
///
/// 事务边界: 各写方法不开事务, 落库与状态回写独立提交——失败留痕优先于原子性
/// (一步绑定模型无跨记录一致性需求, 事务包裹通道 HTTP 调用反而会把失败留痕整体回滚掉)。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAllocReceiverService {

    private final WechatIsvAllocReceiverManager allocReceiverManager;
    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final WechatIsvConfigAssembler wechatIsvConfigAssembler;
    private final WechatAllocReceiverChannelService wechatAllocReceiverChannelService;
    private final WxAppFacade wxAppFacade;

    /// 解析落库用的 sp(服务商)应用 appId
    ///
    /// 运营端显式指定则原样返回; 留空时(商户端不提供平台档应用选择)按**产品级平台档应用**兜底解析,
    /// 落库后记录自描述, 重绑与扫码取号可直接复用。
    /// 该产品未配置平台档应用时按「未配置 sp 应用」报错(与显式指定 miss 的提示一致)。
    private String resolveSpAppId(String mchNo, String spAppId) {
        if (StrUtil.isNotBlank(spAppId)) {
            return spAppId;
        }
        WxAppView platformApp = wxAppFacade.resolveProductPlatformApp(ProductEnum.WECHAT_ISV.getCode());
        if (Objects.isNull(platformApp)) {
            // 微信: 未配置该能力对应的平台应用（sp 必填）
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.wx.appNotConfigured", "spAppId");
        }
        log.info("微信服务商接收方 sp 应用按产品级平台档兜底解析: mchNo={}, spAppId={}", mchNo, platformApp.wxAppId());
        return platformApp.wxAppId();
    }

    /// 分页查询
    public PageResult<WechatIsvAllocReceiverResult> page(PageParam pageParam, WechatIsvAllocReceiverQuery query) {
        return MpUtil.toPageResult(allocReceiverManager.page(pageParam, query));
    }

    /// 查询单条(详情页按 id 定位, 归属校验同写操作)
    public WechatIsvAllocReceiverResult findById(Long id) {
        return this.loadAndCheck(id).toResult();
    }

    /// 新增并绑定接收方
    ///
    /// 绑定失败时记录保留(状态 fail + 失败原因), 并向调用方抛出失败异常以便即时提示。
    public void create(WechatIsvAllocReceiverCreateParam param) {
        // 通道商户存在性与归属校验
        WechatIsvChannelMerchant channelMerchant = wechatIsvChannelMerchantManager
                .findByChannelMchNo(param.getChannelMchNo())
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                        "error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(channelMerchant.getMchNo(), param.getMchNo())) {
            // 通道商户不存在或与商户号不匹配
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.wx.channelMerchantMismatch");
        }
        // 参数校验(类型/关系/名称/sub应用约束)
        this.validateParam(param.getReceiverType(), param.getReceiverName(), param.getRelationType(),
                param.getCustomRelation(), param.getSubAppId());
        // 同通道商户同类型同账号查重(按明文哈希)
        String accountHash = SecureUtil.sha256(param.getReceiverAccount());
        if (allocReceiverManager.existsByChannelMchNoAndTypeAndHash(param.getChannelMchNo(),
                param.getReceiverType(), accountHash)) {
            // 分账接收方已存在
            throw new BizInfoException(CommonErrorCode.REPETITIVE_OPERATION_ERROR,
                    "error.channel.allocReceiverDuplicate");
        }
        WechatIsvAllocReceiver entity = new WechatIsvAllocReceiver()
                .setChannelMchNo(param.getChannelMchNo())
                .setReceiverType(param.getReceiverType())
                .setReceiverAccount(param.getReceiverAccount())
                .setAccountHash(accountHash)
                .setReceiverName(param.getReceiverName())
                .setAlias(StrUtil.trimToNull(param.getAlias()))
                .setRelationType(param.getRelationType())
                .setCustomRelation(param.getCustomRelation())
                .setSpAppId(this.resolveSpAppId(param.getMchNo(), param.getSpAppId()))
                .setSubAppId(param.getSubAppId())
                .setStatus(AllocReceiverStatusEnum.FAIL.getCode());
        // 运营端不装载商户上下文, 必须显式赋值
        entity.setMchNo(param.getMchNo());
        allocReceiverManager.save(entity);
        this.doBind(entity);
    }

    /// 重新绑定(fail/unbound 状态), 可更换绑定所用 sp/sub 应用(留空沿用落库值)
    public void bind(WechatIsvAllocReceiverBindParam param) {
        WechatIsvAllocReceiver entity = this.loadAndCheck(param.getId());
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方无需重复绑定
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverAlreadyBound");
        }
        if (StrUtil.isNotBlank(param.getSpAppId())) {
            // 新应用合法性由 doBind 凭证组装校验, 失败留痕(fail + 原因)
            entity.setSpAppId(param.getSpAppId());
        } else if (StrUtil.isBlank(entity.getSpAppId())) {
            // 存量记录未落 sp 应用(历史数据/商户端创建)时同样按产品级平台档兜底解析
            entity.setSpAppId(this.resolveSpAppId(entity.getMchNo(), null));
        }
        if (StrUtil.isNotBlank(param.getSubAppId())) {
            entity.setSubAppId(param.getSubAppId());
        }
        this.doBind(entity);
    }

    /// 解绑(bound 状态), 成功后保留记录置 unbound
    public void unbind(Long id) {
        WechatIsvAllocReceiver entity = this.loadAndCheck(id);
        if (!Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 仅已绑定的接收方可解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverNotBound");
        }
        try {
            WechatSdkCredential credential = wechatIsvConfigAssembler.buildAllocReceiverConfig(
                    entity.getMchNo(), entity.getChannelMchNo(), entity.getSpAppId(), entity.getSubAppId());
            wechatAllocReceiverChannelService.isvUnbind(this.buildReq(entity, credential));
        } catch (Exception e) {
            // 失败原因落库(保持 bound), HTTP 网络类异常标注"通道结果未知", 异常上抛提示
            log.warn("微信服务商接收方解绑失败: id={}, account={}", entity.getId(), entity.getReceiverAccount(), e);
            entity.setErrorMsg(AllocReceiverFailSupport.recordMessage(e));
            allocReceiverManager.updateById(entity);
            throw AllocReceiverFailSupport.toUserException(e, AllocReceiverFailSupport.KEY_UNBIND_UNKNOWN);
        }
        entity.setStatus(AllocReceiverStatusEnum.UNBOUND.getCode())
                .setUnbindTime(OffsetDateTime.now())
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 删除(仅 fail/unbound, bound 必须先解绑)
    public void delete(Long id) {
        WechatIsvAllocReceiver entity = this.loadAndCheck(id);
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方不可删除, 请先解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverBoundCannotDelete");
        }
        allocReceiverManager.deleteById(id);
    }

    /// 修改别名(纯本地字段, 不触发通道调用, 任意绑定状态均可改; 留空即清空)
    public void updateAlias(Long id, String alias) {
        WechatIsvAllocReceiver entity = this.loadAndCheck(id);
        entity.setAlias(StrUtil.trimToNull(alias));
        allocReceiverManager.updateById(entity);
    }

    /// 执行通道侧绑定并回写状态
    private void doBind(WechatIsvAllocReceiver entity) {
        try {
            WechatSdkCredential credential = wechatIsvConfigAssembler.buildAllocReceiverConfig(
                    entity.getMchNo(), entity.getChannelMchNo(), entity.getSpAppId(), entity.getSubAppId());
            wechatAllocReceiverChannelService.isvBind(this.buildReq(entity, credential));
        } catch (Exception e) {
            // 失败留痕(fail + 可读原因), HTTP 网络类异常标注"通道结果未知"
            log.warn("微信服务商接收方绑定失败: id={}, account={}", entity.getId(), entity.getReceiverAccount(), e);
            entity.setStatus(AllocReceiverStatusEnum.FAIL.getCode())
                    .setErrorMsg(AllocReceiverFailSupport.recordMessage(e));
            allocReceiverManager.updateById(entity);
            throw AllocReceiverFailSupport.toUserException(e, AllocReceiverFailSupport.KEY_BIND_UNKNOWN);
        }
        entity.setStatus(AllocReceiverStatusEnum.BOUND.getCode())
                .setBindTime(OffsetDateTime.now())
                .setUnbindTime(null)
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 组装通道绑定请求
    private WechatAllocReceiverReq buildReq(WechatIsvAllocReceiver entity, WechatSdkCredential credential) {
        return new WechatAllocReceiverReq()
                .setReceiverType(entity.getReceiverType())
                .setReceiverAccount(entity.getReceiverAccount())
                .setReceiverName(entity.getReceiverName())
                .setRelationType(entity.getRelationType())
                .setCustomRelation(entity.getCustomRelation())
                .setCredential(credential);
    }

    /// 加载记录
    private WechatIsvAllocReceiver loadAndCheck(Long id) {
        return allocReceiverManager.findById(id)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                        "error.common.dataNotExist", id));
    }

    /// 微信服务商接收方参数约束校验
    private void validateParam(String receiverType, String receiverName, String relationType,
                               String customRelation, String subAppId) {
        // 类型合法性(服务商多 PERSONAL_SUB_OPENID)
        boolean typeValid = Objects.equals(receiverType, AllocReceiverTypeEnum.MERCHANT_ID.getCode())
                || Objects.equals(receiverType, AllocReceiverTypeEnum.PERSONAL_OPENID.getCode())
                || Objects.equals(receiverType, AllocReceiverTypeEnum.PERSONAL_SUB_OPENID.getCode());
        if (!typeValid) {
            // 微信: 不支持的分账接收方类型
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.allocReceiverUnsupportedType", receiverType);
        }
        // 商户号类型必填商户全称
        if (Objects.equals(receiverType, AllocReceiverTypeEnum.MERCHANT_ID.getCode())
                && StrUtil.isBlank(receiverName)) {
            // 微信: 商户号类型接收方必须填写商户全称
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.allocReceiverMchNameRequired");
        }
        // 子商户应用 openid 必须指定 sub 应用
        if (Objects.equals(receiverType, AllocReceiverTypeEnum.PERSONAL_SUB_OPENID.getCode())
                && StrUtil.isBlank(subAppId)) {
            // 微信: 子商户应用openid类型必须指定子商户应用
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.allocReceiverSubAppIdRequired");
        }
        // 关系类型合法 + CUSTOM 需自定义关系名
        AllocRelationTypeEnum.findByCode(relationType);
        if (Objects.equals(relationType, AllocRelationTypeEnum.CUSTOM.getCode())
                && StrUtil.isBlank(customRelation)) {
            // 自定义关系类型必须填写自定义关系名
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.allocReceiverCustomRelationRequired");
        }
    }
}
