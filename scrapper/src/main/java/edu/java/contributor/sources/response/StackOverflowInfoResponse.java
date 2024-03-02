package edu.java.contributor.sources.response;

public record StackOverflowInfoResponse(StackOverflowItem[] items) {
    public static final StackOverflowInfoResponse
        EMPTY = new StackOverflowInfoResponse(null);
}
