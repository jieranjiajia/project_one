package org.study.data_structure.linearList.itf;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

/**
 * 约瑟夫问题的研究
 */
public class YueSeFu {
	
	class LinkedList {
		public int index;
		public LinkedList next;
		
		public LinkedList(int index, LinkedList next) {
			this.index = index;
			this.next = next;
		}
		
		public String toString() {
			return JSON.toJSONString(this);
		}
	}
	
	/**
	 * 这是递归操作，初始化size长度的一个链表，并没有形成环
	 * @param item
	 * @param size
	 * @return
	 */
	public LinkedList initialLinkedList(LinkedList item, int size) {
		if(item.index == size) {
			return item;
		}
		item.next = new LinkedList(item.index + 1, null);
		return initialLinkedList(item.next,size);
	}
	
	/**
	 * 获取从start元素开始的k个位置的LinkedList
	 * 比如：1开始数3个  返回3
	 * 4 开始 4个  则返回8
	 * @param start
	 * @param k
	 * @return
	 */
	public LinkedList getLinkedListByK(LinkedList start, int k) {
		LinkedList temp = start;
		int i = 1;
		while(i < k) {
			temp = temp.next;
			i++;
		}
		return temp;
	}
	
	/**
	 * 从start开始数，数到n的元素被移除
	 * 
	 */
	public void cal(LinkedList start, int n) {
		
		//n元素的前一个元素
		LinkedList prev = getLinkedListByK(start, n-1);
		
		LinkedList indexN = prev.next;
		System.out.println("当前被移除的元素是:"+indexN.toString());
		
		if(prev == indexN) {
			System.out.println("最后剩下的就是:" + indexN.toString());
			return;
		}
		prev.next = indexN.next;
		
		//继续往下数
		cal(prev.next,n);
		
	}
	
	@Test
	public void test() {
		LinkedList first = new LinkedList(1,null);
		LinkedList end = initialLinkedList(first, 10);
		//此处形成一个闭合的环
		end.next = first;
		cal(first,3);
	}
	
	@Test
	public void test1() {
		
	}
	
}
