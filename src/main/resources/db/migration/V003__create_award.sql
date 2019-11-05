create table award (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  title VARCHAR(100),
  nomination VARCHAR(100),
  dog_id BIGINT(20),
  constraint FK_DOG_ID_AWARD_ID foreign key (dog_id) references dog (id),
  primary key (id)
);