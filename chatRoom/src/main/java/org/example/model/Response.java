package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:点后端接口需要的统一字段
 * User: starry
 * Date: 2021 -05 -22
 * Time: 17:42
 */

@Getter
@Setter
@ToString
public class Response {
    //当前接口响应是否操作成功
    private boolean ok;
    //操作失败，前端要展示的错误信息
    private String reason;
    //返回的数据列表
    private List data;
}
