/**
 * 
 */
package com.manzhizhen.datastructure.queue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Deque接口扩展了 Queue接口，是一个线性 Collection
 * 
 * 支持在两端插入和移除元素。名称 deque 是“double ended queue（双端队列）”的缩写，通常读为“deck”。
 * 大多数 Deque 实现对于它们能够包含的元素数没有固定限制，但此接口既支持有容量限制的双端队列，
 * 也支持没有固定大小限制的双端队列。 
 * 此接口定义在双端队列两端访问元素的方法。提供插入、移除和检查元素的方法。
 * 每种方法都存在两种形式：一种形式在操作失败时抛出异常，另一种形式返回一个特殊值（null 或 false，具体取决于操作）。
 * 插入操作的后一种形式是专为使用有容量限制的 Deque 实现设计的；
 * 在大多数实现中，插入操作不能失败。 
 * 
 * 在将双端队列用作队列时，将得到 FIFO（先进先出）行为：
 * Queue方法  等效 Deque 方法 
 * add(e) 		addLast(e) 
 * offer(e) 	offerLast(e) 
 * remove() 	removeFirst() 
 * poll() 		pollFirst() 
 * element() 	getFirst() 
 * peek() 		peekFirst() 
 *
 * 双端队列也可用作 LIFO（后进先出）堆栈。应优先使用此接口而不是遗留 Stack 类。 
 * 堆栈方法   等效 	Deque 方法 
 * push(e) 		addFirst(e) 
 * pop() 		removeFirst() 
 * peek() 		peekFirst() 
 *
 * 注意，在将双端队列用作队列或堆栈时，peek 方法同样正常工作；无论哪种情况下，都从双端队列的开头抽取元素。 
 *
 * @author Manzhizhen
 * @since jdk 1.6
 */
public class DequeTest {
	public static void main(String[] args) {
		arrayDequeTest();
	}
	
	/**
	 * ArrayDeque 使用
	 * 
	 * 说明：它是 Deque 接口的大小可变数组的实现
	 * @since jdk 1.6
	 */
	public static void arrayDequeTest() {
		// 构造一个初始容量能够容纳 16 个元素的空数组双端队列。
		Deque<String> arrayDeque = new ArrayDeque<String>();
		
		// 初始化数据，便于测试
		System.out.println("初始化前Size:" + arrayDeque.size());
		for(int i = 0; i < 10; i++) {
			arrayDeque.add(i + "");
		}
		System.out.println("初始化后Size:" + arrayDeque.size());
		System.out.println("初始化后队列:" + arrayDeque);
		
		/**
		 * 作为队列来操作（从尾部放数据，从头部取数据，先进先出FIFO）
		 */
		// 获取但不移除队列头元素
		System.out.println("peek():" + arrayDeque.peek());			// 来自Queue 如果队列为空，则返回null
		System.out.println("element():" + arrayDeque.element());	// 来自Queue 与peek不同，如果队列为空，则抛出NoSuchElementException异常
		System.out.println("peekFirst():" + arrayDeque.peekFirst());// 来自Deque
		System.out.println("getFirst():" + arrayDeque.getFirst());	// 来自Deque
		// 获取并移除队列头元素
		System.out.println("poll():" + arrayDeque.poll());			// 来自Queue 如果队列为空，则返回null
		System.out.println("pollFirst()" + arrayDeque.pollFirst());	// 来自Deque
		System.out.println("获取并移除后的队列：" + arrayDeque);
		// 往队列中添加元素
		System.out.println("add():" + arrayDeque.add("10"));				// 来自Queue
		System.out.println("offer():" + arrayDeque.offer("11"));			// 来自Queue	
		arrayDeque.addLast("12");											// 来自Deque
		System.out.println("offerLast():" + arrayDeque.offerLast("13"));	// 来自Deque
		System.out.println("添加数据后的队列：" + arrayDeque);
		// 获取并从队列头中移除元素
		System.out.println("remove():" + arrayDeque.remove());				// 来自Queue 与poll唯一不同的是，如果队列为空，则抛出NoSuchElementException异常
		System.out.println("removeFirst():" + arrayDeque.removeFirst());	// 来自Deque
		
		// 初始化数据，便于测试
		arrayDeque.clear();
		System.out.println("\n\n初始化前Size:" + arrayDeque.size());
		for(int i = 0; i < 10; i++) {
			arrayDeque.push(i + "");
		}
		System.out.println("初始化后Size:" + arrayDeque.size());
		System.out.println("初始化后栈:" + arrayDeque);
		
		/**
		 * 作为栈来操作(后进先出LIFO,取数据和压数据都从栈顶进行)
		 */
		// 把数据压入栈的顶部
		arrayDeque.push("10");
		arrayDeque.addFirst("11");	// 注意，队列的这里是addLast
		System.out.println("添加数据后的栈：" + arrayDeque);
		
		// 从栈顶取数据但不移除
		System.out.println("peek():" + arrayDeque.peek());
		System.out.println("peekFirst():" + arrayDeque.peekFirst());
		System.out.println("peek后的栈：" + arrayDeque);
		
		// 从栈顶取数据并移除
		System.out.println("pop():" + arrayDeque.peek());
		System.out.println("removeFirst():" + arrayDeque.removeFirst());	// 这里和队列不同，Deque在队列中对应poll()的方法是removeLast()
		System.out.println("pop后的栈：" + arrayDeque);
		
		System.out.println("\n\n注意，在将双端队列用作队列或堆栈时，peek 方法同样正常工作；无论哪种情况下，都从双端队列的开头抽取元素。 ");
		
		arrayDeque.clear();
		arrayDeque.add("queue1");
		arrayDeque.addLast("queue2");
		arrayDeque.push("stack1");
		arrayDeque.addFirst("stack2");
		System.out.println(arrayDeque); // 注意，输出时是通过ArrayDeque内部的head变量作为基础开始遍历输出数组元素的。
		
		
	}
	
}
