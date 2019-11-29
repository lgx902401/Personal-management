package com.nbclass.util;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;

/**
 * 统一的接口返回数据结构模版
 * 
 * @author Roger_Luo
 *
 */
public class ResponseTemplate<T> {
  // 默认成功调用时返回的返回码
  public static final Integer SUCCESS_RETURN_CODE = 0;

  @ApiModelProperty(name = "返回码", value = "每次调用接口时，可能获得正确或错误的返回码，开发者可以根据返回码信息调试接口，排查错误。", example = "0")
  @JSONField(ordinal = 1)
  private int code;

  @ApiModelProperty(name = "错误提示", value = "如果接口调用发生错误，该参数会返回相应的错误信息，该错误信息已经实现国际化。", example = "OK")
  @JSONField(ordinal = 2)
  private String message;

  @ApiModelProperty(name = "返回的数据json", value = "如果接口会返回数据，所有相关的数据都会放到该参数里面，数据结构以及参数类型请根据具体接口文档界定。")
  @JSONField(ordinal = 3)
  private T data;

  public ResponseTemplate(int code, String message) {
    this.code = code;
    this.message = message;
    this.data = null;
  }

  public ResponseTemplate(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  // 成功之后默认返回的对象
  public ResponseTemplate(T data) {
    this.code = SUCCESS_RETURN_CODE;
    this.message = "OK";
    this.data = data;
  }

  /**
   * Getter & Setter
   * 
   * @return
   */
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
