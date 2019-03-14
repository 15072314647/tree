package org.tutorial.tree.model.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.tutorial.tree.model.IData;

public class DataImpl implements IData {

    Integer     id;

    Integer     parentId;

    List<IData> children = new ArrayList<>();

    public DataImpl(Integer id, Integer parentId){
        this.id = id;
        this.parentId = parentId;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Serializable getParentId() {
        return parentId;
    }

    @Override
    public List<IData> getChildren() {
        return children;
    }

    public static void main(String[] args) {
        DataImpl i1 = new DataImpl(1, 0);
        DataImpl i2 = new DataImpl(2, 0);
        DataImpl i3 = new DataImpl(3, 0);
        DataImpl i11 = new DataImpl(11, 1);
        DataImpl i21 = new DataImpl(21, 2);
        DataImpl i22 = new DataImpl(22, 2);
        DataImpl i31 = new DataImpl(31, 3);
        DataImpl i32 = new DataImpl(32, 3);
        DataImpl i33 = new DataImpl(33, 3);
        List<DataImpl> datas = new ArrayList<>();
        datas.add(i31);
        datas.add(i32);
        datas.add(i11);
        datas.add(i21);
        datas.add(i2);
        datas.add(i3);
        datas.add(i22);
        datas.add(i1);
        datas.add(i33);
        List<IData> tree = IData.tree(0, datas);
        System.out.println(tree);

    }
}
