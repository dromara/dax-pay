package cn.bootx.daxpay.core.paymodel.alipay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.daxpay.code.paymodel.AliPayWay;
import cn.bootx.daxpay.core.paymodel.alipay.dao.AlipayConfigManager;
import cn.bootx.daxpay.core.paymodel.alipay.entity.AlipayConfig;
import cn.bootx.daxpay.dto.paymodel.alipay.AlipayConfigDto;
import cn.bootx.daxpay.exception.payment.PayFailureException;
import cn.bootx.daxpay.param.paymodel.alipay.AlipayConfigParam;
import cn.bootx.daxpay.param.paymodel.alipay.AlipayConfigQuery;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付宝支付
 *
 * @author xxm
 * @date 2020/12/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayConfigService {

    private final AlipayConfigManager alipayConfigManager;

    /**
     * 添加支付宝配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(AlipayConfigParam param) {
        AlipayConfig alipayConfig = AlipayConfig.init(param);
        alipayConfig.setActivity(false).setState(1);
        alipayConfigManager.save(alipayConfig);
    }

    /**
     * 设置启用的支付宝配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUpActivity(Long id) {
        AlipayConfig alipayConfig = alipayConfigManager.findById(id).orElseThrow(DataNotExistException::new);
        if (Objects.equals(alipayConfig.getActivity(), Boolean.TRUE)) {
            return;
        }
        alipayConfigManager.removeAllActivity();
        alipayConfig.setActivity(true);
        alipayConfigManager.updateById(alipayConfig);
    }

    /**
     * 清除启用状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearActivity(Long id) {
        AlipayConfig alipayConfig = alipayConfigManager.findById(id)
            .orElseThrow(() -> new PayFailureException("支付宝配置不存在"));
        if (Objects.equals(alipayConfig.getActivity(), Boolean.FALSE)) {
            return;
        }
        alipayConfig.setActivity(false);
        alipayConfigManager.updateById(alipayConfig);
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AlipayConfigParam param) {
        AlipayConfig alipayConfig = alipayConfigManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, alipayConfig, CopyOptions.create().ignoreNullValue());
        // 支付方式
        if (CollUtil.isNotEmpty(param.getPayWayList())) {
            alipayConfig.setPayWays(String.join(",", param.getPayWayList()));
        }
        else {
            alipayConfig.setPayWays(null);
        }
        alipayConfigManager.updateById(alipayConfig);
    }

    /**
     * 获取
     */
    public AlipayConfigDto findById(Long id) {
        return alipayConfigManager.findById(id).map(AlipayConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    public PageResult<AlipayConfigDto> page(PageParam pageParam, AlipayConfigQuery param) {
        return MpUtil.convert2DtoPageResult(alipayConfigManager.page(pageParam, param));
    }

    /**
     * 支付宝支持支付方式
     */
    public List<KeyValue> findPayWayList() {
        return AliPayWay.getPayWays()
            .stream()
            .map(e -> new KeyValue(e.getCode(), e.getName()))
            .collect(Collectors.toList());
    }

}
