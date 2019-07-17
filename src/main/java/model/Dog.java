package model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class Dog {
    Long id;

    @NotNull
    @Size(max = 100)
    String name;

    @Past
    Date date;

    @Positive
    Integer height;

    @Positive
    Double weight;
}