package wiki.IceCream.yuq.demo.event;

import com.IceCreamQAQ.Yu.annotation.Event;
import com.IceCreamQAQ.Yu.annotation.EventListener;
import com.IceCreamQAQ.Yu.cache.EhcacheHelp;
import com.icecreamqaq.yuq.YuQ;
import com.icecreamqaq.yuq.event.GroupMemberRequestEvent;
import com.icecreamqaq.yuq.event.GroupMessageEvent;
import com.icecreamqaq.yuq.event.GroupRecallEvent;
import com.icecreamqaq.yuq.message.Image;
import com.icecreamqaq.yuq.message.Message;
import com.icecreamqaq.yuq.message.MessageItem;
import com.icecreamqaq.yuq.message.MessageItemFactory;
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

}
