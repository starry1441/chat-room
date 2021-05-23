package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:用户实体类
 * User: starry
 * Date: 2021 -05 -22
 * Time: 15:56
 */

@Getter
@Setter
@ToString

//implements Serializable:
    //1.数据库使用
    //2.前后端Ajax使用
    //3.session保存时基于对象和二进制数据转换（这里要实现串行化接口）
public class User extends Response implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer userId;
    private String name;
    private String password;
    private String nickName;
    private String iconPath;
    private String signature;
    private java.util.Date lastLogout;

}
