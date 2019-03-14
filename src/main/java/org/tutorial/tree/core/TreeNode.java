package org.tutorial.tree.core;

import java.io.Serializable;
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
        Map<Serializable, TreeNode<T>> cacheNodes = new ConcurrentHashMap<>();// 已经处理过的节点
        Map<Serializable, TreeNode<T>> withOutNodes = new ConcurrentHashMap<>();// 失联节点
        for (T data : datas) {
            TreeNode<T> node = new TreeNode<T>(data);
            cacheNodes.put(data.getId(), node);
            TreeNode<T> parent = root == data.getParentId() ? this : cacheNodes.get(data.getParentId());
            if (withOutNodes.containsKey(data.getId())) {
                // 当前节点是父节点，且已经存在失联的子节点
                node.setFirstChild(withOutNodes.get(data.getId()));
                withOutNodes.remove(data.getId());
            }
            if (parent != null) {
                // 当前节点是子节点，而且其父节点已处理过
                node.setNextSibling(parent.getFirstChild());
                parent.setFirstChild(node);
            } else {
                if (withOutNodes.containsKey(data.getParentId())) {
                    // 当前节点是子节点，且已经存在兄弟节点
                    node.setNextSibling(withOutNodes.get(data.getParentId()));
                }
                withOutNodes.put(data.getParentId(), node);
            }
        }
        if (withOutNodes.size() > 0) {
            // FIXME 后期改成记录日志
            throw new RuntimeException("当前树存在游离节点");
        }
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
