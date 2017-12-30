package com.shuvojit.crux.model.rec_model;

/**
 * Created by SHOBOJIT on 12/30/2017.
 */

public class Update_post_model {
    private String title;
    private String description;

    public Update_post_model(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
