package org.tutorial.tree.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tutorial.tree.model.IData;

/**
 * 类TreeNode.java的实现描述：使用 第一个儿子/下一兄弟表示法 来表示树
 * 
 * @author cl150 2019年3月13日 下午3:54:31
 */
public class TreeNode<T extends IData> {

    T           data;

    TreeNode<T> firstChild;

    TreeNode<T> nextSibling;

    public TreeNode(){
    }

    public TreeNode(T data){
        this.data = data;
    }

    public TreeNode(Serializable root, List<T> datas){
        if (null == datas || datas.size() < 1) {
            return;
        }
        int size = 0;
        Map<Serializable, TreeNode<T>> cacheNodes = new ConcurrentHashMap<>();
        // 树有几层就遍历几遍
        do {
            size = datas.size();// 判断列表是否有改变
            List<T> filters = new ArrayList<>();
            for (T data : datas) {
                if (root != data.getParentId() && !cacheNodes.containsKey(data.getParentId())) {
                    filters.add(data);
                } else {
                    TreeNode<T> node = new TreeNode<T>(data);
                    cacheNodes.put(data.getId(), node);
                    TreeNode<T> parent = root == data.getParentId() ? this : cacheNodes.get(data.getParentId());
                    node.setNextSibling(parent.getFirstChild());
                    parent.setFirstChild(node);
                }
            }
            datas = filters;
        } while (size != datas.size() && datas.size() >= 0);// 列表为空，或者连续两次列表无改变则认为任务结束
    }

    /**
     * 递归调用，转换成树形结构
     * 
     * @return
     */
    public T getData() {
        TreeNode<T> child = firstChild;
        while (child != null) {
            data.getChildren().add(child.getData());
            child = child.nextSibling;
        }
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(TreeNode<T> firstChild) {
        this.firstChild = firstChild;
    }

    public TreeNode<T> getNextSibling() {
        return nextSibling;
    }

    public void setNextSibling(TreeNode<T> nextSibling) {
        this.nextSibling = nextSibling;
    }

}
