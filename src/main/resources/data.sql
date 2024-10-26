-- Вставка данных в таблицу events
INSERT INTO events (title_event, start_date_time, end_date_time,
                    museum_name, address, free_seats, total_seats,
                    created_at, updated_at, created_who)
VALUES ('Экскурсия в Михайловский дворец', '2024-11-20 15:00:00', '2024-11-20 17:00:00', 'Михайловский дворец', 'Санкт-Петербург',
        10, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
       ('Экскурсия в Эрмитаж', '2024-11-25 11:00:00', '2024-11-25 12:30:00', 'Эрмитаж', 'Санкт-Петербург', 20, 20,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
       ('Экскурсия в Зимний дворец', '2024-11-03 12:00:00', '2024-11-03 15:00:00', 'Зимний дворец', 'Санкт-Петербург', 20, 20,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
       ('Экскурсия в Музей Александра Башлачёва', '2024-12-15 11:00:00', '2024-12-15 12:30:00',
        'Музей Александра Башлачёва', 'Череповец, Советский пр., 35А', 5, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        'admin');
