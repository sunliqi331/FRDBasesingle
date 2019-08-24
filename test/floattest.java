import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class floattest {

    public static void main(String[] args) {
        List<Float> floatList = Lists.newArrayList();
        floatList.add(123.1f);
        floatList.add(123.2f);
        floatList.add(223.3f);
        floatList.add(323.1f);
        floatList.add(23.12f);
        floatList.add(3.23f);
        // 初始数据
        Map<String, Float> smap = Maps.newHashMap();
        smap.put("1", 123.3f);
        smap.put("3", 123.2f);
        smap.put("2", 223.3f);


        // 1.8使用lambda表达式
        List<Map.Entry<String, Float>> list2 = Lists.newArrayList();
        list2.addAll(smap.entrySet());
        Collections.sort(list2, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        list2.forEach(entry -> {
            System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());
        });

    }

}
