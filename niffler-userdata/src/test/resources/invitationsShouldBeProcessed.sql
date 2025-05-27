INSERT INTO public."user" (id, username, currency, firstname, surname, photo, photo_small, full_name)
VALUES ('a9165b45-a4aa-47d6-ac50-43611d624421', 'dima', 'RUB', null, null,
        'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD',
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB',
        'Dmitrii Tuchs');
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

-- Create a pending friendship request from dima to viktor (for testing send invitation)
-- No need to add this as it will be created by the test

-- Create a pending friendship request from petr to dima (for testing accept invitation)
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('124cc188-0af2-4366-824a-8051113147ea', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'PENDING');

-- Create a pending friendship request from alex to dima (for testing decline invitation)
INSERT INTO public.friendship (requester_id, addressee_id, created_date, status)
VALUES ('f1b7e6d0-8e7d-4c6b-9e5a-4f3c2d1b0a9f', 'a9165b45-a4aa-47d6-ac50-43611d624421', CURRENT_DATE, 'PENDING');