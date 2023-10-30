INSERT INTO default_travel_cards (travels_left, expires_at, id, last_passed_turnstile_id, travel_amount, travel_card_type,
                                  validity_period)
VALUES (10, now() + interval '1 month', 3, 2, 'FIVE', 'SCHOOL', 'MONTH');

INSERT INTO loyalty_travel_cards (card_balance, id, last_passed_turnstile_id, travel_card_type)
VALUES (110.4, 4, 2, 'ORDINARY');
