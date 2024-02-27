package org.jetbrains.assignment;

public record CommandDTO(Direction direction, Integer steps) {
    @Override
    public String toString() {
        return "[" +
                "direction=" + direction +
                ", steps=" + steps +
                ']';
    }
}
