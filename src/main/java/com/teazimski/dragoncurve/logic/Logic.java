package logic;

import java.util.ArrayList;
import java.util.List;

public class Logic {
    private final List<Integer> turns = new ArrayList<>();
    private int currentIndex = 0;

    public Logic() {
        turns.add(1);
    }

    public int getNextTurn() {
        if (currentIndex >= turns.size()) {
            expandSequence();
        }
        return turns.get(currentIndex++);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    private void expandSequence() {
        int oldSize = turns.size();
        turns.add(1);
        for (int i = oldSize - 1; i >= 0; i--) {
            turns.add(-turns.get(i));
        }
    }
}
