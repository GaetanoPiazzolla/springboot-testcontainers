package gae.piaz.springtc.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

// Serializable it's used to serialize data into RedisCache.
@Entity
public class Customer implements Serializable {

    @Id
    private Integer id;
    private String name;

    public Customer() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
