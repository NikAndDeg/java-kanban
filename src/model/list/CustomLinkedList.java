package model.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList<T> {
	/*
	indexNodeMap нужен для получения и удаления из списка за O(1)
	 */
	private final Map<Integer, Node<T>> indexNodeMap;
	/*
	dataIndexMap нужен для удаления повторяющихся данных за O(1)
	 */
	private final Map<T, Integer> dataIndexMap;
	private int size;
	private Node<T> head;
	private Node<T> tail;

	public CustomLinkedList() {
		indexNodeMap = new HashMap<>();
		dataIndexMap = new HashMap<>();
		size = 0;
		head = null;
		tail = null;
	}

	private void removeDuplicateData(T data) {
		Integer index = dataIndexMap.remove(data);
		if (index == null)
			return;
		delete(index);
	}

	public void linkLast(T data) {
		removeDuplicateData(data);
		dataIndexMap.put(data, size);
		if (head == null) {
			Node<T> headAndTail = new Node<>(data, null, null);
			head = headAndTail;
			tail = headAndTail;
			indexNodeMap.put(size, headAndTail);
			size++;
			return;
		}
		Node<T> oldTail = tail;
		tail = new Node<>(data, null, oldTail);
		oldTail.setNext(tail);
		indexNodeMap.put(size, tail);
		size++;
	}

	public T get(int index) {
		Node<T> node = indexNodeMap.get(index);
		if (node != null)
			return node.getData();
		return null;
	}

	public T delete(int index) {
		Node<T> node = indexNodeMap.remove(index);
		if (node == null)
			return null;
		dataIndexMap.remove(node.getData());
		if (node == head) {
			Node<T> newHead = head.getNext();
			if (newHead != null)
				newHead.setPrev(null);
			head = newHead;
			return node.getData();
		}
		if (node == tail) {
			Node<T> newTail = tail.getPrev();
			newTail.setNext(null);
			tail = newTail;
			return node.getData();
		}
		Node<T> leftNode = node.getPrev();
		Node<T> rightNode = node.getNext();
		leftNode.setNext(rightNode);
		rightNode.setPrev(leftNode);
		return node.getData();
	}

	public T delete(T data) {
		Integer index = dataIndexMap.remove(data);
		if (index == null)
			return null;
		return delete(index);
	}

	public List<T> getAsList() {
		List<T> list = new ArrayList<>();
		if (head == null)
			return list;
		Node<T> node = head;
		while (node.getNext() != null) {
			list.add(node.getData());
			node = node.getNext();
		}
		list.add(node.getData());
		return list;
	}

	static class Node<T> {
		private final T data;
		private Node<T> next;
		private Node<T> prev;

		Node(T data, Node<T> next, Node<T> prev) {
			this.data = data;
			this.next = next;
			this.prev = prev;
		}

		public T getData() {
			return data;
		}

		public Node<T> getNext() {
			return next;
		}

		public void setNext(Node<T> next) {
			this.next = next;
		}

		public Node<T> getPrev() {
			return prev;
		}

		public void setPrev(Node<T> prev) {
			this.prev = prev;
		}
	}
}
