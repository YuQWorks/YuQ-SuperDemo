package wiki.IceCream.yuq.demo.event;

import com.IceCreamQAQ.Yu.annotation.Event;
import com.IceCreamQAQ.Yu.annotation.EventListener;
import com.icecreamqaq.yuq.event.PrivateMessageEvent;
import com.icecreamqaq.yuq.message.Message;
import org.h2.util.StringUtils;

@EventListener
public class PrivateEvent {

    /**
     * 私聊事件
     * @param event
     */
    @Event
    public void onPrivateMessage(PrivateMessageEvent event){
        String content = event.getMessage().getBody().toArray()[0].toString();

        event.getSender().sendMessage(new Message().plus(event.getSender().getName()
                +" 您好，我只是个机器人哦，您所说的："
                +content+" 问题，我无法回答！")
                .plus("请您在群内咨询"));
    }

}
