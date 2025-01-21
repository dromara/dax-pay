package org.dromara.daxpay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 前端转发
 * @author xxm
 * @since 2024/10/6
 */
@Controller
public class FrontController {

    @RequestMapping(value = {"/h5", "/h5/"})
    public ModelAndView h5IndexForward() {
        return new ModelAndView("/h5/index.html");
    }

    @RequestMapping(value = {"/web", "/web/"})
    public ModelAndView webIndexForward() {
        return new ModelAndView("/web/index.html");
    }
}
