package me.shijunjie.worm.bean;

public class CommonResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static<T> CommonResponse success(T data) {
        CommonResponse<T> commonResponse = new CommonResponse<>();
        commonResponse.setCode(0);
        commonResponse.setMessage("成功");
        commonResponse.setData(data);
        return commonResponse;
    }
    public static<T> CommonResponse success() {
        CommonResponse<T> commonResponse = new CommonResponse<>();
        commonResponse.setCode(0);
        commonResponse.setMessage("成功");
        return commonResponse;
    }

    public static<T> CommonResponse fail(String message) {
        CommonResponse<T> commonResponse = new CommonResponse<>();
        commonResponse.setCode(1);
        commonResponse.setMessage("失败");
        return commonResponse;
    }

}
