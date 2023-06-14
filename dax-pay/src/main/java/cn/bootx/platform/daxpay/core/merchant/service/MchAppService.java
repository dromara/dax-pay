package cn.bootx.platform.daxpay.core.merchant.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.merchant.dao.MchAppManager;
import cn.bootx.platform.daxpay.core.merchant.dao.MerchantInfoManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchApplication;
import cn.bootx.platform.daxpay.core.merchant.entity.MerchantInfo;
import cn.bootx.platform.daxpay.dto.merchant.MchApplicationDto;
import cn.bootx.platform.daxpay.param.merchant.MchApplicationParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 商户应用
 *
 * @author xxm
 * @date 2023-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppService {

    private final MchAppManager mchAppManager;

    private final MerchantInfoManager mchManager;

    private final MchAppPayConfigService appPayConfigService;

    /**
     * 添加
     */
    public void add(MchApplicationParam param) {
        MchApplication mchApp = MchApplication.init(param);
        mchApp.setCode(IdUtil.getSnowflakeNextIdStr());
        mchAppManager.save(mchApp);
    }

    /**
     * 修改
     */
    public void update(MchApplicationParam param) {
        MchApplication mchApp = mchAppManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, mchApp, CopyOptions.create().ignoreNullValue());
        mchAppManager.updateById(mchApp);
    }

    /**
     * 分页
     */
    public PageResult<MchApplicationDto> page(PageParam pageParam, MchApplicationParam mchApplicationParam) {
        return MpUtil.convert2DtoPageResult(mchAppManager.page(pageParam, mchApplicationParam));
    }

    /**
     * 获取单条
     */
    public MchApplicationDto findById(Long id) {
        return mchAppManager.findById(id).map(MchApplication::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<MchApplicationDto> findAll() {
        return ResultConvertUtil.dtoListConvert(mchAppManager.findAll());
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        appPayConfigService.deleteByAppId(id);
        mchAppManager.deleteById(id);
    }

    /**
     * 验证商户号和商户应用是否匹配
     */
    public boolean checkMatch(String mchCode, String mchAppCode) {
        MerchantInfo merchantInfo = mchManager.findByCode(mchCode).orElseThrow(DataNotExistException::new);
        MchApplication mchApp = mchAppManager.findByCode(mchAppCode).orElseThrow(DataNotExistException::new);

        // 商户与应用是否有关联关系
        if (!Objects.equals(mchApp.getMchCode(), merchantInfo.getCode())) {
            return false;
        }
        return true;
    }

}
