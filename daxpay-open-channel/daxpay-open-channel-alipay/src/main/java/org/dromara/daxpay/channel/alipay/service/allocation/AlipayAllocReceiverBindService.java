package org.dromara.daxpay.channel.alipay.service.allocation;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.dao.allocation.AlipayAllocReceiverBindManager;
import org.dromara.daxpay.channel.alipay.entity.allocation.AlipayAllocReceiverBind;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverBindParam;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverBindQuery;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverBindResult;
import org.dromara.daxpay.channel.alipay.service.payment.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.exception.ConfigErrorException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationUnbindModel;
import com.alipay.api.domain.RoyaltyEntity;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBindRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationUnbindRequest;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBindResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationUnbindResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 支付宝分账接收方绑定服务类
 * @author xxm
 * @since 2025/1/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAllocReceiverBindService {

    private final AlipayConfigService alipayConfigService;

    private final AlipayAllocReceiverBindManager receiverBindManager;

    private final PaymentAssistService paymentAssistService;

    /**
     * 分页
     */
    public PageResult<AlipayAllocReceiverBindResult> page(PageParam pageParam, AlipayAllocReceiverBindQuery query){
        return MpUtil.toPageResult(receiverBindManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public AlipayAllocReceiverBindResult findById(Long id){
        return receiverBindManager.findById(id).map(AlipayAllocReceiverBind::toResult).orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
    }

    /**
     * 添加
     */
    public void add(AlipayAllocReceiverBindParam param){
        // 校验
        if (!this.validation(param)){
            throw new ValidationFailedException("分账接收者参数未通过校验");
        }
        paymentAssistService.initMchAndApp(param.getAppId());
        var receiverBind = AlipayAllocReceiverBind.init(param);
        receiverBindManager.save(receiverBind);
    }

    /**
     * 编辑
     */
    public void update(AlipayAllocReceiverBindParam param){
        var receiverBind = receiverBindManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
        if (receiverBind.isBind()){
            throw new DataErrorException("分账接收方已绑定");
        }
        BeanUtil.copyProperties(param,receiverBind, CopyOptions.create().ignoreNullValue());
        receiverBindManager.updateById(receiverBind);
    }

    /**
     * 绑定关系
     */
    public void bind(Long id){
        var receiverBind = receiverBindManager.findById(id).orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
        if (receiverBind.isBind()){
            throw new DataErrorException("分账接收方已绑定");
        }
        paymentAssistService.initMchAndApp(receiverBind.getMchNo(),receiverBind.getAppId());
        var aliPayConfig = alipayConfigService.getAndCheckConfig(receiverBind.isIsv());
        var model = new AlipayTradeRoyaltyRelationBindModel();
        RoyaltyEntity entity = new RoyaltyEntity();
        var receiverTypeEnum = AllocReceiverTypeEnum.findByCode(receiverBind.getReceiverType());
        entity.setType(this.getReceiverType(receiverTypeEnum));
        entity.setAccount(receiverBind.getReceiverAccount());
        entity.setName(receiverBind.getReceiverName());
        entity.setMemo(receiverBind.getRelationName());

        // 不报错视为同步成功
        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationBindRequest request = new AlipayTradeRoyaltyRelationBindRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        model.setOutRequestNo(String.valueOf(receiverBind.getId()));
        request.setBizModel(model);
        AlipayTradeRoyaltyRelationBindResponse response = null;
        try {
            response = alipayConfigService.execute(request,aliPayConfig);
        } catch (AlipayApiException e) {
            log.error("支付宝分账接收方绑定失败", e);
            throw new TradeFailException("支付宝分账接收方绑定失败: "+e.getMessage());
        }
        String errorMsg = this.verifyErrorMsg(response);
        if (StrUtil.isBlank(errorMsg)){
            receiverBind.setBind(true);
        }
        receiverBind.setErrorMsg(errorMsg);
        receiverBindManager.updateById(receiverBind);
    }

    /**
     * 解绑关系
     */
    public void unbind(Long id){
        var receiverBind = receiverBindManager.findById(id).orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
        if (!receiverBind.isBind()){
            throw new DataErrorException("分账接收方已解绑");
        }
        paymentAssistService.initMchAndApp(receiverBind.getMchNo(),receiverBind.getAppId());
        var aliPayConfig = alipayConfigService.getAndCheckConfig(receiverBind.isIsv());

        AlipayTradeRoyaltyRelationUnbindModel model = new AlipayTradeRoyaltyRelationUnbindModel();
        model.setOutRequestNo(String.valueOf(receiverBind.getId()));

        RoyaltyEntity entity = new RoyaltyEntity();

        var receiverTypeEnum = AllocReceiverTypeEnum.findByCode(receiverBind.getReceiverType());
        entity.setType(this.getReceiverType(receiverTypeEnum));
        entity.setAccount(receiverBind.getReceiverAccount());

        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationUnbindRequest request = new AlipayTradeRoyaltyRelationUnbindRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        AlipayTradeRoyaltyRelationUnbindResponse response;
        try {
            response = alipayConfigService.execute(request,aliPayConfig);
        } catch (AlipayApiException e) {
            log.error("支付宝分账接收方解绑失败", e);
            throw new TradeFailException("支付宝分账接收方解绑失败: "+e.getMessage());
        }
        // 如果出现分账方不存在也视为成功
        if (Objects.equals(response.getSubCode(), AlipayCode.USER_NOT_EXIST)) {
            return;
        }
        String errorMsg = this.verifyErrorMsg(response);
        if (StrUtil.isBlank(errorMsg)){
            receiverBind.setBind(false);
        }
        receiverBind.setErrorMsg(errorMsg);
        receiverBindManager.updateById(receiverBind);
    }

    /**
     * 验证错误信息
     */
    private String verifyErrorMsg(AlipayResponse alipayResponse) {
        if (!Objects.equals(alipayResponse.getCode(), AlipayCode.ResponseCode.SUCCESS)) {
            String errorMsg = alipayResponse.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = alipayResponse.getMsg();
            }
            return errorMsg;
        }
        return null;
    }

    /**
     * 删除
     */
    public void remove(Long id){
        var receiverBind = receiverBindManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
        if (receiverBind.isBind()){
            throw new DataErrorException("分账接收方已绑定, 无法删除");
        }
    }

    /**
     * 获取分账接收方类型编码
     */
    private String getReceiverType(AllocReceiverTypeEnum receiverTypeEnum){
        if (receiverTypeEnum == AllocReceiverTypeEnum.USER_ID){
            return "userId";
        }
        if (receiverTypeEnum == AllocReceiverTypeEnum.OPEN_ID){
            return "openId";
        }
        if (receiverTypeEnum == AllocReceiverTypeEnum.LOGIN_NAME){
            return "loginName";
        }
        throw new ConfigErrorException("分账接收方类型错误");
    }

    /**
     * 校验
     */
    private boolean validation(AlipayAllocReceiverBindParam allocReceiver){
        List<String> list = Arrays.asList(AllocReceiverTypeEnum.USER_ID.getCode(), AllocReceiverTypeEnum.OPEN_ID.getCode(), AllocReceiverTypeEnum.LOGIN_NAME.getCode());
        String receiverType = allocReceiver.getReceiverType();
        return list.contains(receiverType);
    }
}
