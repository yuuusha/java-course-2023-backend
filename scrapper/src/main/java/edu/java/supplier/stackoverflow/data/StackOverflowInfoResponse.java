package edu.java.supplier.stackoverflow.data;

public record StackOverflowInfoResponse(
    StackOverflowItem[] items
) {
    public static final StackOverflowInfoResponse EMPTY = new StackOverflowInfoResponse(null);
}
