DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id) VALUES
  ('2019-06-22 08:00:00', 'dinner1', 1000, 100000),
  ('2019-06-22 13:00:00', 'dinner2', 510, 100000),
  ('2019-06-22 17:00:00', 'dinner3', 500, 100000),
  ('2019-06-23 08:00:00', 'breakfast1', 1000, 100001),
  ('2019-06-23 13:00:00', 'breakfast2', 500, 100001),
  ('2019-06-23 17:00:00', 'breakfast3', 500, 100001);
