DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_line_items;

CREATE TABLE orders
(
    id SERIAL AUTO_INCREMENT,
    orderNumber text
);

CREATE TABLE order_line_items
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    sensor_id  INTEGER                           NOT NULL,
    value      DOUBLE PRECISION                  NOT NULL,
    raining    BOOLEAN                           NOT NULL,
    registered timestamp           DEFAULT now() NOT NULL,
    FOREIGN KEY (sensor_id) REFERENCES sensor (id) ON UPDATE CASCADE ON DELETE CASCADE
);
