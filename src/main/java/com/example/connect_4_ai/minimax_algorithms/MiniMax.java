package com.example.connect_4_ai.minimax_algorithms;

import com.example.connect_4_ai.utilities.Node;
import com.example.connect_4_ai.utilities.State;

public abstract class MiniMax {
    protected int maxLevel;
    public Node root;
    public int expandedNodes = 1;

    public long time;
    public int Decision(long bitsBoard, int k) {
        this.maxLevel = k;
        long startTime = System.currentTimeMillis();
        root = new Node(new State(bitsBoard));
        Node rot = maximize(root, 0);
        time = (System.currentTimeMillis() - startTime);
        System.out.println((System.currentTimeMillis() - startTime) + " ms");
        System.out.println("actual " + expandedNodes);
        return rot.getChosenCol();
//        return Util.longToChar2dArray(maximize(root, 0).chosenNode.state.board);
    }

    protected abstract Node maximize(Node node, int level);

}
