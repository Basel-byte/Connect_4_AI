package com.example.connect_4_ai.minimax_algorithms;

import com.example.connect_4_ai.utilities.Node;

public class MinimaxWithoutPruning extends MiniMax {


    //r ->cpu
    @Override
    public Node maximize(Node node, int level) {
        //   expandedNodes++;
        node.visited = true;
        if (level == maxLevel || node.isTerminal()) {
            node.score = Evaluation.evaluateScore(node.state.board);
//            System.out.println(maxLevel);
            return node;
        }

        node.score = Integer.MIN_VALUE;
        for (Node child : node.expand()) {
            expandedNodes++;
            child.visited = true;
            child = minimize(child, level + 1);
            // System.out.print("level " + level + " " + child.score + " ");
            if (child.score > node.score) {
                node.score = child.score;
                node.chosenNode = child;
            }
        }
        // System.out.println();
        return node;

    }

    public Node minimize(Node node, int level) {
        node.visited = true;
        //  expandedNodes++;
        if (level == maxLevel || node.isTerminal()) {
            // int eval = Evaluation.eval(board, 'y');

            node.score = Evaluation.evaluateScore(node.state.board);

            return node;
        }

        node.score = Integer.MAX_VALUE;
        for (Node child : node.expand()) {
            expandedNodes++;
            child.visited = true;
            // long bit = Util.char2dArrayToLong(child);
            child = maximize(child, level + 1);
            //System.out.print("level " + level + " " + child.score + " ");
            if (child.score < node.score) {
                node.score = child.score;
                node.chosenNode = child;
            }
        }
        //System.out.println("");
        return node;
    }

}
