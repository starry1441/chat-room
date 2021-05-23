package org.example.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:自定义异常类
 * User: starry
 * Date: 2021 -05 -22
 * Time: 18:44
 */
public class AppException extends RuntimeException{

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

}
