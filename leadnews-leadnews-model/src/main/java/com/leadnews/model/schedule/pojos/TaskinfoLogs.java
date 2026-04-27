package com.leadnews.model.schedule.pojos;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author leadnews
 */
@Data
@TableName("taskinfo_logs")
public class TaskinfoLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 浠诲姟id
     */
    @TableId(type = IdType.ID_WORKER)
    private Long taskId;

    /**
     * 鎵ц鏃堕棿
     */
    @TableField("execute_time")
    private Date executeTime;

    /**
     * 鍙傛暟
     */
    @TableField("parameters")
    private byte[] parameters;

    /**
     * 浼樺厛绾?
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 浠诲姟绫诲瀷
     */
    @TableField("task_type")
    private Integer taskType;

    /**
     * 鐗堟湰鍙?鐢ㄤ箰瑙傞攣
     */
    @Version
    private Integer version;

    /**
     * 鐘舵€?0=int 1=EXECUTED 2=CANCELLED
     */
    @TableField("status")
    private Integer status;


}

