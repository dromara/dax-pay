package cn.bootx.platform.notice.core.mail.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.notice.exception.MailConfigCodeAlreadyExistedException;
import cn.bootx.platform.notice.exception.MailConfigNotExistException;
import cn.bootx.platform.notice.param.mail.MailConfigParam;
import cn.bootx.platform.notice.core.mail.dao.MailConfigManager;
import cn.bootx.platform.notice.core.mail.entity.MailConfig;
import cn.bootx.platform.notice.dto.mail.MailConfigDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮件配置
 *
 * @author xxm
 * @since 2020/4/8 13:29
 */
@Service
@AllArgsConstructor
public class MailConfigService {

    private final MailConfigManager mailConfigManager;

    /**
     * 添加新邮箱配置
     */
    @Transactional(rollbackFor = Exception.class)
    public MailConfigDto add(MailConfigParam param) {
        if (mailConfigManager.existsByCode(param.getCode())) {
            throw new MailConfigCodeAlreadyExistedException();
        }

        MailConfig mailConfig = MailConfig.init(param);
        return mailConfigManager.save(mailConfig).toDto();
    }

    /**
     * 更新邮箱配置
     */
    @Transactional(rollbackFor = Exception.class)
    public MailConfigDto update(MailConfigParam param) {
        MailConfig mailConfig = mailConfigManager.findById(param.getId()).orElseThrow(MailConfigNotExistException::new);

        if (mailConfigManager.existsByCode(param.getCode(), param.getId())) {
            throw new MailConfigCodeAlreadyExistedException();
        }

        BeanUtil.copyProperties(param, mailConfig, CopyOptions.create().ignoreNullValue());
        return mailConfigManager.updateById(mailConfig).toDto();
    }

    /**
     * 根据 id 获取相应的邮箱配置
     */
    public MailConfigDto findById(Long id) {
        return ResultConvertUtil.dtoConvert(mailConfigManager.findById(id));
    }

    /**
     * 获取 默认邮箱配置
     */
    public MailConfigDto getDefaultConfig() {
        return ResultConvertUtil.dtoConvert(mailConfigManager.findByActivity());
    }

    /**
     * 分页
     */
    public PageResult<MailConfigDto> page(PageParam pageParam, MailConfigParam param) {
        return MpUtil.convert2DtoPageResult(mailConfigManager.page(pageParam, param));
    }

    /**
     * 根据 code 获取相应的邮箱配置
     */
    public MailConfigDto getByCode(String code) {
        return ResultConvertUtil.dtoConvert(mailConfigManager.findByCode(code));
    }

    /**
     * 编码是否已经存在
     */
    public boolean existsByCode(String code) {
        return mailConfigManager.existsByCode(code);
    }

    /**
     * 编码是否已经存在(不包含自身)
     */
    public boolean existsByCode(String code, Long id) {
        return mailConfigManager.existsByCode(code, id);
    }

    /**
     * 设置活动邮箱
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUpActivity(Long id) {
        MailConfig mailConfig = mailConfigManager.findById(id).orElseThrow(MailConfigNotExistException::new);
        mailConfig.setActivity(true);
        mailConfigManager.removeAllActivity();
        mailConfigManager.updateById(mailConfig);
    }

    /**
     * 根据 id 删除相应的邮箱配置
     */
    public void delete(Long id) {
        mailConfigManager.deleteById(id);
    }

}
