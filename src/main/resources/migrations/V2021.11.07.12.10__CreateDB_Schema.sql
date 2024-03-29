CREATE TABLE TEACHER_POSITION(
  id SERIAL NOT NULL,
  position_name VARCHAR NOT NULL,
  salary INT NOT NULL,
  CONSTRAINT position_pk PRIMARY KEY (id)
);

CREATE TABLE STUDENT(
  id SERIAL NOT NULL,
  name VARCHAR NOT NULL,
  CONSTRAINT student_pk PRIMARY KEY (id)
);

CREATE TABLE TEACHER(
  id SERIAL NOT NULL,
  name VARCHAR NOT NULL,
  teacher_position INT NOT NULL REFERENCES TEACHER_POSITION(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT teacher_pk PRIMARY KEY (id)
);

CREATE TABLE COURSE(
  id SERIAL NOT NULL,
  course_name VARCHAR NOT NULL,
  main_teacher INT NOT NULL REFERENCES TEACHER(id),
  CONSTRAINT course_pk PRIMARY KEY (id)
);

CREATE TABLE COURSE_TIMETABLE(
  id SERIAL NOT NULL,
  course_id INT NOT NULL REFERENCES COURSE(id) ON UPDATE CASCADE ON DELETE CASCADE,
  lecture_teacher INT NOT NULL REFERENCES TEACHER(id),
  lecture_name VARCHAR NOT NULL,
  room_number VARCHAR NOT NULL,
  CONSTRAINT timetable_pk PRIMARY KEY (id)
);

CREATE TABLE STUDENT_BY_COURSE(
  id SERIAL NOT NULL,
  student_id INT NOT NULL REFERENCES STUDENT(id) ON UPDATE CASCADE ON DELETE CASCADE,
  course_id INT NOT NULL REFERENCES COURSE(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT student_by_course_pk PRIMARY KEY (id)
);

CREATE TABLE VISITS_AND_HOMEWORKS(
  id SERIAL NOT NULL,
  student_by_course_id INT REFERENCES STUDENT_BY_COURSE(id) ON UPDATE CASCADE ON DELETE CASCADE,
  lecture_id INT REFERENCES COURSE_TIMETABLE(id) ON UPDATE CASCADE ON DELETE CASCADE,
  lecture_visited BOOLEAN NOT NULL,
  homework_grade INT,
  CONSTRAINT visits_pk PRIMARY KEY (id)
);