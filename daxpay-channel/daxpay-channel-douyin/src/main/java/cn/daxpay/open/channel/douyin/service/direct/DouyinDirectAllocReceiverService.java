package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinAllocReceiverReq;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAllocReceiverManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAllocReceiver;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAllocReceiverBindParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAllocReceiverCreateParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAllocReceiverQuery;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAllocReceiverResult;
import cn.daxpay.open.channel.douyin.service.payment.alloc.DouyinAllocReceiverChannelService;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocRelationTypeEnum;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # 抖音直连分账接收方服务
///
/// 接收方档案 CRUD 与通道侧绑定编排:
/// 新增一步绑定(落库留痕 → 组装凭证 → 调子应用 → 回写状态),
/// 绑定失败记录保留(fail)可修正后重新绑定; 解绑保留记录(unbound)。
///
/// 绑定所用应用([channelAppId])落库, 重新绑定时复用组装凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectAllocReceiverService {

    private final DouyinDirectAllocReceiverManager allocReceiverManager;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;
    private final DouyinAllocReceiverChannelService douyinAllocReceiverChannelService;

    /// 分页查询
    public PageResult<DouyinDirectAllocReceiverResult> page(PageParam pageParam, DouyinDirectAllocReceiverQuery query) {
        return MpUtil.toPageResult(allocReceiverManager.page(pageParam, query));
    }

    /// 新增并绑定接收方
    ///
    /// 绑定失败时记录保留(状态 fail + 失败原因), 并向调用方抛出失败异常以便即时提示。
    @Transactional(rollbackFor = Exception.class)
    public void create(DouyinDirectAllocReceiverCreateParam param) {
        // 通道商户存在性与归属校验
        DouyinDirectChannelMerchant channelMerchant = channelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, param.getChannelMchNo())
                .oneOpt()
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                        "error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(channelMerchant.getMchNo(), param.getMchNo())) {
            // 抖音: 通道商户不存在或与商户号不匹配
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.douyin.channelMerchantMismatch");
        }
        // 参数校验(类型/关系/名称约束)
        this.validateParam(param.getReceiverType(), param.getReceiverName(), param.getRelationType(),
                param.getCustomRelation());
        // 同通道商户同类型同账号查重(按明文哈希)
        String accountHash = SecureUtil.sha256(param.getReceiverAccount());
        if (allocReceiverManager.existsByChannelMchNoAndTypeAndHash(param.getChannelMchNo(),
                param.getReceiverType(), accountHash)) {
            // 分账接收方已存在
            throw new BizInfoException(CommonErrorCode.REPETITIVE_OPERATION_ERROR,
                    "error.channel.allocReceiverDuplicate");
        }
        DouyinDirectAllocReceiver entity = new DouyinDirectAllocReceiver()
                .setChannelMchNo(param.getChannelMchNo())
                .setReceiverType(param.getReceiverType())
                .setReceiverAccount(param.getReceiverAccount())
                .setAccountHash(accountHash)
                .setReceiverName(param.getReceiverName())
                .setRelationType(param.getRelationType())
                .setCustomRelation(param.getCustomRelation())
                .setChannelAppId(param.getChannelAppId())
                .setStatus(AllocReceiverStatusEnum.FAIL.getCode());
        // 运营端不装载商户上下文, 必须显式赋值
        entity.setMchNo(param.getMchNo());
        allocReceiverManager.save(entity);
        this.doBind(entity);
    }

    /// 重新绑定(fail/unbound 状态), 可更换绑定所用应用(留空沿用落库值)
    @Transactional(rollbackFor = Exception.class)
    public void bind(DouyinDirectAllocReceiverBindParam param) {
        DouyinDirectAllocReceiver entity = this.loadAndCheck(param.getId());
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方无需重复绑定
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverAlreadyBound");
        }
        if (StrUtil.isNotBlank(param.getChannelAppId())) {
            // 新应用合法性由 doBind 凭证组装校验, 失败整体回滚
            entity.setChannelAppId(param.getChannelAppId());
        }
        this.doBind(entity);
    }

    /// 解绑(bound 状态), 成功后保留记录置 unbound
    @Transactional(rollbackFor = Exception.class)
    public void unbind(Long id) {
        DouyinDirectAllocReceiver entity = this.loadAndCheck(id);
        if (!Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 仅已绑定的接收方可解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverNotBound");
        }
        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildAllocReceiverConfig(
                entity.getMchNo(), entity.getChannelMchNo(), entity.getChannelAppId());
        try {
            douyinAllocReceiverChannelService.unbind(this.buildReq(entity, credential));
        } catch (Exception e) {
            // 失败原因落库(保持 bound), 异常上抛提示
            entity.setErrorMsg(e.getMessage());
            allocReceiverManager.updateById(entity);
            throw e;
        }
        entity.setStatus(AllocReceiverStatusEnum.UNBOUND.getCode())
                .setUnbindTime(OffsetDateTime.now())
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 删除(仅 fail/unbound, bound 必须先解绑)
    public void delete(Long id) {
        DouyinDirectAllocReceiver entity = this.loadAndCheck(id);
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方不可删除, 请先解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverBoundCannotDelete");
        }
        allocReceiverManager.deleteById(id);
    }

    /// 执行通道侧绑定并回写状态
    private void doBind(DouyinDirectAllocReceiver entity) {
        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildAllocReceiverConfig(
                entity.getMchNo(), entity.getChannelMchNo(), entity.getChannelAppId());
        try {
            douyinAllocReceiverChannelService.bind(this.buildReq(entity, credential));
        } catch (Exception e) {
            entity.setStatus(AllocReceiverStatusEnum.FAIL.getCode())
                    .setErrorMsg(e.getMessage());
            allocReceiverManager.updateById(entity);
            throw e;
        }
        entity.setStatus(AllocReceiverStatusEnum.BOUND.getCode())
                .setBindTime(OffsetDateTime.now())
                .setUnbindTime(null)
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 组装通道绑定请求
    private DouyinAllocReceiverReq buildReq(DouyinDirectAllocReceiver entity, DouyinSdkCredential credential) {
        return new DouyinAllocReceiverReq()
                .setReceiverType(entity.getReceiverType())
                .setReceiverAccount(entity.getReceiverAccount())
                .setReceiverName(entity.getReceiverName())
                .setRelationType(entity.getRelationType())
                .setCustomRelation(entity.getCustomRelation())
                .setCredential(credential);
    }

    /// 加载记录
    private DouyinDirectAllocReceiver loadAndCheck(Long id) {
        return allocReceiverManager.findById(id)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                        "error.common.dataNotExist", id));
    }

    /// 抖音接收方参数约束校验
    private void validateParam(String receiverType, String receiverName, String relationType, String customRelation) {
        // 类型合法性
        if (!Objects.equals(receiverType, AllocReceiverTypeEnum.MERCHANT_ID.getCode())
                && !Objects.equals(receiverType, AllocReceiverTypeEnum.PERSONAL_OPENID.getCode())) {
            // 抖音: 不支持的分账接收方类型
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.allocReceiverUnsupportedType", receiverType);
        }
        // 商户号类型必填商户全称
        if (Objects.equals(receiverType, AllocReceiverTypeEnum.MERCHANT_ID.getCode())
                && StrUtil.isBlank(receiverName)) {
            // 抖音: 商户号类型接收方必须填写商户全称
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.allocReceiverMchNameRequired");
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
