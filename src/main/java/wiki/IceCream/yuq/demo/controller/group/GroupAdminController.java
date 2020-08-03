package wiki.IceCream.yuq.demo.controller.group;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.IceCreamQAQ.Yu.annotation.Before;
import com.IceCreamQAQ.Yu.annotation.Path;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.annotation.GroupController;
import com.icecreamqaq.yuq.entity.Member;
import lombok.val;
import wiki.IceCream.yuq.demo.entity.QqGroup;
import wiki.IceCream.yuq.demo.serivce.GroupService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Path("群")
@GroupController
public class GroupAdminController {

    @Inject
    private GroupService service;

    @Before(except = "init")
    public QqGroup before(Member qq) {
        if (qq.isAdmin()) {
            val qqGroup = service.getGroupByGroupId(qq.getGroup().getId());
            if (qqGroup == null) throw FunKt.toMessage("群信息不存在！请群主执行\"群 初始化信息\"命令！");
            if (!qq.getGroup().getBot().isAdmin()) throw FunKt.toMessage("所有命令只有当机器人为管理员时生效！");
            return qqGroup;
        }
        throw FunKt.toMessage("你没有使用该命令的权限！");
    }

    @Action("初始化信息")
    public String init(Member qq) {
        if (qq.isOwner()) {
            QqGroup qqGroup = service.getGroupByGroupId(qq.getGroup().getId());
            if (qqGroup != null) return "群信息已经存在，无需再次初始化！";
            qqGroup = new QqGroup();

            qqGroup.setGroupId(qq.getGroup().getId());
            qqGroup.setAd(false);
            qqGroup.setRecall(false);
            qqGroup.setAutoAccept(false);
            qqGroup.setAdKeyList(new ArrayList<>());

            service.save(qqGroup);
            return "群信息初始化完毕！";
        }
        return "你没有使用该命令的权限！该命令仅限群主使用！";
    }

    @Action("撤回监控 {flag}")
    public String recall(boolean flag, QqGroup qqGroup) {
        qqGroup.setRecall(flag);
        service.update(qqGroup);
        if (flag) return "撤回监控已经启用！";
        return "撤回监控已经禁用！";
    }

    @Action("广告监控 {flag}")
    public String ad(boolean flag, QqGroup qqGroup) {
        qqGroup.setAd(flag);
        service.update(qqGroup);
        if (flag) return "广告监控已经启用！";
        return "广告监控已经禁用！";
    }

    @Action("自动同意 {flag}")
    public String accept(boolean flag, QqGroup qqGroup) {
        qqGroup.setAutoAccept(flag);
        service.update(qqGroup);
        if (flag) return "自动同意已经启用！";
        return "自动同意已经禁用！";
    }

    @Action("广告关键词 添加 {keyword}")
    public String addKeyword(String keyword, QqGroup qqGroup) {
        qqGroup.getAdKeyList().add(keyword.toLowerCase());
        service.update(qqGroup);
        return "广告关键词添加成功！";
    }

    @Action("广告关键词 删除 {keyword}")
    public String delKeyword(String keyword, QqGroup qqGroup) {
        val newList = new ArrayList<String>();
        keyword = keyword.toLowerCase();
        for (String s : qqGroup.getAdKeyList()) {
            if (!s.equals(keyword)) newList.add(s);
        }
        qqGroup.setAdKeyList(newList);
        service.update(qqGroup);
        return "广告关键词添加成功！";
    }

}
