package uk.co.kenfos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    private boolean active;

    @Override
    public String toString() {
        return "User{id='" + id + ", name='" + name + ", active=" + active + "}";
    }
}
