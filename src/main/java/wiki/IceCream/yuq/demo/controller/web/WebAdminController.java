package wiki.IceCream.yuq.demo.controller.web;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.IceCreamQAQ.YuWeb.annotation.WebController;
import com.icecreamqaq.yuq.YuQ;

import javax.inject.Inject;

@WebController
public class WebAdminController {

    @Inject
    private YuQ yuq;

    @Action("groupMessage")
    public String groupMessage(String group, String msg) {
        yuq.sendMessage(yuq.getMessageFactory().newGroup(Long.parseLong(group)).plus(msg));
        return "OK!";
    }

    @Action("privMessage")
    public String privMessage(String qq, String msg) {
        yuq.sendMessage(yuq.getMessageFactory().newPrivate(Long.parseLong(qq)).plus(msg));
        return "OK!";
    }

    @Action("tempMessage")
    public String tempMessage(String qq, String group, String msg) {
        yuq.sendMessage(yuq.getMessageFactory().newTemp(Long.parseLong(group), Long.parseLong(qq)).plus(msg));
        return "OK!";
    }

}
