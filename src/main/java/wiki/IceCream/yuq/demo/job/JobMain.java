package wiki.IceCream.yuq.demo.job;

import com.IceCreamQAQ.Yu.annotation.Cron;
import com.IceCreamQAQ.Yu.annotation.JobCenter;
import com.IceCreamQAQ.Yu.util.DateUtil;
import com.icecreamqaq.yuq.YuQ;
import com.icecreamqaq.yuq.entity.Member;
import com.icecreamqaq.yuq.message.Message;
import com.icecreamqaq.yuq.message.MessageItemFactory;
import lombok.val;
import wiki.IceCream.yuq.demo.serivce.GroupService;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@JobCenter
public class JobMain {

    @Inject
    private YuQ yuq;

    @Inject
    private MessageItemFactory mif;
    /***
     * 时钟任务。
     * value 参数可接受 1d 1h 1m 1s 1S（天，小时，分钟，秒，毫秒）的参数。
     * 同时也支持类似于 5m5s 的组合参数。
     *
     * 当然如果你愿意，1d2S111m56h7998s333m 这样的参数也能顺利解析并正确使用。
     * 但是代码是写给自己看的，为什么要跟自己过不去呢？
     *
     * 时钟任务方法不接受任何参数，也不接受任何返回值。
     */
    // @Cron("10s")
    // public void ten() {
    //     System.out.println("到十秒钟啦！");
    // }

    @Inject
    private DateUtil dateUtil;

    /***
     * 定时任务。
     * value 必须按规范写成组合
     * 以 At:: 开头。
     * 如果匹配每天的第几个小时的第几分钟，则接下来写 d。
     * 如果匹配某小时的第几分钟则写 h。
     * 接下来写分隔符 ::
     * 如果上一步写的是 d，则写 小时:分钟（这里只有一个 : ）（二十四小时制）。例如: 12:00
     * 如果上一步写的是 h，则直接写第几分钟。例如: 00
     * 所有的冒号均是英文半角。
     *
     * 本实例在每个小时刚开始触发。
     *
     * 定时任务方法不接受任何参数，也不接受任何返回值。
     */
   /* @Cron("At::h::00")
    public void at00() {
        System.out.println("现在是每个小时开始的第一分钟！" + dateUtil.formatDate());
    }*/


    /**
     * 定时1
     */
    @Cron("0 20 13,15,17 * * ? ")
    public String remind1() {
        return remind();
    }

    /**
     * 定时2
     */
    @Cron("0 30 13,15,17 * * ? ")
    public String remind2() {
        return remind();
    }


    /**
     * 定时群消息 && 私信 推送
     *
     * @return
     */
    public String remind() {
        // 不需要发送私信的QQ数组
        Long notPrivateStringQQ[] = {2586515115L};
        // 群
        String group = "1141137860";
        val g = yuq.getGroups().get(Long.parseLong(group));
        if (g == null) return "Error: Group Not Found!";
        // 发送三遍
        for (int i = 0; i < 3; i++) {
            Message message = new Message().plus(mif.at(-1L).plus("现在是北京时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n" +
                    "请大家及时打卡上报体温哦！！！"));
            g.sendMessage(message);
        }

        // 获取群组成员 && 私信
        Map<Long, Member> members = yuq.getGroups().get(Long.parseLong(group)).getMembers();
        members.forEach((Long id, Member member) -> {
            val gr = yuq.getGroups().get(member.getGroup().getId());
            if (gr == null) System.out.println("Error: Group Not Found!");
            val m = gr.getMembers().get(id);
            if (m == null) System.out.println("Error: Member Not Found!");
            // 发送三遍
            for (int i = 0; i < notPrivateStringQQ.length; i++) {
                if (!notPrivateStringQQ[i].equals(id)) {
                    for (int j = 0; j < 3; j++) {
                        String msg = "现在是北京时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "\n" +
                                "请您及时打卡上报体温哦！！！";
                        m.sendMessage(new Message().plus(msg));
                    }
                }
            }
        });
        return "OK!";
    }


}
