package store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by li on 2016/8/23.
 */
@Controller
public class SessionController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String index(HttpServletRequest request) {
        request.getSession().setAttribute("email", "zombie.jun.li@aliyun.com");
        return "index";
    }

    @RequestMapping(value = "/change-session-id", method = RequestMethod.GET)
    @ResponseBody
    public String changeSessionId(HttpServletRequest request) {
        request.changeSessionId();
        return "changeSessionId";
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    @ResponseBody
    public String invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (null != session)
            session.invalidate();
        return "invalidate";
    }
}
