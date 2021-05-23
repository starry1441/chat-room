package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * Description:消息类
 * User: starry
 * Date: 2021 -05 -22
 * Time: 16:13
 */

@Getter
@Setter
@ToString
public class Message {

    private Integer messageId;
    private Integer userId;
    private Integer channelId;
    private String content;
    private java.util.Date sendTime;

    //接收客户端发送的消息，转发到所有客户端的消息，需要昵称
    private String nickName;

}
