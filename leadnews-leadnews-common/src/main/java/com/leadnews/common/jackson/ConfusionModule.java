package com.leadnews.common.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfusionModule extends Module {

    public final static String MODULE_NAME = "jackson-confusion-encryption";
    public final static Version VERSION = new Version(1,0,0,null,"leadnews",MODULE_NAME);

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(new ConfusionSerializerModifier());
        context.addBeanDeserializerModifier(new ConfusionDeserializerModifier());
    }

    /**
     * 娉ㄥ唽褰撳墠妯″潡
     * @return
     */
    public static ObjectMapper registerModule(ObjectMapper objectMapper){
        //CamelCase绛栫暐锛孞ava瀵硅薄灞炴€э細personId锛屽簭鍒楀寲鍚庡睘鎬э細persionId
        //PascalCase绛栫暐锛孞ava瀵硅薄灞炴€э細personId锛屽簭鍒楀寲鍚庡睘鎬э細PersonId
        //SnakeCase绛栫暐锛孞ava瀵硅薄灞炴€э細personId锛屽簭鍒楀寲鍚庡睘鎬э細person_id
        //KebabCase绛栫暐锛孞ava瀵硅薄灞炴€э細personId锛屽簭鍒楀寲鍚庡睘鎬э細person-id
        // 蹇界暐澶氫綑瀛楁锛屾姏閿?
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper.registerModule(new ConfusionModule());
    }

}

