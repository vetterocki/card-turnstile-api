INSERT INTO travel_card_reports (id, travel_card_id, turnstile_id)
VALUES (1, 3, 2),
       (2, 4, 2);

INSERT INTO report_interactions (report_id, description, interaction_type)
VALUES (1, 'Error', 'DENIED'),
       (1, '', 'SUCCESSFUL_ACCESS'),
       (2, '', 'SUCCESSFUL_ACCESS'),
       (2, '', 'SUCCESSFUL_ACCESS');