package org.study.data_structure.linearList.itf;

import org.springframework.util.Assert;
import org.study.model.User;

/**
 * 线性表操作实类
 */

public class SequenceList implements LinearList {
	
	private Object[] dataList;
	
	private int size;
	
	public SequenceList(){
		this(10);
	}
	public SequenceList(int length){
		if(length<=0) {
			dataList = new Object[10];
		}
		dataList = new Object[length];
		this.size = 0;
	}
	
	
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Object get(int index) {
		checkIndex(index);
		return dataList[index];
	}

	@Override
	public boolean set(int index, Object obj) {
		try {
			checkIndex(index);
			dataList[index] = obj;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean add(int index, Object obj) {
		if(index > size || index < 0) {
			throw new IndexOutOfBoundsException("index is: " + index + "this size is: " + size);
		}
		int leng = dataList.length;
		//当数据已被填满了，需要进行扩容
		if(size == leng) {
			Object[] temp = dataList;
			dataList = new Object[leng << 2];
			//index位置以前的元素不变
			for(int i = 0; i<index; i++) {
				dataList[i] = temp[i];
			}
			//将temp的index位置以及它以后的元素放入dataList的index位置的后面
			for(int j = index; j < leng; j++) {
				dataList[j+1] = temp[j];
			}
			temp = null;
		} else {
			for(int i = size -1; i>=index; i--) {
				dataList[i+1] = dataList[i];
			}
		}
		//放入新元素
		dataList[index] = obj;
		size++;
		return true;
	}

	@Override
	public boolean add(Object obj) {
		add(size, obj);
		return true;
	}

	@Override
	public Object remove(int index) {
		checkIndex(index);
		Object temp = dataList[index];
		for(int i = index; i< size--; i++) {
			dataList[i] = dataList[i+1];
		}
		//这里先进行清空然后size--
		dataList[size--]=null;
		return temp;
	}

	@Override
	public void clear() {
		if(size > 0) {
			for(int i = 0; i<size; i++) {
			  dataList[i] = null;	
			}
			size = 0;
		}
	}
	
	private void checkIndex(int index) {
		if(index <0 || index >= size) {
			throw new IndexOutOfBoundsException("index: "+index +"  size is:" + size);
		}
	}
	
	
	public static SequenceList as(Object... objs) {
		Assert.notNull(objs);
		SequenceList list = new SequenceList();
		for(int i = 0, len = objs.length; i < len; i++) {
			list.add(objs[i]);
		}
		return list;
	}
	
	
	public static void main(String[] args) {
		User user1 = new User("1","zhangsan1","sewwe",null);
		User user2 = new User("2","zhangsan2","sewwe",null);
		User user3 = new User("3","zhangsan3","sewwe",null);
		User user4 = new User("4","zhangsan4","sewwe",null);
		User user5 = new User("5","zhangsan5","sewwe",null);
		User user6 = new User("7","zhangsan6","sewwe",null);
		User user7 = new User("7","zhangsan7","sewwe",null);
		User user8 = new User("8","zhangsan8","sewwe",null);
		
		SequenceList as = SequenceList.as(user1,user2,user3,user4,user5,user6,user7,user8);
		
		Object object = as.get(7);
		System.out.println("this size is "+as.size);
		System.out.println(object);
	}
	
}
