Demonstration of Phase III: https://youtu.be/7XyN5CrUx3s
Phase I: Design a database
To complete this project, XAMPP and Android Studio software were used. In Phase I each team was tasked with designing an entity-relationship diagram for a database. The information is stored as entity and relationship sets with cardinality and participation constraints. Attributes and primary keys are labeled for each set. After the diagram is designed it is translated into relationship tables with primary and foreign key constraints (if used). The following relationships were given and to be accounted for:
•	The database should include details about courses, students, and instructors. 
•	Each course can have several sections. 
•	Students can be undergraduates, MS students, or PhD students. 
•	PhD students can serve as TAs for a course, while MS students and undergraduates (who have scored A- or higher in the course) can be graders. 
•	MS and PhD students may have one or more advisors who are instructors. 
•	Each section should have exactly one instructor and can have none, one, or multiple TAs and graders. 
•	An instructor may teach up to four sections, ranging from none to four. 
•	Each section is assigned a specific time slot in a particular classroom, with the restriction that only one section can occupy a classroom at any given time slot. 
•	No more than four sections can be scheduled concurrently in different classrooms during the same time slot.

Phase II: Implement a web-based database
The first part of Phase II consisted of taking the entity-relationship diagram and the tables made from it’s logic and encoding them into a database in PhpMyAdmin. The database name is ‘DB2’. 
The second part of Phase II involved performing the following specific queries on the database ‘DB2’ using only plain HTML and PHP. 
1. A student can create an account and modify their information later. (The accounts for admin and instructors are created in advance).
2. The admin will be able to create a new course section and appoint an instructor to teach the section. Every course section is scheduled to meet at a specific time slot, with a limit of two sections per time slot. Each instructor teaches one or two sections per semester. Should an instructor be assigned two sections, the two sections must be scheduled in consecutive time slots.
3. A student can browse all the courses offered in the current semester and can register for a specific section of a course if they satisfy the prerequisite conditions and there is available space in the section. (Assume each section is limited to 15 students).
4. A student can view a list of all courses they have taken and are currently taking, along with the total number of credits earned and their cumulative GPA.
5. Instructors have access to records of all course sections they have taught, including names of current semester's enrolled students and the names and grades of students from past semesters.
6. Teaching Assistants (TAs), who are PhD students, will be assigned by the admin to sections with more than 10 students. A PhD student is eligible to be a TA for only one section.
7. Grader positions for sections with 5 to 10 students will be assigned by the admin with either MS students or undergraduate students who have got A- or A in the course. If there is more than one qualified candidate, the admin will choose one as the grader. A student may serve as a grader for only one section.
8. The admin or instructor can appoint one or two instructors as advisor(s) for PhD students, including a start date, and optional end date. The advisor will be able to view the course history of their advisee’s and update their advisee’s information.
9. Student-proposed functionality #1
10. Student-proposed functionality #2

Phase III: Implement an Android app connected to a database
Phase III involved performing the following specific queries on the database ‘DB2’:
1. A student can create an account. 
2. A student can browse all the courses offered in the current semester and can register for a specific section of a course if there is available space in the section. (Assume each section is limited to 15 students). 
3. A student can view a list of all courses they have taken and are currently taking. 
4. Instructors have access to records of all course sections they have taught, including names of current semester's enrolled students and the names and grades of students from past semesters.
5. Student-proposed functionality #1
After starting XAMPP (which is needed to access the database) click on ‘Netstat’ in the app then record the I.P. address, this needs to be adding into the strings.xml file in Android Studio so this line will be updated with the current IP you just recorded:
<string name="url">http://192.168.40.6:80/Phase3/</string>
 Now the android app should be able to connect to the XAMPP software. 
