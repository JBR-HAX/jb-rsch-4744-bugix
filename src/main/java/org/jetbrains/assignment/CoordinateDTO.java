package org.jetbrains.assignment;

public record CoordinateDTO(Integer x, Integer y) {
    @Override
    public String toString() {
        return "[" +
                "x=" + x +
                ", y=" + y +
                ']';
    }
}
