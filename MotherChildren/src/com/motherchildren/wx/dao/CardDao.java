package com.motherchildren.wx.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.motherchildren.wx.moudle.Card;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/23.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
@Component
public class CardDao {
    @Resource
    private JdbcTemplate serverUserTemplate;

    public Card getCard(String cardId) {
        String sql = String.format("select * from MotherChildrenCard WHERE id = '%s'", cardId);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            Card card = new Card();
            card.setSessionId(Long.valueOf(map4dept.get("session_id").toString()));
            card.setId(((String) map4dept.get("id")));
            card.setCount(Integer.valueOf(map4dept.get("count").toString()));
            return card;
        }
        return null;
    }

    public List<Card> getCards(Long sessionId) {
        List<Card> cards = new ArrayList<Card>();
        String sql = String.format("select * from MotherChildrenCard WHERE session_id = '%s'", sessionId);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            Card card = new Card();
            card.setSessionId(Long.valueOf(map4dept.get("session_id").toString()));
            card.setId(((String) map4dept.get("id")));
            cards.add(card);
        }
        return cards;
    }

    public void insertCard(Card card) {
        String sql = String.format("insert into MotherChildrenCard (id, session_id, count) values('%s', '%s', %d)", card.getId(), card.getSessionId(), card.getCount());
        serverUserTemplate.execute(sql);
    }

    public void updateCard(Card card) {
        String sql = "update MotherChildrenCard card SET " +
            "card.session_id = '"+ card.getSessionId() +
            "', card.count = '" + card.getCount() +
            "' where card.id = " + card.getId();
        serverUserTemplate.execute(sql);

    }
}
