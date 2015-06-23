package com.example.amin.todolist;

/**
 * Created by Amin on 6/19/2015.
 */
public class Todo {

    private String title;
    private boolean complete;

    public Todo() {

    }

    public Todo(String title, boolean complete) {
        this.title = title;
        this.complete = complete;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getTitle() {
        return title;
    }

    public boolean isComplete() {
        return complete;
    }
}
