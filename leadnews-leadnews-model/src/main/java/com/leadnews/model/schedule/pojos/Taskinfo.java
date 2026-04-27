package com.leadnews.model.schedule.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("taskinfo")
public class Taskinfo implements Serializable {

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


}

