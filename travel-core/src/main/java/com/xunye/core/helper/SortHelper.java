package com.xunye.core.helper;

import java.util.Comparator;
import java.util.List;

import com.xunye.core.support.IHierarchical;
import com.xunye.core.support.ISortable;
import com.xunye.core.tools.CheckTools;

/**
 * 排序工具类
 * 目前主要用于与{@link IHierarchical}结合使用
 */
public class SortHelper {

    /**
     * 对外提供的默认排序的方法
     *
     * @param list 需要排序的集合
     * @param <T>  {@link ISortable}接口的实现类
     */
    public static <T extends ISortable> void sort(List<T> list) {
        list.sort(new DefaultSort<>());
    }

    /**
     * 默认的排序方法
     * 作用于实现了{@link ISortable}的集合
     *
     * @param <T>
     */
    static class DefaultSort<T extends ISortable> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            if (CheckTools.isNotNullOrEmpty(o1.fetchSortNo())
                    && CheckTools.isNotNullOrEmpty(o2.fetchSortNo())) {
                return o1.fetchSortNo() - o2.fetchSortNo();
            }
            return 0;
        }
    }


    /*

    工具方法测试

        @Data
    @AllArgsConstructor
    static class Test implements ISortable {

        private String name;
        private Integer value;

        @Override
        public Integer fetchSortNo() {
            return value;
        }
    }

    public static void main(String[] args) {
        List<Test> list = new ArrayList<>();
        list.add(new Test("测试1", 3));
        list.add(new Test("测试2", 1));
        list.add(new Test("测试3", 9));
        list.add(new Test("测试4", 4));
        list.add(new Test("测试4", -8));
        // list.sort(new DefaultSort<>());
        sort(list);
        System.out.println(list);
    }
     */


}
