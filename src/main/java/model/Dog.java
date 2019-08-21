package model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
    Integer weight;
}