package com.roadtomastery.project.dao;

import com.roadtomastery.project.entity.db.Item;
import com.roadtomastery.project.entity.db.ItemType;
import com.roadtomastery.project.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository //不這個repository會怎樣？
public class FavoriteDao {
    @Autowired
    private SessionFactory sessionFactory;

    // Insert a favorite record to the database
    public void setFavoriteItem(String userId, Item item) {
        //Create Session Object
        Session session = null; //什麼是session , session是代表一次DB的操作。

        //我怎麼知道要Try Catch？很多時候Try Catch是被強制加上去的
        //大部分情況係IDE會標紅色。IDE會講unhandled exception。
        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            user.getItemSet().add(item);
            session.beginTransaction(); //開始跟db Transection.
            session.save(user);
            session.getTransaction().commit(); //真正寫進，commit到DB裡面。
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback(); //rollback需要很嚴格的例子，銀行。原子性
        } finally { //finally的意思是什麼？ 不管有沒有exception， finally都會執行一下。
            if (session != null) session.close(); //不close會有什麼後果？DB會有Active Connection
                                                  //Connection出錯了，就留在那，回不來了。
        }
    }

    // Remove a favorite record from the database
    public void unsetFavoriteItem(String userId, String itemId) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            Item item = session.get(Item.class, itemId);
            user.getItemSet().remove(item);
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    public Set<Item> getFavoriteItems(String userId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(User.class, userId).getItemSet();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return new HashSet<>();
    }

    // Get favorite item ids for the given user
    public Set<String> getFavoriteItemIds(String userId) {
        Set<String> itemIds = new HashSet<>();

        try (Session session = sessionFactory.openSession()) {
            Set<Item> items = session.get(User.class, userId).getItemSet();
            for(Item item : items) {
                itemIds.add(item.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return itemIds;
    }

    // Get favorite items for the given user. The returned map includes three entries like {"Video": [item1, item2, item3], "Stream": [item4, item5, item6], "Clip": [item7, item8, ...]}
    public Map<String, List<String>> getFavoriteGameIds(Set<String> favoriteItemIds) {
        Map<String, List<String>> itemMap = new HashMap<>();
        for (ItemType type : ItemType.values()) {
            itemMap.put(type.toString(), new ArrayList<>());
        }

        try (Session session = sessionFactory.openSession()) {
            for(String itemId : favoriteItemIds) {
                Item item = session.get(Item.class, itemId);
                itemMap.get(item.getType().toString()).add(item.getGameId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return itemMap;
    }

}
