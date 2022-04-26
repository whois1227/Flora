package dev.jmsaez.florafragments.model.entity;

public class DeleteResponse {
    public int rows;

    public DeleteResponse(){}

    @Override
    public String toString() {
        return "DeleteResponse{" +
                "rows=" + rows +
                '}';
    }
}
