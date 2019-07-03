package model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class Dog {
    Long id;
    String name;
    Date date;
    Integer height;
    Double weight;
}