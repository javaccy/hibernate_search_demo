package com.javaccy.utils;

import com.javaccy.entity.User;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {

    /**
     * 高亮显示文章
     *
     * @param query {@link org.apache.lucene.search.Query}
     * @param data 未高亮的数据
     * @param fields 需要高亮的字段
     * @return 高亮数据
     */
    public static List<User> hightLight(Query query, List<User> data, String... fields) {
        List<User> result = new ArrayList<User>();
        Formatter formatter = new SimpleHTMLFormatter("<b style=\"color:red\">", "</b>");
        QueryScorer queryScorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, queryScorer);
        // 使用IK中文分词
        IKAnalyzer analyzer = new IKAnalyzer();
        for (User a : data) {
            // 构建新的对象进行返回，避免页面错乱（我的页面有错乱）
            User article = new User();
            for (String fieldName : fields) {
                // 获得字段值，并给新的文章对象赋值
                Object fieldValue = ReflectionUtils.invokeMethod(BeanUtils.getPropertyDescriptor(User.class, fieldName).getReadMethod(),a);
                ReflectionUtils.invokeMethod(BeanUtils.getPropertyDescriptor(User.class, fieldName).getWriteMethod(),article, fieldValue);
                String hightLightFieldValue = null;
                try {
                    hightLightFieldValue = highlighter.getBestFragment(analyzer, fieldName, String.valueOf(fieldValue));
                } catch (Exception e) {
                    throw new RuntimeException("高亮显示关键字失败", e);
                }
                // 如果高亮成功则重新赋值
                if (hightLightFieldValue != null) {
                    ReflectionUtils.invokeMethod(BeanUtils.getPropertyDescriptor(User.class, fieldName).getWriteMethod(),article,hightLightFieldValue);
                }
            }
            // 赋值ID
            ReflectionUtils.invokeMethod(BeanUtils.getPropertyDescriptor(User.class, "id").getWriteMethod(),article, a.getId());
            result.add(article);
        }
        return result;
    }

}
