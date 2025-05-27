INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('a9165b45-a4aa-47d6-ac50-43611d624421', 'dima', 'RUB', null, null,
        'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Dmitrii Tuchs');

-- Create 11 users to test pagination
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('29427095-c78f-49d4-96ab-0fcb63d960c2', 'viktor', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Viktor Gansales');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('124cc188-0af2-4366-824a-8051113147ea', 'petr', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Petr Janga');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('f1b7e6d0-8e7d-4c6b-9e5a-4f3c2d1b0a9f', 'alex', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Alex Smith');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('2a3e2f1d-0c9b-4a7e-6d5f-4e3d2c1b0a9f', 'john', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'John Doe');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', 'jane', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Jane Smith');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('4c5d6e7f-8a9b-0c1d-2e3f-4a5b6c7d8e9f', 'bob', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Bob Johnson');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('5d6e7f8a-9b0c-1d2e-3f4a-5b6c7d8e9f0a', 'alice', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Alice Brown');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1b', 'charlie', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Charlie Wilson');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('7f8a9b0c-1d2e-3f4a-5b6c-7d8e9f0a1b2c', 'david', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'David Miller');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('8a9b0c1d-2e3f-4a5b-6c7d-8e9f0a1b2c3d', 'emma', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Emma Davis');
INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('9b0c1d2e-3f4a-5b6c-7d8e-9f0a1b2c3d4e', 'frank', 'RUB', null, null,
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Frank Wilson');

-- Create friendships (all users are friends with dima)
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('29427095-c78f-49d4-96ab-0fcb63d960c2', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('124cc188-0af2-4366-824a-8051113147ea', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('f1b7e6d0-8e7d-4c6b-9e5a-4f3c2d1b0a9f', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('2a3e2f1d-0c9b-4a7e-6d5f-4e3d2c1b0a9f', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('3b4c5d6e-7f8a-9b0c-1d2e-3f4a5b6c7d8e', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('4c5d6e7f-8a9b-0c1d-2e3f-4a5b6c7d8e9f', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('5d6e7f8a-9b0c-1d2e-3f4a-5b6c7d8e9f0a', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('6e7f8a9b-0c1d-2e3f-4a5b-6c7d8e9f0a1b', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('7f8a9b0c-1d2e-3f4a-5b6c-7d8e9f0a1b2c', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('8a9b0c1d-2e3f-4a5b-6c7d-8e9f0a1b2c3d', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('9b0c1d2e-3f4a-5b6c-7d8e-9f0a1b2c3d4e', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'ACCEPTED');
