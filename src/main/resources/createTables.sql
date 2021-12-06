--drop table books;

create table books (
    id varchar primary key,
    title varchar,
    author varchar,
    cover_image varchar,
    page_count int
);

insert into books(id, title, author, cover_image, page_count)
values ('433f67b6-46fe-4a09-9cd1-6c0ccd695293', 'The Long Dark Teatime of the Soul', 'Dougle Adams', 'assets/copyright-infringment.jpg', 420);

insert into books(id, title, author, cover_image, page_count)
values ('433f67b6-46fe-4a09-9cd1-6c0ccd695294', E'The Wiseman's Fear', 'FINISH YOUR BOOKS ROTHFUSS', 'assets/copyright-infringment2.jpg', 421);

insert into books(id, title, author, cover_image, page_count)
values ('433f67b6-46fe-4a09-9cd1-6c0ccd695295', E'Very Impressive Sounding Book I am so Well Read', 'Kurt Spelled-Correctly', 'assets/copyright-infringment3.jpg', 422);

