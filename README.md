## Задание №9

Хранимые процедуры

Для выполнения были созданы 2 процедуры get_student и update_student в MYSQL.

Процедура get_student как параметр принимает id студент и возвращает запись о студенте по указанному id.
Процедура:

DELIMITER $$
create procedure get_student(IN student_id INT)
BEGIN
	SELECT student.id as "Код", student.first_name as "Имя", student.last_name as "Фамилия",
            student.middle_name as "Очество", student.study_group as "Учебная группа",
            studentinfo.phone as "Телефон", studentinfo.country as "Страна",
            studentinfo.city as "Город", studentinfo.address as "Адрес",
            studentinfo.birthday as "Дата рождения", studentinfo.gender as "Пол",
            AVG(studentscore.computer_network) as "Компьютерные сети",
            AVG(studentscore.numeric_methods) as "Числовые методы",
            AVG(studentscore.mathematics_methods) as "Математические методы" FROM students.student 
            INNER JOIN students.studentInfo ON student.id = studentInfo.student_id 
            INNER JOIN students.studentScore ON student.id = studentScore.student_id
            WHERE student.id = student_id
            GROUP BY student.id;
END$$
DELIMITER ;

Процедура update как параметр принимает id студент, имя, фамилия, отчество, телефон, учебная группа, телефон, страна,
 город и адрес. Возвращаемого значения нету, так как выполняется операции обновления. 
 Обновляются таблицы student, studentinfo.
Процедура:

DELIMITER $$
create procedure update_student(IN studentId INT, IN student_first_name VARCHAR(100), IN student_last_name VARCHAR(100), IN student_middle_name VARCHAR(100), 
IN student_study_group VARCHAR(100), IN student_phone VARCHAR(100), IN student_country VARCHAR(100), 
IN student_city VARCHAR(100), IN student_address VARCHAR(100), IN student_gender boolean)
BEGIN
	UPDATE students.student SET first_name = student_first_name,
            last_name = student_last_name, middle_name = student_middle_name,
            study_group = student_study_group WHERE id = studentId;
	UPDATE students.studentinfo SET phone = student_phone,
            country = student_country, city = student_city, address = student_address,
            gender = student_gender
            WHERE student_id = studentId;
END$$
DELIMITER ;

Внутри кода приложения, был создан новый класс StudentMapper для конвертации сырых данных в объект класса Student.
Так же был изменен класс StudentRepository, так как теперь есть процедуры. 
Поэтому был изменены методы "getStudent" и "update", теперь внутри этих методов вызываем процедуры через переменную jdbcCall, 
которые принадлежит классу SimpleJdbcCall, позволяющий вызывать процедуры. 
Для этого при создании вызыва надо указывать наименнование вызываемой процедуры.
Так в "getStudent" указываем процедуру "get_student" виде константы GET_STUDENT_BY_ID_PROCEDURE_NAME.
А в "update" указываем процедуру "update_student" виде константы UPDATE_STUDENT_PROCEDURE_NAME.
Так же убираем аннотацию "@Transactional", так как все процедуры на уровне базы данных выполняютсь в режиме транзакций.
Так же были изменены наименнования параметров внутри методов для соответствия параметрам процедур.
Станицы на основе которых, можно оценить изменения, URL: "localhost:8080/update", 
и выбрать студента для обновления его данных, так как теперь получения одно студента и обновления его данных теперь выполняется через вызовы процедур.

## Задание №10

Настройка DataGrid в Windows Forms

В описание задания нужно использовать DataGridColumnStyle для стелизации таблицы приложения.

Такой функционал у нас имеется, и выполняется он через css стили.