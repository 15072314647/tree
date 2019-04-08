package org.tutorial.tree.avl;

import java.util.function.Consumer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AVLTree<T extends Comparable> {

    private AVLNode<T> root;

    public void leftVisitor(Consumer<AVLNode<T>> fn) {
        root.leftVisitor(fn);
    }

    /**
     * 新增
     * 
     * @param value
     * @return
     */
    public AVLNode<T> add(T value) {
        // 提前失败
        if (value == null || (root != null && root.parent != null)) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            root = new AVLNode<T>(value);
            return root;
        } else {
            AVLNode<T> parent = root;
            AVLNode<T> node = null;
            while (true) {
                AVLNode temp = null;
                boolean isRight = false;
                if (value.compareTo(parent.value) > 0) {
                    temp = parent.right;
                    isRight = true;
                } else if (value.compareTo(parent.value) < 0) {
                    temp = parent.left;
                    isRight = false;
                } else {
                    throw new IllegalArgumentException();
                }
                if (temp == null) {
                    node = new AVLNode<T>(value, parent, isRight);
                    // 层数未变就不会失衡
                    if (parent.left == null || parent.right == null) {
                        AVLNode root = parent.fireChange();// 返回的是调整之后的失衡树
                        if (root.parent == null && root != this.root) {
                            this.root = root;// 更新根节点
                        }
                    }
                    break;
                } else {
                    parent = temp;
                }
            }
            return node;
        }
    }

    /**
     * 类AVLTree.java的实现描述：节点
     * 
     * @author cl150 2019年4月3日 下午3:37:37
     */
    private static class AVLNode<T extends Comparable> {

        private T          value;

        private AVLNode<T> parent;

        private AVLNode<T> left;

        private AVLNode<T> right;

        /**
         * FIXME 优化的第一步缓存高度、优化的第二步把高度作为属性（但这步太难了）
         * 
         * @return
         */
        private int getHeight() {
            return Math.max(left == null ? 0 : left.getHeight(), right == null ? 0 : right.getHeight()) + 1;
        }

        private void leftVisitor(Consumer<AVLNode<T>> fn) {
            if (left != null) {
                left.leftVisitor(fn);
            }
            fn.accept(this);
            if (right != null) {
                right.leftVisitor(fn);
            }
        }

        public AVLNode(T value){
            this.value = value;
        }

        public AVLNode(T value, AVLNode<T> parent, boolean isRight){
            this(value);
            this.parent = parent;
            if (isRight) {
                parent.right = this;
            } else {
                parent.left = this;
            }
        }

        /**
         * @param isAdd 新增or删除
         */
        private AVLNode fireChange() {
            AVLNodeState state = getState();
            switch (state) {
                case LL:
                case RR:
                case LR:
                case RL:
                    Consumer<AVLNode> fn = state.action;
                    fn.accept(this);
                    return this.parent;
                case NORMAL:
                    if (this.parent != null) {
                        return this.parent.fireChange();
                    } else {
                        return this;
                    }
                default:
                    throw new IllegalStateException();
            }

        }

        /**
         * 当前树的状态：LL\LR\RL\RR
         * 
         * @return
         */
        private AVLNodeState getState() {
            if (height(this.left) - height(this.right) == 2) {
                AVLNode node = this.left;
                if (height(node.left) > height(node.right)) {
                    return AVLNodeState.LL;
                } else if (height(node.left) < height(node.right)) {
                    return AVLNodeState.LR;
                } else {
                    throw new IllegalStateException();
                }
            } else if (height(this.right) - height(this.left) == 2) {
                AVLNode node = this.right;
                if (height(node.left) > height(node.right)) {
                    return AVLNodeState.RL;
                } else if (height(node.left) < height(node.right)) {
                    return AVLNodeState.RR;
                } else {
                    throw new IllegalStateException();
                }
            } else if (Math.abs(height(this.left) - height(this.right)) < 2) {
                return AVLNodeState.NORMAL;
            } else {
                throw new IllegalStateException();
            }
        }

    }

    private static int height(AVLNode node) {
        if (node == null) {
            return 0;
        } else {
            return node.getHeight();
        }
    }

    /**
     * 类AVLNode.java的实现描述：状态机
     * 
     * @author cl150 2019年4月3日 上午9:29:51
     */
    private static enum AVLNodeState {
                                      LL(LLFn), RR(RRFn), LR(LRFn), RL(RLFn), NORMAL(null);

        private Consumer<AVLNode> action;

        private AVLNodeState(Consumer<AVLNode> action){
            this.action = action;
        }

    }

    private final static Consumer<AVLNode> LLFn = new Consumer<AVLNode>() {

                                                    @Override
                                                    public void accept(AVLNode t) {
                                                        AVLNode left = t.left;                  // 失衡节点的左节点
                                                        {
                                                            // 左节点的右节点变成失衡节点的左节点
                                                            if (null != left.right) {
                                                                left.right.parent = t;
                                                            }
                                                            t.left = left.right;
                                                        }
                                                        {
                                                            // 左节点的右节点变成失衡节点
                                                            left.right = t;
                                                            // parent交换
                                                            swapParent(t, left);
                                                        }
                                                    }
                                                };

    private final static Consumer<AVLNode> RRFn = new Consumer<AVLNode>() {

                                                    @Override
                                                    public void accept(AVLNode t) {
                                                        AVLNode right = t.right;                // 失衡节点的右节点
                                                        {
                                                            // 右节点的左节点变成失衡节点的右节点
                                                            if (right.left != null) {
                                                                right.left.parent = t;
                                                            }
                                                            t.right = right.left;
                                                        }
                                                        {
                                                            // 右节点的左节点变成失衡节点
                                                            right.left = t;
                                                            // parent交换
                                                            swapParent(t, right);
                                                        }
                                                    }
                                                };

    private final static Consumer<AVLNode> LRFn = new Consumer<AVLNode>() {

                                                    @Override
                                                    public void accept(AVLNode t) {
                                                        AVLNode left = t.left;
                                                        RRFn.accept(left);                      // 右右失衡节点的左节点，变成左左失衡
                                                        LLFn.accept(t);                         // 左左失衡节点
                                                    }
                                                };

    private final static Consumer<AVLNode> RLFn = new Consumer<AVLNode>() {

                                                    @Override
                                                    public void accept(AVLNode t) {
                                                        AVLNode right = t.right;
                                                        LLFn.accept(right);                     // 左左失衡节点的右节点，变成右右平衡
                                                        RRFn.accept(t);                         // 右右失衡节点
                                                    }
                                                };

    private static void swapParent(AVLNode source, AVLNode target) {
        target.parent = source.parent;
        if (source.parent != null) {
            if (source.parent.left == source) {
                source.parent.left = target;
            } else if (source.parent.right == source) {
                source.parent.right = target;
            } else {
                throw new IllegalStateException();
            }
        }
        source.parent = target;
    }

}
