package com.trentovskyi.processor;

import com.trentovskyi.models.Card;

import java.util.Arrays;
import java.util.Collection;

public class FaceCounter {

    private int[] faces = new int[Card.facesSetSize];
    private int keysCount;

    public void clear() {
        keysCount = 0;
        Arrays.fill(faces, 0);
    }

    public boolean containsSameFaces(int value) {
        for (int faceCount : faces) {
            if (faceCount == value) {
                return true;
            }
        }
        return false;
    }

    public int getKeysCount() {
        return keysCount;
    }

    public void putAll(Collection<Character> combination) {
        for (Character face : combination) {
            int index = Card.getFaceIndex(face);
            if (faces[index] == 0) {
                keysCount++;
            }
            faces[index]++;
        }
    }
}
