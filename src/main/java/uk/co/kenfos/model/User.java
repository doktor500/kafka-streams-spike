package uk.co.kenfos.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String name;

    @Override
    public String toString() {
        return "User{id='" + id + ", name='" + name + "}";
    }
}
