package org.cayambe.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "Category.findAll", query = "SELECT c from Category c")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category {


    @Id
    @SequenceGenerator(
            name = "category_sequence",
            allocationSize = 1,
            initialValue = 1020
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
    @Column(name = "category_id", updatable = false, nullable = false)
    protected Integer id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    protected String name;

    protected String header;

    protected Boolean visible;

    @Column(name = "image_path", length = 120)
    protected String imagePath;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    protected Category parent;

    protected LocalDateTime created = LocalDateTime.now();

    protected LocalDateTime updated;

    @Version
    protected Integer version = 0;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(header, category.header) &&
                Objects.equals(visible, category.visible) &&
                Objects.equals(imagePath, category.imagePath) &&
                Objects.equals(created, category.created) &&
                Objects.equals(updated, category.updated) &&
                Objects.equals(version, category.version)
             ;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, header, visible, imagePath, created, updated);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Category.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("header='" + header + "'")
                .add("visible=" + visible)
                .add("imagePath='" + imagePath + "'")
                .add("parent=" + parent)
                .add("created=" + created)
                .add("updated=" + updated)
                .add("version=" + version)
                .toString();
    }
}
