package org.cayambe.model;

import java.time.LocalDateTime;

/**
 * Ein einfach Objekt zum Testen. Es erbt von {@link Category}, damit auch die Werte ohne Setter, wie id und version, leicht gesetzt werden können
 */
public class TestCategoryObject extends Category {

    public TestCategoryObject(Integer id) {
        this.id = id;
    }


    TestCategoryObject(Integer id,
                       String name,
                       String header,
                       Boolean visible,
                       String imagePath,
                       Category parent,
                       LocalDateTime created,
                       LocalDateTime updated,
                       Integer version) {
        this.id = id;
        this.name = name;
        this.header = header;
        this.visible = visible;
        this.imagePath = imagePath;
        this.parent = parent;
        this.created = created;
        this.updated = updated;
        this.version = version;
    }

}
