package org.study.sort;

public class Sort {

	private Sort() {
	}

	/**
	 * 冒泡排序： 原理：两两比较一轮确定最大数，比较次数是(n*(n-1))/2
	 */
	public static void bubbleSort(int[] arr) {
		int i = 0, j, temp, len = arr.length;
		boolean flag = true;
		int count1 = 0, count2 = 0;
		for (; i < len && flag; i++) {
			// 这种方式是从后往前比较，后面的比前面的小
			flag = false;
			for (j = len - 1; j > i; j--) {
				count1++;
				if (arr[j] < arr[j - 1]) {
					temp = arr[j];
					arr[j] = arr[j - 1];
					arr[j - 1] = temp;
					count2++;
					flag = true;
				}
			}
		}

		System.out.println("比较了" + count1 + "次数");
		System.out.println("交换了" + count2 + "次数");
	}

	/**
	 * 另一种写法的冒泡排序。和上面的写法比较起来，上面的写法做了一定的优化。 比较次数为n(n-1)/2
	 */
	public static void bubbleSort1(int[] arr) {
		boolean flag = true;
		for (int i = 0, len = arr.length; i < len && flag; i++) {
			for (int j = 0; j < len - i - 1; j++) {
				// 前面的数比后面的数大
				if (arr[j] > arr[j + 1]) {
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}

	/**
	 * 插入排序 原理：将一个数据插入到已经排序好的子序列中
	 */
	public static void insertSort(int[] arr) {
		int i, j, len = arr.length, temp;
		for (i = 1; i < len; i++) { // 从位置为1的开始，0位置的元素独自作为一个有序的元素
			if (arr[i] < arr[i - 1]) {
				// 0 ~ i-1 位是有序的，若i小于 i-1位则继续寻位置
				temp = arr[i]; // 保存第i位的值
				for (j = i - 1; j >= 0 && temp < arr[j]; j--) { // 如果左边的数据小于右边数据，需要将右边大的数据往左移
					arr[j + 1] = arr[j];
				}
				// 最后将i位置的数据插入相应的位置
				arr[j + 1] = temp;
			}
		}
	}

	/**
	 * 折半插入排序原理 折半排序原理：在将一个新元素插入到一个已经排好的序列中时，采用折半的方式找到元素的位置， 是对直接插入排序的改进
	 * 元素比较次数：元素比较的次数好于直接插入排序，平均比较次数为nlogn,
	 * 元素交换次数：折半排序元素移动的次数与直接插入元素移动的次数相同，均与元素的初始序列有关
	 *
	 * 最差情况下，元素需要移动的次数为nlogn,即每次插入在有序序列的开头，所有元素都需要后移一位
	 *
	 * 空间占用情况：只占用一个临时变量，故空间占用率为O(1)
	 */
	public static void binaryInsertSort(int[] arr) {
		int i, j, temp, len = arr.length;
		int low, hight, middle;
		// 依然是认为0位置的数据是一个有序的序列
		for (i = 1; i < len; i++) {
			low = 0;
			hight = i - 1;
			temp = arr[i]; // 记录i位置上的数据
			while (hight >= low) {
				middle = (low + hight) / 2;
				// 如果i位置的数据大于中间位置的数据，则low位置需要往前移
				if (temp > arr[middle]) {
					low = middle + 1;
				} else {
					hight = middle - 1;
				}
			}
			// 找到要插入的元素位置为low
			for (j = i - 1; j >= low; j--) {
				arr[j + 1] = arr[j];
			}
			arr[low] = temp;
		}
	}

	/**
	 * 希尔排序 希尔排序是不稳定的排序，是对插入排序的改进 在最好的情况和最差的情况下性能都是比较好的 排序原理：
	 */
	public static void shellSort(int[] arr) {
		int len = arr.length;
		int i, j, gap = len; // gap为初识量
		do {
			gap = gap / 3; // 设置每次的间隔变量
			for (i = gap; i < len; i++) {
				int temp = arr[i];

				// 这个地方相当于是执行插入排序
				for (j = i - gap; j >= 0 && temp < arr[j]; j -= gap) {
					arr[j + gap] = arr[j];
				}
				arr[j + gap] = temp;
			}
		} while (gap > 1);
	}

	/**
	 * 将两个已经排序好的数列arr[low...middle]和arr[middle+1...high]进行合并
	 */
	public static void mergeArray(int[] arr, int low, int middle, int high) {
		int[] temp = new int[high - low + 1];// 创建一个存储排序后的临时数组
		int i = low, m = middle; // 左指针
		int j = middle + 1, n = high; // 右指针
		int k = 0;

		while (i <= m && j <= n) {
			if (arr[i] < arr[j]) {
				temp[k++] = arr[i++]; // 将小的数据放入中间的数组后，再执行一个++操作
			} else {
				temp[k++] = arr[j++];
			}
		}
		// 最后arr1或arr2中多余的数据添加到temp数组中去
		while (i <= m) {
			temp[k++] = arr[i++];
		}
		while (j <= n) {
			temp[k++] = arr[j++];
		}
		// 将临时数组中排好序的数组写入到实际数组中去
		for (int index = 0; index < k; index++) {
			arr[low + index] = temp[index];
		}
	}

	/**
	 *  归并排序
	 *  归并排序效率分析：归并排序运行的时间复杂度为O(nlogn) 空间代价为O(n)
	 *  最好情况和最差情况下时间复杂度均为O(nlogn),归并排序是一种稳定的外部排序算法
	 */
	public static void mergeSort(int[] arr, int low, int high) {

		int middle = (low + high) / 2;
		//这里要进行递归调用
		if (low < high) {
			// 排序左边的
			mergeSort(arr, low, middle);
			// 排序右边的
			mergeSort(arr, middle + 1, high);
			// 合并数组
			mergeArray(arr, low, middle, high);
		}
	}


	public static void print(int[] arr) {
		for (int i = 0, len = arr.length; i < len; i++) {
			System.out.print(arr[i] + "   ");
		}
	}
	
	/**
	 * 可以理解为挖坑天数
	 * @param arr  待排序的数组
	 * 快速排序原理：在序列中找到基准元素，大于基准元素的放在右边，小于基准元素的放在左边，然后对基准元素两边再次进行排序
	 * 快速排序是内部排序中最好的一个，平均情况下其效率为O(nlogn)，由于其采用递归方式，递归次数取决于递归树的高度log2n,因此其占用的空间为O(log2n)
	 * 对于快速排序的优化：
	 * 第一种方式：如果递归树两边的元素数量均等，那么递归时分配的栈占用的内存空间最小，因此可以适当优化选取的基准元素，取前端、中间点和尾端的中间值
	 * 第二种方式：如果基准元素两边的元素数量小于等于7时采用直接插入排序，或者如果基准元素两边的元素小于某个值时，返回，最终对序列进行一次直接插入排序
	 * 元素交换次数：
	 * 最好情况：
	 * 最坏情况：如果元素逆序，那么递归的次数为n次，占用的存储空间为O(n)，所需比较的次数为n(n-1)/2,效率低于直接插入排序
	 */
	public static void quickSort(int[] arr, int low, int high) {
		
		if(low < high) {
			//arr[low]为标杆
			int i = low, j = high, temp = arr[low];
			while (i < j) {
				
				//从右往左找第一个小于标杆的数
				while(i < j && arr[j] >= temp) {
					j--;
				}			
				if(i < j) {
					arr[i++] = arr[j]; //arr[i] = arr[j]; i++
				}
				//从左往右找第一个大于标杆的数
				while(i < j && arr[i] <= temp) {
					i++;
				}
				if(i < j) {
					arr[j--] = arr[i]; //arr[j] = arr[i]; j--
				}
				//将标杆的数据移入到正确的位置
				arr[i] = temp;
				//递归调用继续排序，对i左边的继续排序
				quickSort(arr, low, i-1);
				//递归调用继续排序，对i右边的继续排序
				quickSort(arr, i+1, high);
			}
		}
		
	}
	
	/**
	 * 选择排序
	 * 原理：从待排序中选择最小的元素放入到已排好序的序列中
	 * 元素比较次数：需要比较的次数为n(n-1)/2，总的元素移动次数为3(n-1) ,空间占用情况为O(1)
	 * 元素交换次数：
	 * 最好情况：最好的情况，元素移动的次数为0
	 * 最坏情况：和最好情况一致，元素需要移动3(n-1)次
	 * @param arr
	 */
	public static void selectSort(int[] arr) {
		
		for(int i = 0, len = arr.length; i < len; i++) {
			int index = i;
			for(int j = i+1; j < len; j++) {
				if(arr[index] > arr[j]) {
					//找到最小的元素
					index = j;
				}
			}
			//找到最小的不是同一个元素则交换位置
			if(i != index) {
				int temp = arr[index];
				arr[index] = arr[i];
				arr[i] = temp;
			}
		}
		
	}
	
	//箱排序
	public static void basket(int data[])// data为待排序数组
	{
		int n = data.length;
		int bask[][] = new int[10][n];
		int index[] = new int[10];
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			max = max > (Integer.toString(data[i]).length()) ? max : (Integer.toString(data[i]).length());
		}
		String str;
		for (int i = max - 1; i >= 0; i--) {
			for (int j = 0; j < n; j++) {
				str = "";
				if (Integer.toString(data[j]).length() < max) {
					for (int k = 0; k < max - Integer.toString(data[j]).length(); k++)
						str += "0";
				}
				str += Integer.toString(data[j]);
				bask[str.charAt(i) - '0'][index[str.charAt(i) - '0']++] = data[j];
			}
			int pos = 0;
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < index[j]; k++) {
					data[pos++] = bask[j][k];
				}
			}
			for (int x = 0; x < 10; x++)
				index[x] = 0;
		}
	}
	
	
	
	public static void main(String[] args) {
		int[] arr = { 2, 78, 264, 2, 46, 65, 56, 6, 4, 64, 45, 6487, 12 };
		Sort.basket(arr);
		print(arr);
	}
}
