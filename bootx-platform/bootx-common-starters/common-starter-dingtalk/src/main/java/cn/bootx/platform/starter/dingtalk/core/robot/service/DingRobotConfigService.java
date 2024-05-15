package cn.bootx.platform.starter.dingtalk.core.robot.service;

import cn.bootx.platform.starter.dingtalk.param.robot.DingRobotConfigParam;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.dingtalk.core.robot.dao.DingRobotConfigManager;
import cn.bootx.platform.starter.dingtalk.core.robot.entity.DingRobotConfig;
import cn.bootx.platform.starter.dingtalk.dto.robot.DingRobotConfigDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 钉钉机器人消息发送
 *
 * @author xxm
 * @since 2020/11/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingRobotConfigService {

    private final DingRobotConfigManager robotConfigManager;

    /**
     * 添加新配置
     */
    @Transactional(rollbackFor = Exception.class)
    public DingRobotConfigDto add(DingRobotConfigParam param) {
        if (robotConfigManager.existsByCode(param.getCode())) {
            throw new BizException("code重复");
        }

        DingRobotConfig dingRobotConfig = DingRobotConfig.init(param);

        return robotConfigManager.save(dingRobotConfig).toDto();
    }

    /**
     * 更新钉钉机器人配置
     */
    @Transactional(rollbackFor = Exception.class)
    public DingRobotConfigDto update(DingRobotConfigParam param) {
        DingRobotConfig dingRobotConfig = robotConfigManager.findById(param.getId())
            .orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, dingRobotConfig, CopyOptions.create().ignoreNullValue());
        return robotConfigManager.updateById(dingRobotConfig).toDto();
    }

    /**
     * 获取所有配置
     */
    public List<DingRobotConfigDto> findAll() {
        return ResultConvertUtil.dtoListConvert(robotConfigManager.findAll());
    }

    /**
     * 获取所有配置
     */
    public PageResult<DingRobotConfigDto> page(PageParam pageParam, DingRobotConfigParam param) {
        return MpUtil.convert2DtoPageResult(robotConfigManager.page(pageParam, param));
    }

    /**
     * 根据 id 获取相应的配置
     */
    public DingRobotConfigDto findById(Long id) {
        return ResultConvertUtil.dtoConvert(robotConfigManager.findById(id));
    }

    /**
     * 根据 id 删除相应的机器人配置
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
