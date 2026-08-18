package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayAllocReceiverReq;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAllocReceiverManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAllocReceiver;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAllocReceiverBindParam;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAllocReceiverCreateParam;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAllocReceiverQuery;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAllocReceiverResult;
import cn.daxpay.open.channel.alipay.service.payment.alloc.AlipayAllocReceiverChannelService;
import cn.daxpay.open.payment.trade.alloc.AllocReceiverFailSupport;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # 支付宝直连分账接收方服务
///
/// 接收方档案 CRUD 与通道侧绑定编排:
/// 新增一步绑定(落库留痕 → 组装凭证 → 调子应用 → 回写状态),
/// 绑定失败记录保留(fail)可修正后重新绑定; 解绑保留记录(unbound)。
///
/// 绑定所用应用引用([directAppRefId])落库, 重新绑定时复用组装凭证;
/// 支付宝 out_request_no 用记录 id(重试同 id 幂等)。
///
/// 事务边界: 各写方法不开事务, 落库与状态回写独立提交——失败留痕优先于原子性
/// (一步绑定模型无跨记录一致性需求, 事务包裹通道 HTTP 调用反而会把失败留痕整体回滚掉)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAllocReceiverService {

    private final AlipayDirectAllocReceiverManager allocReceiverManager;
    private final AlipayDirectChannelMerchantManager channelMerchantManager;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;
    private final AlipayAllocReceiverChannelService alipayAllocReceiverChannelService;

    /// 分页查询
    public PageResult<AlipayDirectAllocReceiverResult> page(PageParam pageParam, AlipayDirectAllocReceiverQuery query) {
        return MpUtil.toPageResult(allocReceiverManager.page(pageParam, query));
    }

    /// 新增并绑定接收方
    ///
    /// 绑定失败时记录保留(状态 fail + 失败原因), 并向调用方抛出失败异常以便即时提示。
    public void create(AlipayDirectAllocReceiverCreateParam param) {
        // 通道商户存在性与归属校验
        AlipayDirectChannelMerchant channelMerchant = channelMerchantManager.lambdaQuery()
                .eq(AlipayDirectChannelMerchant::getChannelMchNo, param.getChannelMchNo())
                .oneOpt()
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                        "error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(channelMerchant.getMchNo(), param.getMchNo())) {
            // 支付宝: 通道商户不存在或与商户号不匹配
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.alipay.channelMerchantMismatch");
        }
        // 类型合法性(USER_ID/LOGIN_NAME)
        if (!Objects.equals(param.getReceiverType(), AllocReceiverTypeEnum.USER_ID.getCode())
                && !Objects.equals(param.getReceiverType(), AllocReceiverTypeEnum.LOGIN_NAME.getCode())) {
            // 支付宝: 不支持的分账接收方类型
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.allocReceiverTypeInvalid", param.getReceiverType());
        }
        // 同通道商户同类型同账号查重(按明文哈希)
        String accountHash = SecureUtil.sha256(param.getReceiverAccount());
        if (allocReceiverManager.existsByChannelMchNoAndTypeAndHash(param.getChannelMchNo(),
                param.getReceiverType(), accountHash)) {
            // 分账接收方已存在
            throw new BizInfoException(CommonErrorCode.REPETITIVE_OPERATION_ERROR,
                    "error.channel.allocReceiverDuplicate");
        }
        AlipayDirectAllocReceiver entity = new AlipayDirectAllocReceiver()
                .setChannelMchNo(param.getChannelMchNo())
                .setReceiverType(param.getReceiverType())
                .setReceiverAccount(param.getReceiverAccount())
                .setAccountHash(accountHash)
                .setReceiverName(param.getReceiverName())
                .setDirectAppRefId(param.getAppRefId())
                .setStatus(AllocReceiverStatusEnum.FAIL.getCode());
        // 运营端不装载商户上下文, 必须显式赋值
        entity.setMchNo(param.getMchNo());
        allocReceiverManager.save(entity);
        this.doBind(entity);
    }

    /// 重新绑定(fail/unbound 状态), 可更换绑定所用应用引用(留空沿用落库值)
    public void bind(AlipayDirectAllocReceiverBindParam param) {
        AlipayDirectAllocReceiver entity = this.loadAndCheck(param.getId());
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方无需重复绑定
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverAlreadyBound");
        }
        if (param.getAppRefId() != null) {
            // 新应用合法性由 doBind 凭证组装(应用存在性+归属)校验, 失败留痕(fail + 原因)
            entity.setDirectAppRefId(param.getAppRefId());
        }
        this.doBind(entity);
    }

    /// 解绑(bound 状态), 成功后保留记录置 unbound
    public void unbind(Long id) {
        AlipayDirectAllocReceiver entity = this.loadAndCheck(id);
        if (!Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 仅已绑定的接收方可解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverNotBound");
        }
        try {
            AlipaySdkCredential credential = alipayDirectConfigAssembler.buildAllocReceiverConfig(
                    entity.getMchNo(), entity.getChannelMchNo(), entity.getDirectAppRefId());
            alipayAllocReceiverChannelService.unbind(this.buildReq(entity, credential));
        } catch (Exception e) {
            // 失败原因落库(保持 bound), HTTP 网络类异常标注"通道结果未知", 异常上抛提示
            log.warn("支付宝直连接收方解绑失败: id={}, account={}", entity.getId(), entity.getReceiverAccount(), e);
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
        AlipayDirectAllocReceiver entity = this.loadAndCheck(id);
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方不可删除, 请先解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverBoundCannotDelete");
        }
        allocReceiverManager.deleteById(id);
    }

    /// 执行通道侧绑定并回写状态
    private void doBind(AlipayDirectAllocReceiver entity) {
        try {
            AlipaySdkCredential credential = alipayDirectConfigAssembler.buildAllocReceiverConfig(
                    entity.getMchNo(), entity.getChannelMchNo(), entity.getDirectAppRefId());
            alipayAllocReceiverChannelService.bind(this.buildReq(entity, credential));
        } catch (Exception e) {
            // 失败留痕(fail + 可读原因), HTTP 网络类异常标注"通道结果未知"
            log.warn("支付宝直连接收方绑定失败: id={}, account={}", entity.getId(), entity.getReceiverAccount(), e);
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

    /// 组装通道绑定请求(out_request_no 用记录 id, 重试同 id 幂等)
    private AlipayAllocReceiverReq buildReq(AlipayDirectAllocReceiver entity, AlipaySdkCredential credential) {
        return new AlipayAllocReceiverReq()
                .setOutRequestNo(String.valueOf(entity.getId()))
                .setReceiverType(entity.getReceiverType())
                .setReceiverAccount(entity.getReceiverAccount())
                .setReceiverName(entity.getReceiverName())
                .setCredential(credential);
    }

    /// 加载记录
    private AlipayDirectAllocReceiver loadAndCheck(Long id) {
        return allocReceiverManager.findById(id)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                        "error.common.dataNotExist", id));
    }
}
