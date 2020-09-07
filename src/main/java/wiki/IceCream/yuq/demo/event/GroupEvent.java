package wiki.IceCream.yuq.demo.event;

import com.IceCreamQAQ.Yu.annotation.Event;
import com.IceCreamQAQ.Yu.annotation.EventListener;
import com.IceCreamQAQ.Yu.cache.EhcacheHelp;
import com.icecreamqaq.yuq.YuQ;
import com.icecreamqaq.yuq.entity.Contact;
import com.icecreamqaq.yuq.event.GroupMemberJoinEvent;
import com.icecreamqaq.yuq.event.GroupMemberRequestEvent;
import com.icecreamqaq.yuq.event.GroupMessageEvent;
import com.icecreamqaq.yuq.event.GroupRecallEvent;
import com.icecreamqaq.yuq.message.Image;
import com.icecreamqaq.yuq.message.Message;
import com.icecreamqaq.yuq.message.MessageItem;
import com.icecreamqaq.yuq.message.MessageItemFactory;
import com.icecreamqaq.yuq.message.MessageSource;
import com.icecreamqaq.yuq.mirai.message.MiraiMessageSource;
import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
import lombok.val;
import wiki.IceCream.yuq.demo.serivce.GroupService;

import javax.inject.Inject;
import javax.inject.Named;

@EventListener
public class GroupEvent {

    @Inject
    private GroupService service;

    @Inject
    private YuQ yuq;
    @Inject
    private MessageItemFactory mif;

    @Inject
    @Named("MessageSaved")
    private EhcacheHelp<Message> saves;

    /**
     * 监控群消息
     *
     * @param event
     */
    @Event
    public void onGroupMessage(GroupMessageEvent event) {
        val groupId = event.getGroup().getId();
        val message = event.getMessage();

        val group = service.getGroupByGroupId(groupId);
        if (group == null) return;

        if (group.getAd()) {
            val ms = message.sourceMessage.toString().toLowerCase();
            if (!event.getSender().isAdmin())
                for (String s : group.getAdKeyList()) {
                    if (ms.contains(s)) {
                        message.recall();
                        event.getGroup().sendMessage(new Message().plus(mif.at(event.getSender().getId())).plus("请勿群内发布广告！"));
                        event.getSender().ban(300);

                        event.cancel = true;
                        return;
                    }
                }
        }

        for (MessageItem item : event.getMessage().getBody()) {
            if (item instanceof Image) {
                val url = ((Image) item).getUrl();
            }
        }

        if (group.getRecall()) {
            saves.set(message.getId().toString(), message);
        }

    }

    /**
     * 申请入群事件
     *
     * @param event
     */
    @Event
    public void onNewRequest(GroupMemberRequestEvent event) {
        val groupId = event.getGroup().getId();

        val group = service.getGroupByGroupId(groupId);
        if (group == null) return;

        if (group.getAutoAccept()) {
            event.cancel = true;
            event.setAccept(true);
        }
    }

    /**
     * 群消息撤回事件
     *
     * @param event
     */
    @Event
    public void onGroupRecall(GroupRecallEvent event) {
        if (event.getSender() != event.getOperator()) return;
        val groupId = event.getGroup().getId();

        val group = service.getGroupByGroupId(groupId);
        if (group == null) return;

        if (group.getRecall()) {
            val rm = saves.get(String.valueOf(event.getMessageId()));
            if (rm == null) return;
            event.getGroup().sendMessage(new Message().plus("群成员：").plus(mif.at(event.getOperator().getId())).plus("\n妄图撤回一条消息。\n消息内容为：\n").plus(rm));
        }
    }

    /**
     * 新成员入群事件
     *
     * @param event
     */
    @Event
    public void onGroupMemberJoin(GroupMemberJoinEvent event) {
        // 业务数据中心群
        if (event.getGroup().getId() == 577651387L) {
            event.getGroup().sendMessage(new Message().plus("欢迎").plus(mif.at(event.getMember().getId())).plus("加入本群~~\n" +
                    "请修改群内备注为:企业/库区-姓名 如XXXX库-张三  \n" +
                    "业务数据中心相关问题可在群内提问。\n" +
                    "群内定期踢除无备注者,以防止不良人员混入\n" +
                    "业务数据中心网址：http://36.7.135.171:10081/outer-web/frameJsp.do"));
        } else if (event.getGroup().getId() == 781109951L) { // 信息报送
            event.getGroup().sendMessage(new Message().plus("欢迎").plus(mif.at(event.getMember().getId())).plus("加入本群~~\n" +
                    "请修改群内备注为:市/县-姓名 如XXXX市-张三  \n" +
                    "信息报送相关问题可在群内提问。\n" +
                    "使用系统前请先下载群文件中的操作手册阅读。\n" +
                    "信息报送网址：http://172.10.8.2:8080/XTBG/frameJsp.do"));

        } else if (event.getGroup().getId() == 1146472411) { // 项目库
            event.getGroup().sendMessage(new Message().plus("欢迎").plus(mif.at(event.getMember().getId())).plus("加入本群~~\n" +
                    "请修改群内备注为:市/县-姓名 如XXXX市-张三  \n" +
                    "项目进度相关问题可在群内提问。"));

        } else { // 其他
            event.getGroup().sendMessage(new Message().plus("欢迎").plus(mif.at(event.getMember().getId())).plus("加入本群~~\n"));
        }

    }

}