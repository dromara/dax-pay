package cn.bootx.platform.starter.wechat.core.menu.service;

import cn.bootx.platform.starter.wechat.param.menu.WeChatMenuParam;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.wechat.core.menu.dao.WeChatMenuManager;
import cn.bootx.platform.starter.wechat.core.menu.domin.WeChatMenuInfo;
import cn.bootx.platform.starter.wechat.core.menu.entity.WeChatMenu;
import cn.bootx.platform.starter.wechat.dto.menu.WeChatMenuDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信菜单
 *
 * @author xxm
 * @since 2022/8/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatMenuService {

    private final WxMpService wxMpService;

    private final WeChatMenuManager weChatMenuManager;

    /**
     * 添加
     */
    public void add(WeChatMenuParam param) {
        WeChatMenu weChatMenu = WeChatMenu.init(param);
        weChatMenuManager.save(weChatMenu);
    }

    /**
     * 修改
     */
    public void update(WeChatMenuParam param) {
        WeChatMenu weChatMenu = weChatMenuManager.findById(param.getId()).orElseThrow(DataNotExistException::new);

        BeanUtil.copyProperties(param, weChatMenu, CopyOptions.create().ignoreNullValue());
        weChatMenuManager.updateById(weChatMenu);
    }

    /**
     * 分页
     */
    public PageResult<WeChatMenuDto> page(PageParam pageParam, WeChatMenuParam weChatMenuParam) {
        return MpUtil.convert2DtoPageResult(weChatMenuManager.page(pageParam, weChatMenuParam));
    }

    /**
     * 获取单条
     */
    public WeChatMenuDto findById(Long id) {
        return weChatMenuManager.findById(id).map(WeChatMenu::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<WeChatMenuDto> findAll() {
        return ResultConvertUtil.dtoListConvert(weChatMenuManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        weChatMenuManager.deleteById(id);
    }

    /**
     * 发布菜单
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id) {
        WeChatMenu weChatMenu = weChatMenuManager.findById(id).orElseThrow(() -> new DataNotExistException("菜单信息不存在"));
        WxMenu wxMenu = weChatMenu.getMenuInfo().toWxMenu();
        WxMpMenuService menuService = wxMpService.getMenuService();
        menuService.menuCreate(wxMenu);
        weChatMenu.setPublish(true);
        weChatMenuManager.clearPublish();
        weChatMenuManager.updateById(weChatMenu);
    }

    /**
     * 导入当前微信菜单
     */
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void importMenu() {
        WxMpMenuService menuService = wxMpService.getMenuService();
        WxMpMenu wxMpMenu = menuService.menuGet();
        WeChatMenuInfo weChatMenuInfo = WeChatMenuInfo.init(wxMpMenu);
        WeChatMenu weChatMenu = new WeChatMenu().setName("微信自定义菜单")
            .setRemark("导入时间" + DateUtil.now())
            .setPublish(true)
            .setMenuInfo(weChatMenuInfo);
        weChatMenuManager.clearPublish();
        weChatMenuManager.save(weChatMenu);
    }

    /**
     * 清空菜单
     */
    @SneakyThrows
    public void clearMenu() {
        WxMpMenuService menuService = wxMpService.getMenuService();
        menuService.menuDelete();
    }

}
