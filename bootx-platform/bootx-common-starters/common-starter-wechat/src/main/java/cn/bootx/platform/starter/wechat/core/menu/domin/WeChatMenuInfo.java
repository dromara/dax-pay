package cn.bootx.platform.starter.wechat.core.menu.domin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022/8/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信自定义菜单")
public class WeChatMenuInfo {

    private List<Button> buttons = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    @Schema(title = "菜单按钮")
    public static class Button {

        /**
         * <pre>
         * 菜单的响应动作类型.
         * view表示网页类型，
         * click表示点击类型，
         * miniprogram表示小程序类型
         * </pre>
         */
        private String type;

        /**
         * 菜单标题，不超过16个字节，子菜单不超过60个字节.
         */
        private String name;

        /**
         * <pre>
         * 菜单KEY值，用于消息接口推送，不超过128字节.
         * click等点击类型必须
         * </pre>
         */
        private String key;

        /**
         * <pre>
         * 网页链接.
         * 用户点击菜单可打开链接，不超过1024字节。type为miniprogram时，不支持小程序的老版本客户端将打开本url。
         * view、miniprogram类型必须
         * </pre>
         */
        private String url;

        /**
         * <pre>
         * 调用新增永久素材接口返回的合法media_id.
         * media_id类型和view_limited类型必须
         * </pre>
         */
        private String mediaId;

        /**
         * <pre>
         * 调用发布图文接口获得的article_id.
         * article_id类型和article_view_limited类型必须
         * </pre>
         */
        private String articleId;

        /**
         * <pre>
         * 小程序的appid.
         * miniprogram类型必须
         * </pre>
         */
        private String appId;

        /**
         * <pre>
         * 小程序的页面路径.
         * miniprogram类型必须
         * </pre>
         */
        private String pagePath;

        private List<Button> subButtons = new ArrayList<>();

    }

    /**
     * 转换成wxJava的对象
     */
    public WxMenu toWxMenu() {
        WxMenu wxMenu = new WxMenu();
        List<WxMenuButton> collect = this.getButtons().stream().map(this::toWxButton).collect(Collectors.toList());
        wxMenu.setButtons(collect);
        return wxMenu;
    }

    private WxMenuButton toWxButton(Button button) {
        List<WxMenuButton> subButtons = button.getSubButtons()
            .stream()
            .map(this::toWxButton)
            .collect(Collectors.toList());
        WxMenuButton wxMenuButton = new WxMenuButton();
        wxMenuButton.setType(button.getType());
        wxMenuButton.setName(button.getName());
        wxMenuButton.setKey(button.getKey());
        wxMenuButton.setUrl(button.getUrl());
        wxMenuButton.setMediaId(button.getMediaId());
        wxMenuButton.setArticleId(button.getArticleId());
        wxMenuButton.setAppId(button.getAppId());
        wxMenuButton.setPagePath(button.getPagePath());
        wxMenuButton.setSubButtons(subButtons);
        return wxMenuButton;
    }

    /**
     * 从WxJava对象转成
     */
    public static WeChatMenuInfo init(WxMpMenu wxMpMenu) {
        WeChatMenuInfo weChatMenuInfo = new WeChatMenuInfo();
        List<Button> buttons = wxMpMenu.getMenu()
            .getButtons()
            .stream()
            .map(WeChatMenuInfo::initButton)
            .collect(Collectors.toList());

        weChatMenuInfo.setButtons(buttons);
        return weChatMenuInfo;
    }

    /**
     * 菜单按钮转换
     */
    private static Button initButton(WxMenuButton wxMenuButton) {
        List<Button> subButtons = wxMenuButton.getSubButtons()
            .stream()
            .map(WeChatMenuInfo::initButton)
            .collect(Collectors.toList());
        Button button = new Button();
        button.setType(wxMenuButton.getType());
        button.setName(wxMenuButton.getName());
        button.setKey(wxMenuButton.getKey());
        button.setUrl(wxMenuButton.getUrl());
        button.setMediaId(wxMenuButton.getMediaId());
        button.setArticleId(wxMenuButton.getArticleId());
        button.setAppId(wxMenuButton.getAppId());
        button.setPagePath(wxMenuButton.getPagePath());
        button.setSubButtons(subButtons);
        return button;
    }

}
