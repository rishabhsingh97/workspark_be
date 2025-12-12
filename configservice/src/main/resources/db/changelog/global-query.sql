CREATE TABLE IF NOT EXISTS config_properties
(
    id               int          not null auto_increment primary key,
    application_name varchar(255) not null,
    profile          varchar(50),
    label            varchar(255),
    property_key     varchar(255) not null,
    property_value   text         not null,
    last_updated     timestamp default current_timestamp on update current_timestamp
);