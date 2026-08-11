package com.robothaver.mp3reorder.core.utils;

import javafx.scene.Node;

public class NodeUtils {

    private NodeUtils() {
        /* This utility class should not be instantiated */
    }

    public static void setNodeVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }
}
