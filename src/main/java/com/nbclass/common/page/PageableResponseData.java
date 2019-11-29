package com.nbclass.common.page;

import io.swagger.annotations.ApiModelProperty;

/**
 * 带分页参数的返回数据模板
 *
 * @author Roger_Luo
 */
public class PageableResponseData<T> {

  /**
   * 分页数据的内容
   */
  @ApiModelProperty(name = "分页数据的内容", value = "分页数据的内容。")
  private T content;

  /**
   * 当前页数
   */
  @ApiModelProperty(name = "当前页数", value = "当前查看的页数。")
  private int page;

  /**
   * 当前页的数据个数
   */
  @ApiModelProperty(name = "当前页的数据个数", value = "当前页的数据个数。")
  private int size;

  /**
   * 总页数
   */
  @ApiModelProperty(name = "总页数", value = "总页数。")
  private int totalPages;

  /**
   * 数据总个数
   */
  @ApiModelProperty(name = "数据总个数", value = "数据总个数。")
  private long totalElements;

  // Getter & Setter

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }
}
