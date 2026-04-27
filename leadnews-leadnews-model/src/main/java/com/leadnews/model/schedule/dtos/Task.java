package com.leadnews.model.schedule.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {

    /**
     * 浠诲姟id
     */
    private Long taskId;
    /**
     * 绫诲瀷
     */
    private Integer taskType;

    /**
     * 浼樺厛绾?
     */
    private Integer priority;

    /**
     * 鎵цid
     */
    private long executeTime;

    /**
     * task鍙傛暟
     */
    private byte[] parameters;
    
}
