package org.study.collections;

import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by Administrator on 2017/4/4 0004.
 */
public class CollectionTest {

    @Test
    public void test1() {

        Collection<String> c1 = new HashSet<String>();
        c1.add("a");
        c1.add("c");
        c1.add("d");
        c1.add("n");
        Collection<String> c2 = new HashSet<String>();
        c2.add("b");
        c2.add("c");
        c2.add("d");
        c2.add("f");

        //c1集合对c2集合取交集，结果是c d
        c1.retainAll(c2);
        System.out.print(c1);
    }

    @Test
    public void testIterator() {
        Collection<String> col = new ArrayList<String>();
        col.add("java");
        col.add("c++");
        col.add(".net");

        System.out.println("迭代前："+col);

        //注意，使用迭代器遍历集合时。集合本身是不能被更改的。否则爆发异常
        Iterator<String> iterator = col.iterator();
        while(iterator.hasNext()) {
            String next = iterator.next();
            if(".net".equals(next)) {
                iterator.remove();
            }
        }

        System.out.println("迭代后："+col);
    }

    //测试Collection的removeIf方法
    @Test
    public void test2() {

        Collection<String> col = new LinkedList<>();
        col.add("com");
        col.add("org");
        col.add("edu");
        col.add("oschi");

        //移除包含字符O的
        col.removeIf(a->a.contains("o"));

        System.out.println("过滤之后的集合："+col);
    }


    public int calByCondition(Collection col, Predicate p) {
        int count = 0;
       /* Iterator it = col.iterator();
        while(it.hasNext()) {
            Object next = it.next();
            if(p.test(next)) {
                count++;
            }
        }*/
        //使用另一种方式实现过滤条件的统计
        count = (int)col.parallelStream().filter(p).count();

        return count;
    }

    //测试集合的条件过滤
    @Test
    public void test() {
        Collection<String> col = new ArrayList<String>();
        col.add("java之疯狂讲义");
        col.add("java之设计模式");
        col.add("java之代码大作战");
        col.add("java之多线程的应用");
        col.add("java之spring的框架应用");
        //计算集合中长度大于9的字符串
        int size1 = calByCondition(col,obj->((String)obj).length() > 9);
        System.out.println(size1);
    }

    @Test
    public void testHashSet() {
        Collection<R> col = new HashSet<R>();
        R r5 = new R(5);
        col.add(r5);
        R r_3 = new R(-3);
        col.add(r_3);
        R r9 = new R(-3);
        col.add(r9);
        R r_2 = new R(-3);
        col.add(r_2);

        System.out.println("改变前："+col);

        Iterator<R> it = col.iterator();
        R r = it.next();
        r.count = -3;
        System.out.println("改变后："+col);//集合有重复元素

        col.remove(new R(-3));
        System.out.println("移除后："+col);

        System.out.println("是否包含count为-3的R对象" + col.contains(new R(-3)));
        //当程序把可变对象添加到HashSet中后，尽量不要去修改该元素集合中参与计算hashCode(),equals()的实例变量
        //否则将会导致set集合无法准备访问该对象
        System.out.println("是否包含count为-2的R对象" + col.contains(r_2));
    }

}


class R {
    int count;

    public R(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "[count: "+count+"   hashCode:"+hashCode()+"]";
    }

    @Override
    public int hashCode() {
        return this.count;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(null != obj && obj.getClass() == R.class) {
            R r = (R)obj;
            return this.count == r.count;
        }
        return false;
    }
}
