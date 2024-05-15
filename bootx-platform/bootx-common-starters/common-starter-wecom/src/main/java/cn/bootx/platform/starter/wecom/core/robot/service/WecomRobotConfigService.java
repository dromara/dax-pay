package cn.bootx.platform.starter.wecom.core.robot.service;

import cn.bootx.platform.starter.wecom.param.robot.WecomRobotConfigParam;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.wecom.core.robot.dao.WecomRobotConfigManager;
import cn.bootx.platform.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.bootx.platform.starter.wecom.dto.robot.WecomRobotConfigDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WecomRobotConfigService {

    private final WecomRobotConfigManager robotConfigManager;

    /**
     * 添加
     */
    public void add(WecomRobotConfigParam param) {
        WecomRobotConfig wecomRobotConfig = WecomRobotConfig.init(param);
        robotConfigManager.save(wecomRobotConfig);
    }

    /**
     * 修改
     */
    public void update(WecomRobotConfigParam param) {
        WecomRobotConfig wecomRobotConfig = robotConfigManager.findById(param.getId())
            .orElseThrow(DataNotExistException::new);

        BeanUtil.copyProperties(param, wecomRobotConfig, CopyOptions.create().ignoreNullValue());
        robotConfigManager.updateById(wecomRobotConfig);
    }

    /**
     * 分页
     */
    public PageResult<WecomRobotConfigDto> page(PageParam pageParam, WecomRobotConfigParam wecomRobotConfigParam) {
        return MpUtil.convert2DtoPageResult(robotConfigManager.page(pageParam, wecomRobotConfigParam));
    }

    /**
     * 获取单条
     */
    public WecomRobotConfigDto findById(Long id) {
        return robotConfigManager.findById(id).map(WecomRobotConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<WecomRobotConfigDto> findAll() {
        return ResultConvertUtil.dtoListConvert(robotConfigManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        robotConfigManager.deleteById(id);
    }

    /**
     * 编码是否已经存在
     */
    public boolean existsByCode(String code) {
        return robotConfigManager.existsByCode(code);
    }

    /**
     * 编码是否已经存在(不包含自身)
     */
    public boolean existsByCode(String code, Long id) {
        return robotConfigManager.existsByCode(code, id);
    }

}
