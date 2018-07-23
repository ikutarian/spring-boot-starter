package com.okada.stater.pojo;

/**
 * 自定义响应数据结构
 *
 * 这个类是提供给门户，ios，安卓，微信商城用的
 *
 * status的含义
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 */
public class JSONResult {

    // 响应状态
    private Integer status;
    // 响应消息
    private String msg;
    // 响应中的数据
    private Object data;

    private JSONResult() {
    }

    public static JSONResult ok(Object data) {
        JSONResult result = new JSONResult();
        result.setStatus(200);
        result.setData(data);
        result.setMsg("ok");
        return result;
    }

    public static JSONResult error(String msg) {
        JSONResult result = new JSONResult();
        result.setStatus(500);
        result.setMsg(msg);
        return result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
