package cn.bootx.platform.notice.core.sms.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.notice.core.sms.dao.SmsTemplateManager;
import cn.bootx.platform.notice.core.sms.entity.SmsTemplate;
import cn.bootx.platform.notice.dto.sms.SmsTemplateDto;
import cn.bootx.platform.notice.param.sms.SmsTemplateParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信模板配置
 * @author xxm
 * @since 2023-08-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsTemplateService {
    private final SmsTemplateManager smsTemplateManager;

    /**
     * 添加
     */
    public void add(SmsTemplateParam param){
        SmsTemplate smsTemplate = SmsTemplate.init(param);
        smsTemplateManager.save(smsTemplate);
    }

    /**
     * 修改
     */
    public void update(SmsTemplateParam param){
        SmsTemplate smsTemplate = smsTemplateManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,smsTemplate, CopyOptions.create().ignoreNullValue());
        smsTemplateManager.updateById(smsTemplate);
    }

    /**
     * 分页
     */
    public PageResult<SmsTemplateDto> page(PageParam pageParam,SmsTemplateParam smsTemplateParam){
        return MpUtil.convert2DtoPageResult(smsTemplateManager.page(pageParam,smsTemplateParam));
    }

    /**
     * 获取单条
     */
    public SmsTemplateDto findById(Long id){
        return smsTemplateManager.findById(id).map(SmsTemplate::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<SmsTemplateDto> findAll(){
        return ResultConvertUtil.dtoListConvert(smsTemplateManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id){
        smsTemplateManager.deleteById(id);
    }
}
