import com.javaccy.core.Main;
import com.javaccy.entity.User;
import com.javaccy.utils.SearchUtils;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class HibernateTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void before() {
        session = Main.getSession();
        sessionFactory = session.getSessionFactory();
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        user.setUsername("javaccy");
        user.setPassword("nihao");
        user.setDelFlag("0");
        user.setAbout(ss);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setUpdateTime(user.getUpdateTime());
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
            Assert.isTrue(true);
        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
            }
        }finally {
            session.close();
        }

    }


    @Test
    public void testSearch() {
        List<User> users = searchArticle(0, 10, "记者");
        for (User user : users) {
            System.out.println(user.getAbout());
        }
    }

    public List<User> searchArticle(int pageNum, int pageSize, String keyword) {
        FullTextSession fts = Search.getFullTextSession(session);
        QueryBuilder qb = fts.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        Query luceneQuery = qb.keyword().onFields("about").matching(keyword).createQuery();
        FullTextQuery query = fts.createFullTextQuery(luceneQuery,User.class);
        query.setFirstResult(1);
        query.setMaxResults(pageSize);
        List<User> data = query.list();

        //将数据高亮
        return SearchUtils.hightLight(luceneQuery, data, "about");
    }

    private static String ss = "快来看，洋记者怎么学习总书记讲话 治国理政\n" +
            "美好梦想，奔驰在辽阔的草原 专题\n" +
            "人民日报七论学习贯彻习近平\"7·26\"重要讲话精神\n" +
            "王毅谈南海行为准则磋商:不希望域外国家指手划脚\n" +
            "香港流感死亡人数超SARS? 香港卫生署:不可比较\n" +
            "中方67次谈印军越界:从高度克制到空前严厉\n" +
            "送房、送户口、送钱 主流二线城市上演\"抢人大战\"\n" +
            "美坠海军机遗骸被发现 失踪3士兵搜救工作遭取消\n" +
            "媒体:李文星事件后 静海村民抱怨\"馒头销量少9成\"\n" +
            "旅行社购票\"特殊渠道\" 有人可能正拿你的票坐火车\n" +
            "吴京一家都不是中国大陆籍？谢楠母亲晒护照辟谣\n" +
            "利比亚撤侨舰长:海军反应没\"战狼2\"那么迟缓\n" +
            "男子观看《战狼2》时猝死 目击者一句话让人心痛\n" +
            "山东聊城\"蝶蓓蕾\"最大传销案:头目2成是大学生\n" +
            "传销亲历者:警察对窝点很了解 当地人习以为常\n" +
            "中国游客在德行纳粹礼被拘?这些外国禁忌不可不知\n" +
            "拿中国护照也不能回国?国航:前往港澳须持通行证\n" +
            "李文星家人收官方慰问金2千元 将获司法援助\n" +
            "辽宁6人遭雷击3人骨折 居民称:雷电击碎玻璃\n" +
            "死直男消失之后\n" +
            "女子遭好友欺骗给人当小三，发生关系后知道真相\n" +
            "财经 | 贾跃亭回来了？第一站香港鼓捣新能源车\n" +
            "梁军:与老乐视切割需彻底 今夏最火财经会议来袭\n" +
            "科技 | 黑莓夏普8月8日回归：能重返一线品牌吗？\n" +
            "永恒的神秘电波 有人猜测:发射电台是核武器按钮\n" +
            "体育 | 李毅组天王级约球阵容 NBA最无聊巨星是他?\n" +
            "从中超\"大恶人\"变球队后防中坚 他不进国足没天理\n" +
            "娱乐 | 表情亮了！刘亦菲素颜吃火锅直呼超级爽\n" +
            "吴京回应\"战狼2\"创新纪录 :不忘初心 与君共勉\n" +
            "女人 | 王菲：天后级段子手 不一样的女神\n" +
            "杨幂美出新高度 网友:四海八荒只服你的大长腿\n" +
            "网易号 | 不可思议，这种鸟离婚率只有0.3%。\n" +
            "为什么你不用在意大多数育儿文？";
}
