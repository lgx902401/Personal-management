package com.nbclass.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 统一的接口入参带分页参数的模板
 * <p>
 * 如果有额外的入参需要，只要继承这个类即可
 *
 * @author Roger_Luo
 */
public class AbstractPageableRequest implements Serializable {

  private static final long serialVersionUID = -1667968684573762186L;

  @ApiModelProperty(value = "页码(从1开始)，默认值为1。", example = "1")
  private Integer page = 1;

  @ApiModelProperty(value = "一页展示的个数，默认值为20。", example = "20")
  private Integer size = 20;

  @ApiModelProperty(value = "排序的参数。")
  private List<Sort> sorts;

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public List<Sort> getSorts() {
    return sorts;
  }

  public void setSorts(List<Sort> sorts) {
    this.sorts = sorts;
  }

  /**
   * 排序用的参数
   *
   * @author Roger_Luo
   */
  @ApiModel(description = "排序的参数对象")
  public static class Sort implements Serializable {

    private static final long serialVersionUID = 1755111907299494140L;

    @ApiModelProperty(value = "排序的方式（倒序desc、顺序asc），默认值为asc。", allowableValues = "asc, desc", example = "asc")
    private String direction;

    @ApiModelProperty(value = "根据哪个字段排序。", required = true, example = "startTime")
    @NotBlank
    private String property;

    public String getDirection() {
      return direction;
    }

    public void setDirection(String direction) {
      this.direction = direction;
    }

    public String getProperty() {
      return property;
    }

    public void setProperty(String property) {
      this.property = property;
    }
  }
}
