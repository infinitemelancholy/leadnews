package com.leadnews.utils.common;

/**
 * Twitter_Snowflake<br>
 * SnowFlake鐨勭粨鏋勫涓?姣忛儴鍒嗙敤-鍒嗗紑):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1浣嶆爣璇嗭紝鐢变簬long鍩烘湰绫诲瀷鍦↗ava涓槸甯︾鍙风殑锛屾渶楂樹綅鏄鍙蜂綅锛屾鏁版槸0锛岃礋鏁版槸1锛屾墍浠d涓€鑸槸姝ｆ暟锛屾渶楂樹綅鏄?<br>
 * 41浣嶆椂闂存埅(姣绾?锛屾敞鎰忥紝41浣嶆椂闂存埅涓嶆槸瀛樺偍褰撳墠鏃堕棿鐨勬椂闂存埅锛岃€屾槸瀛樺偍鏃堕棿鎴殑宸€硷紙褰撳墠鏃堕棿鎴?- 寮€濮嬫椂闂存埅)
 * 寰楀埌鐨勫€硷級锛岃繖閲岀殑鐨勫紑濮嬫椂闂存埅锛屼竴鑸槸鎴戜滑鐨刬d鐢熸垚鍣ㄥ紑濮嬩娇鐢ㄧ殑鏃堕棿锛岀敱鎴戜滑绋嬪簭鏉ユ寚瀹氱殑锛堝涓嬩笅闈㈢▼搴廔dWorker绫荤殑startTime灞炴€э級銆?1浣嶇殑鏃堕棿鎴紝鍙互浣跨敤69骞达紝骞碩 = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10浣嶇殑鏁版嵁鏈哄櫒浣嶏紝鍙互閮ㄧ讲鍦?024涓妭鐐癸紝鍖呮嫭5浣峝atacenterId鍜?浣峸orkerId<br>
 * 12浣嶅簭鍒楋紝姣鍐呯殑璁℃暟锛?2浣嶇殑璁℃暟椤哄簭鍙锋敮鎸佹瘡涓妭鐐规瘡姣(鍚屼竴鏈哄櫒锛屽悓涓€鏃堕棿鎴?浜х敓4096涓狪D搴忓彿<br>
 * 鍔犺捣鏉ュ垰濂?4浣嶏紝涓轰竴涓狶ong鍨嬨€?br>
 * SnowFlake鐨勪紭鐐规槸锛屾暣浣撲笂鎸夌収鏃堕棿鑷鎺掑簭锛屽苟涓旀暣涓垎甯冨紡绯荤粺鍐呬笉浼氫骇鐢烮D纰版挒(鐢辨暟鎹腑蹇僆D鍜屾満鍣↖D浣滃尯鍒?锛屽苟涓旀晥鐜囪緝楂橈紝缁忔祴璇曪紝SnowFlake姣忕鑳藉浜х敓26涓嘔D宸﹀彸銆?
 */
public class SnowflakeIdWorker {

    // ==============================Fields===========================================
    /** 寮€濮嬫椂闂存埅 (2015-01-01) */
    private final long twepoch = 1420041600000L;

    /** 鏈哄櫒id鎵€鍗犵殑浣嶆暟 */
    private final long workerIdBits = 5L;

    /** 鏁版嵁鏍囪瘑id鎵€鍗犵殑浣嶆暟 */
    private final long datacenterIdBits = 5L;

    /** 鏀寔鐨勬渶澶ф満鍣╥d锛岀粨鏋滄槸31 (杩欎釜绉讳綅绠楁硶鍙互寰堝揩鐨勮绠楀嚭鍑犱綅浜岃繘鍒舵暟鎵€鑳借〃绀虹殑鏈€澶у崄杩涘埗鏁? */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 鏀寔鐨勬渶澶ф暟鎹爣璇唅d锛岀粨鏋滄槸31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 搴忓垪鍦╥d涓崰鐨勪綅鏁?*/
    private final long sequenceBits = 12L;

    /** 鏈哄櫒ID鍚戝乏绉?2浣?*/
    private final long workerIdShift = sequenceBits;

    /** 鏁版嵁鏍囪瘑id鍚戝乏绉?7浣?12+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /** 鏃堕棿鎴悜宸︾Щ22浣?5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 鐢熸垚搴忓垪鐨勬帺鐮侊紝杩欓噷涓?095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 宸ヤ綔鏈哄櫒ID(0~31) */
    private long workerId;

    /** 鏁版嵁涓績ID(0~31) */
    private long datacenterId;

    /** 姣鍐呭簭鍒?0~4095) */
    private long sequence = 0L;

    /** 涓婃鐢熸垚ID鐨勬椂闂存埅 */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================
    /**
     * 鏋勯€犲嚱鏁?
     * @param workerId 宸ヤ綔ID (0~31)
     * @param datacenterId 鏁版嵁涓績ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================
    /**
     * 鑾峰緱涓嬩竴涓狪D (璇ユ柟娉曟槸绾跨▼瀹夊叏鐨?
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //濡傛灉褰撳墠鏃堕棿灏忎簬涓婁竴娆D鐢熸垚鐨勬椂闂存埑锛岃鏄庣郴缁熸椂閽熷洖閫€杩囪繖涓椂鍊欏簲褰撴姏鍑哄紓甯?
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //濡傛灉鏄悓涓€鏃堕棿鐢熸垚鐨勶紝鍒欒繘琛屾绉掑唴搴忓垪
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //姣鍐呭簭鍒楁孩鍑?
            if (sequence == 0) {
                //闃诲鍒颁笅涓€涓绉?鑾峰緱鏂扮殑鏃堕棿鎴?
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //鏃堕棿鎴虫敼鍙橈紝姣鍐呭簭鍒楅噸缃?
        else {
            sequence = 0L;
        }

        //涓婃鐢熸垚ID鐨勬椂闂存埅
        lastTimestamp = timestamp;

        //绉讳綅骞堕€氳繃鎴栬繍绠楁嫾鍒颁竴璧风粍鎴?4浣嶇殑ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * 闃诲鍒颁笅涓€涓绉掞紝鐩村埌鑾峰緱鏂扮殑鏃堕棿鎴?
     * @param lastTimestamp 涓婃鐢熸垚ID鐨勬椂闂存埅
     * @return 褰撳墠鏃堕棿鎴?
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 杩斿洖浠ユ绉掍负鍗曚綅鐨勫綋鍓嶆椂闂?
     * @return 褰撳墠鏃堕棿(姣)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    //==============================Test=============================================
    /** 娴嬭瘯 */
    public static void main(String[] args) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(10, 10);
        for (int i = 0; i < 10000; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
        }
    }
}
