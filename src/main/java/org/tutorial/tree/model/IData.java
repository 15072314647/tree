package org.tutorial.tree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.tutorial.tree.core.TreeNode;

public interface IData {

    Serializable getId();

    Serializable getParentId();

    /**
     * 必须初始化children
     * 
     * @return
     */
    List<IData> getChildren();

    /**
     * @param root 根节点，运行为空
     * @param datas 数据列表
     * @return 把输入的数据列表反转成一个宽松树形结构
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<IData> tree(Serializable root, List<? extends IData> datas) {
        List<IData> result = new ArrayList<>();
        TreeNode tree = new TreeNode(root, datas);
        TreeNode children = tree.getFirstChild();
        while (children != null) {
            result.add(children.getData());
            children = children.getNextSibling();
        }
        return result;
    }
}
