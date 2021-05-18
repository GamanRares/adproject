package com.adproject.utils;

public class ExecutionTime {
    private Long javaExecutionTime;
    private Long databaseExecutionTime;

    public ExecutionTime(Long javaExecutionTime, Long databaseExecutionTime) {
        this.javaExecutionTime = javaExecutionTime;
        this.databaseExecutionTime = databaseExecutionTime;
    }

    public Long getJavaExecutionTime() {
        return javaExecutionTime;
    }

    public Long getDatabaseExecutionTime() {
        return databaseExecutionTime;
    }

    @Override
    public String toString() {
        return "ExecutionTime{" +
                "javaExecutionTime=" + (double) javaExecutionTime / 1000 +
                ", databaseExecutionTime=" + (double) databaseExecutionTime / 1000 +
                '}';
    }
}
