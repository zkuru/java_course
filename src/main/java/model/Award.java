package model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain = true)
public class Award {
    Long id;
    String title;
    String nomination;
}