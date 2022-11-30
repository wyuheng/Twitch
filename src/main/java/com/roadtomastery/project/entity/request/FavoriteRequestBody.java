package com.roadtomastery.project.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadtomastery.project.entity.db.Item;

public class FavoriteRequestBody {
    @JsonProperty("favorite")
    private Item favoriteItem;

    public Item getFavoriteItem() {
        return favoriteItem;
    }

}
