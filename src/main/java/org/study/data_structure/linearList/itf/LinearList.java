package org.study.data_structure.linearList.itf;
/**
 * 定义：线性数据结构中，数据元素之间是一对一的关系，除第一个和最后一个数据元素之外，其他的数据元素都是首尾相连的
 * 实际应用中线性表是以字符串，数组，栈，队列等特殊线性表的形式来使用的
 * 线性表有如下特点：
 * 1.有且仅有一个开始点
 * 2.有且仅有一个终端点
 * 3.内部节点有且仅有一个直接前驱节点和直接后继节点
 * 4.还具有均匀性和有序性
 * 线性表功能的接口定义
 */
public interface LinearList {
	/**
	 * 判断线性表是否为空
	 */
	boolean isEmpty();
	/**
	 * 获取线性数据的长度
	 */
	int size();
	/**
	 * 获取指定位置的元素
	 */
	Object get(int index);
	/**
	 * 设置指定位置的元素
	 * 此处应该要抛出异常，当设置的元素不在逻辑位置时，比如在小于0以后的位置，或大于实际长度的位置等等
	 */
	boolean set(int index, Object obj);
	
	/**
	 * 向指定位置添加相应的元素
	 * @param index
	 * @param obj
	 * @return  添加成功返回true，失败返回false
	 */
	boolean add(int index, Object obj);
	
	/**
	 * 向列表的最后位置添加元素
	 */
	boolean add(Object obj);
	
	/**
	 * 移除相应位置的元素
	 */
	Object remove(int index);
	
	/**
	 * 清楚所有的元素
	 */
	void clear();
	
	
}
