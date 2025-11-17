package org.dromara.daxpay.payment.isv.service.info;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import org.dromara.daxpay.payment.common.exception.ConfigNotExistException;
import org.dromara.daxpay.payment.common.exception.OperationFailException;
import org.dromara.daxpay.payment.isv.convert.info.IsvInfoConvert;
import org.dromara.daxpay.payment.isv.dao.config.IsvChannelConfigManager;
import org.dromara.daxpay.payment.isv.dao.isv.IsvInfoManager;
import org.dromara.daxpay.payment.isv.entity.config.IsvChannelConfig;
import org.dromara.daxpay.payment.isv.entity.info.IsvInfo;
import org.dromara.daxpay.payment.isv.param.isv.IsvInfoParam;
import org.dromara.daxpay.payment.isv.param.isv.IsvInfoQuery;
import org.dromara.daxpay.payment.isv.result.info.IsvInfoResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务商
 * @author xxm
 * @since 2024/10/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvInfoService {
    private final IsvInfoManager isvInfoManager;
    private final IsvChannelConfigManager isvChannelConfigManager;

    /**
     * 添加服务商
     */
    public void add(IsvInfoParam param) {
        IsvInfo entity = IsvInfoConvert.CONVERT.toEntity(param);
        // 生成服务商号
        entity.setIsvNo(this.genIsvNo());
        isvInfoManager.save(entity);
    }
    /**
     * 修改
     */
    public void update(IsvInfoParam param) {
        IsvInfo isvInfo = isvInfoManager.findById(param.getId()).orElseThrow(ConfigNotExistException::new);
        BeanUtil.copyProperties(param, isvInfo, CopyOptions.create().ignoreNullValue());
        isvInfoManager.updateById(isvInfo);
    }

    /**
     * 分页
     */
    public PageResult<IsvInfoResult> page(PageParam pageParam, IsvInfoQuery query) {
        return MpUtil.toPageResult(isvInfoManager.page(pageParam, query));
    }

    /**
     * 获取单条
     */
    public IsvInfoResult findById(Long id) {
        return isvInfoManager.findById(id).map(IsvInfo::toResult).orElseThrow(ConfigNotExistException::new);
    }

    /**
     * 下拉列表
     */
    public List<LabelValue> dropdown(){
        return isvInfoManager.findAll().stream()
                .map(o->new LabelValue(o.getName(),o.getIsvNo()))
                .collect(Collectors.toList());
    }

    /**
     * 启用的下拉列表
     */
    public List<LabelValue> dropdownByEnable() {
        return isvInfoManager.findAllByStatus(IsvStatusEnum.ENABLE).stream()
                .map(o->new LabelValue(o.getName(),o.getIsvNo()))
                .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        if (true){
            throw new OperationFailException("服务商不允许被删除");
        }
        IsvInfo isvInfo = isvInfoManager.findById(id).orElseThrow(()-> new DataNotExistException("服务商通道不存在"));
        // 查看是否有配置的服务商通道配置
        if (isvChannelConfigManager.existedByField(IsvChannelConfig::getIsvNo, isvInfo.getIsvNo())){
            throw new OperationFailException("该服务商已绑定通道配置，无法删除");
        }
        isvInfoManager.deleteById(id);
    }

    /**
     * 生成服务商号
     */
    private String genIsvNo() {
        String isvNo = "ISV"+RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++){
            if (!isvInfoManager.existedByField(IsvInfo::getIsvNo, isvNo)){
                return isvNo;
            }
            isvNo = "ISV"+ RandomUtil.randomNumbers(16);
        }
        throw new BizException("服务商生成失败");
    }

}
