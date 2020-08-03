package wiki.IceCream.yuq.demo.controller.web;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.IceCreamQAQ.YuWeb.annotation.WebController;
import com.icecreamqaq.yuq.YuQ;
import com.icecreamqaq.yuq.message.Message;
import lombok.val;
import wiki.IceCream.yuq.demo.entity.QqGroup;
import wiki.IceCream.yuq.demo.serivce.GroupService;

import javax.inject.Inject;

@WebController
public class WebAdminController {

    @Inject
    private YuQ yuq;

    @Inject
    private GroupService service;

    @Action("groupMessage")
    public String groupMessage(String group, String msg) {
        val g = yuq.getGroups().get(Long.parseLong(group));
        if (g == null) return "Error: Group Not Found!";
        g.sendMessage(new Message().plus(msg));
        return "OK!";
    }

    @Action("privMessage")
    public String privMessage(String qq, String msg) {
        val f = yuq.getFriends().get(Long.parseLong(qq));
        if (f == null) return "Error: Friend Not Found!";
        f.sendMessage(new Message().plus(msg));
        return "OK!";
    }

    @Action("tempMessage")
    public String tempMessage(String qq, String group, String msg) {
        val g = yuq.getGroups().get(Long.parseLong(group));
        if (g == null) return "Error: Group Not Found!";
        val m = g.getMembers().get(Long.parseLong(qq));
        if (m == null) return "Error: Member Not Found!";
        m.sendMessage(new Message().plus(msg));
        return "OK!";
    }

    @Action("groupInfo")
    public QqGroup groupInfo(String group) {
        return service.getGroupByGroupId(Long.parseLong(group));
    }

}
