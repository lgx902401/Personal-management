package com.nbclass.common.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理分页参数的工具
 *
 * @author Roger_Luo
 */
public class PageUtil {

  /**
   * 将接收到的分页参数解析成Pageable对象
   *
   * @param pageableRequest
   * @return Pageable对象，供jpa使用
   */
  public static Pageable parsePageable(AbstractPageableRequest pageableRequest) {

    Pageable pageable;

    List<AbstractPageableRequest.Sort> sorts = pageableRequest.getSorts();

    if (null == sorts || sorts.size() <= 0) {
      pageable = PageRequest.of(parsePageablePage(pageableRequest.getPage()), pageableRequest.getSize());
    } else {
      List<Sort.Order> orders = new ArrayList<>();
      sorts.forEach(s -> {
        if (!StringUtils.isEmpty(s.getProperty())) {
          Sort.Direction direction = Sort.Direction.fromOptionalString(s.getDirection()).orElse(null);
          orders.add(new Sort.Order(direction, s.getProperty()));
        }
      });
      pageable = PageRequest
        .of(parsePageablePage(pageableRequest.getPage()), pageableRequest.getSize(), Sort.by(orders));
    }
    return pageable;
  }

  /**
   * 将数据库返回的带分页信息的数据解析成待返回的分页信息，通常用于返回给前端使用
   *
   * @param pageData
   * @return
   */
  public static <T> PageableResponseData parsePageableResponseData(Page<?> pageData, T t) {
    PageableResponseData data = new PageableResponseData();
    // Page对象里面的page是以0开始为第一页的，所以返回出去的page参数需要加一
    data.setPage(pageData.getNumber() + 1);
    data.setSize(pageData.getSize());
    data.setTotalElements(pageData.getTotalElements());
    data.setTotalPages(pageData.getTotalPages());
    data.setContent(t);

    return data;
  }

  /**
   * 转换成pageable对象使用的page参数（0代表第一页）
   *
   * @param oneIndexdPage 1代表第一页的page参数
   * @return pageable对象使用的page参数（0代表第一页）
   */
  private static int parsePageablePage(Integer oneIndexdPage) {
    if (null == oneIndexdPage) {
      return 1;
    }
    return oneIndexdPage > 0 ? oneIndexdPage - 1 : oneIndexdPage;
  }
}
