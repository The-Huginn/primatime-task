package com.thehuginn;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // 1,
        // 2 -> 3, 4
        // 5 -> 6 -> 7
        List<Node> list = new ArrayList<>();
        list.add(node(1));
        list.add(node(2, new Node[]{ node(3), node(4) }));
        list.add(node(5, new Node[]{ node(6, new Node[]{ node(7) })}));

        // should return 4
        // because (1 + 2 + 3 + 4 + 5 + 6 + 7) / 7 = 4
        Pair meanPair = getMeanValue(list);
        System.out.printf("Mean value is %.2f and weighted mean is %.2f%n", meanPair.getMean(), meanPair.getWeightedMean());

        list.add(node(10));
        meanPair = getMeanValue(list);
        System.out.printf("Mean value after adding node 10 is %.2f and weighted mean is %.2f%n", meanPair.getMean(), meanPair.getWeightedMean());
    }

    public interface Node {
        double getValue();
        List<Node> getNodes();
    }

    public static Pair getMeanValue(List<Node> nodes) {
        Pair meanPair = new Pair();
        mean(meanPair, nodes, 1.0);
        return meanPair;
    }

    private static void mean(Pair meanPair, List<Node> nodes, double value) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }

        for (Node node : nodes) {
            meanPair.addValue(node.getValue(), value);
            mean(meanPair, node.getNodes(), 0.9 * value);
        }
    }

    public static final class Pair {
        private double sum = 0.0;
        private double weightedSum = 0.0;
        private int count = 0;

        public Pair() {}

        public void addValue(double value, double weight) {
            sum += value;
            weightedSum += value * weight;
            count++;
        }

        public double getMean() {
            if (count == 0) {
                throw new RuntimeException("No values to calculate the mean from were provided");
            }

            return sum / ((double) count);
        }

        public double getWeightedMean() {
            if (count == 0) {
                throw new RuntimeException("No values to calculate the mean from were provided");
            }

            return weightedSum / ((double) count);
        }
    }

    // builders

    public static Node node(double value) {
        return node(value, new Node[]{});
    }

    public static Node node(double value, Node[] nodes) {
        return new Node() {
            public double getValue() {
                return value;
            }
            public List<Node> getNodes() {
                return Arrays.asList(nodes);
            }
        };
    }
}