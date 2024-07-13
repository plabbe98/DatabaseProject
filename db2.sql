-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 23, 2024 at 06:11 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db2`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `email` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`email`, `password`, `type`) VALUES
('admin@uml.edu', '123456', 'admin'),
('Charles_Wilkes@uml.edu', '123456', 'instructor'),
('davidsmith@email.com', '123456', 'student'),
('dbadams@cs.uml.edu', '123456', 'instructor'),
('EMAIL@TEST.COM', '123456', 'student'),
('Johannes_Weis@uml.edu', '123456', 'instructor'),
('johnsmith@email.com', '123456', 'student'),
('labbepaige@gmail.com', 'password', 'student'),
('master@email.com', '123456', 'student'),
('slin@cs.uml.edu', '123456', 'instructor'),
('Yelena_Rykalova@uml.edu', '123456', 'instructor');

-- --------------------------------------------------------

--
-- Table structure for table `advise`
--

CREATE TABLE `advise` (
  `instructor_id` varchar(8) NOT NULL,
  `student_id` varchar(10) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `course_id` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `advise`
--

INSERT INTO `advise` (`instructor_id`, `student_id`, `start_date`, `end_date`, `course_id`) VALUES
('1', '5555555', '2024-03-11', NULL, NULL),
('2', '5555555', '2024-03-11', '2024-03-29', NULL),
('3', '6666666', '2024-03-03', '2024-03-31', NULL),
('5', '6666666', '2024-03-03', '2024-03-31', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `advise_appt`
--

CREATE TABLE `advise_appt` (
  `student_id` int(7) NOT NULL,
  `instructor_id` int(1) NOT NULL,
  `time_id` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `advise_appt`
--

INSERT INTO `advise_appt` (`student_id`, `instructor_id`, `time_id`) VALUES
(123456, 1, 'time4');

-- --------------------------------------------------------

--
-- Table structure for table `advising_time`
--

CREATE TABLE `advising_time` (
  `time_id` varchar(5) DEFAULT NULL,
  `day` text DEFAULT NULL,
  `start_time` time(6) DEFAULT NULL,
  `end_time` time(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `advising_time`
--

INSERT INTO `advising_time` (`time_id`, `day`, `start_time`, `end_time`) VALUES
('time2', 'Tues', '08:00:00.000000', '08:30:00.000000'),
('time3', 'Wed', '08:00:00.000000', '08:30:00.000000'),
('time4', 'Thur', '08:00:00.000000', '08:30:00.000000'),
('time5', 'Fri', '08:00:00.000000', '08:30:00.000000');

-- --------------------------------------------------------

--
-- Table structure for table `advisor`
--

CREATE TABLE `advisor` (
  `instructor_id` int(1) NOT NULL,
  `dept_name` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `advisor`
--

INSERT INTO `advisor` (`instructor_id`, `dept_name`) VALUES
(1, 'Miner School of Computer & Information Sciences');

-- --------------------------------------------------------

--
-- Table structure for table `appointment`
--

CREATE TABLE `appointment` (
  `course_id` varchar(20) NOT NULL,
  `section_id` varchar(20) NOT NULL,
  `semester` varchar(6) NOT NULL,
  `year` decimal(4,0) NOT NULL,
  `student_id` varchar(10) NOT NULL,
  `date_column` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `appointment`
--
DELIMITER $$
CREATE TRIGGER `student_conflict_insert` BEFORE INSERT ON `appointment` FOR EACH ROW BEGIN
    IF EXISTS (
        SELECT *
        FROM appointment AS a
        WHERE a.course_id = NEW.course_id AND
              a.section_id = NEW.section_id AND
              a.semester = NEW.semester AND
              a.year = NEW.year AND
              a.date_column = NEW.date_column AND
              a.student_id <> NEW.student_id AND
              ((NEW.start_time >= a.start_time AND NEW.start_time <= a.end_time) OR
               (NEW.end_time >= a.start_time AND NEW.end_time <= a.end_time) OR
               (a.start_time >= NEW.start_time AND a.start_time <= NEW.end_time) OR
               (a.end_time >= NEW.start_time AND a.end_time <= NEW.end_time))
    )
    THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Student conflict detected. Appointment cannot be scheduled.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `student_conflict_update` BEFORE UPDATE ON `appointment` FOR EACH ROW BEGIN
    IF EXISTS (
        SELECT *
        FROM appointment AS a
        WHERE a.course_id = NEW.course_id AND
              a.section_id = NEW.section_id AND
              a.semester = NEW.semester AND
              a.year = NEW.year AND
              a.date_column = NEW.date_column AND
              a.student_id <> NEW.student_id AND
              ((NEW.start_time >= a.start_time AND NEW.start_time <= a.end_time) OR
               (NEW.end_time >= a.start_time AND NEW.end_time <= a.end_time) OR
               (a.start_time >= NEW.start_time AND a.start_time <= NEW.end_time) OR
               (a.end_time >= NEW.start_time AND a.end_time <= NEW.end_time))
    )
    THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Student conflict detected. Appointment cannot be updated.';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `classroom`
--

CREATE TABLE `classroom` (
  `classroom_id` varchar(8) NOT NULL,
  `building` varchar(15) NOT NULL,
  `room_number` varchar(7) NOT NULL,
  `capacity` decimal(4,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `classroom`
--

INSERT INTO `classroom` (`classroom_id`, `building`, `room_number`, `capacity`) VALUES
('CR1', 'Cumnock', '201', 0),
('CR2', 'Olsen', '402', 5),
('CR3', 'Olney', '305', 1),
('CR4', 'Southwick', '210', 0),
('CR5', 'Shah', '215', 0);

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `course_id` varchar(20) NOT NULL,
  `course_name` varchar(50) NOT NULL,
  `credits` decimal(2,0) DEFAULT NULL CHECK (`credits` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`course_id`, `course_name`, `credits`) VALUES
('COMP1010', 'Computing I', 3),
('COMP1020', 'Computing II', 3),
('COMP2010', 'Computing III', 3),
('COMP2040', 'Computing IV', 3),
('MATH1310', 'Calculus I', 3);

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `dept_name` varchar(100) NOT NULL,
  `location` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`dept_name`, `location`) VALUES
('Miner School of Computer & Information Sciences', 'Dandeneau Hall, 1 University Avenue, Lowell, MA 01854');

-- --------------------------------------------------------

--
-- Table structure for table `instructor`
--

CREATE TABLE `instructor` (
  `instructor_id` varchar(10) NOT NULL,
  `instructor_name` varchar(50) NOT NULL,
  `title` varchar(30) DEFAULT NULL,
  `dept_name` varchar(100) DEFAULT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `instructor`
--

INSERT INTO `instructor` (`instructor_id`, `instructor_name`, `title`, `dept_name`, `email`) VALUES
('1', 'David Adams', 'Teaching Professor', 'Miner School of Computer & Information Sciences', 'dbadams@cs.uml.edu'),
('2', 'Sirong Lin', 'Associate Teaching Professor', 'Miner School of Computer & Information Sciences', 'slin@cs.uml.edu'),
('3', 'Yelena Rykalova', 'Associate Teaching Professor', 'Miner School of Computer & Information Sciences', 'Yelena_Rykalova@uml.edu'),
('4', 'Johannes Weis', 'Assistant Teaching Professor', 'Miner School of Computer & Information Sciences', 'Johannes_Weis@uml.edu'),
('5', 'Tom Wilkes', 'Assistant Teaching Professor', 'Miner School of Computer & Information Sciences', 'Charles_Wilkes@uml.edu');

-- --------------------------------------------------------

--
-- Table structure for table `master`
--

CREATE TABLE `master` (
  `student_id` varchar(10) NOT NULL,
  `total_credits` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mastergrader`
--

CREATE TABLE `mastergrader` (
  `student_id` varchar(10) NOT NULL,
  `course_id` varchar(8) NOT NULL,
  `section_id` varchar(8) NOT NULL,
  `semester` varchar(6) NOT NULL,
  `year` decimal(4,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `office_hours`
--

CREATE TABLE `office_hours` (
  `instructor_id` int(11) DEFAULT NULL,
  `time_id` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `office_hours`
--

INSERT INTO `office_hours` (`instructor_id`, `time_id`) VALUES
(1, 'T10'),
(1, 'T11'),
(1, 'T5'),
(1, 'T7'),
(1, 'T8'),
(2, 'T6');

-- --------------------------------------------------------

--
-- Table structure for table `office_time_slot`
--

CREATE TABLE `office_time_slot` (
  `office_id` varchar(3) NOT NULL,
  `day` varchar(3) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `office_time_slot`
--

INSERT INTO `office_time_slot` (`office_id`, `day`, `start_time`, `end_time`) VALUES
('T1', 'Mon', '09:00:00', '09:30:00'),
('T10', 'Thu', '09:00:00', '09:30:00'),
('T11', 'Thu', '10:00:00', '10:30:00'),
('T12', 'Thu', '11:00:00', '11:30:00'),
('T13', 'Fri', '09:00:00', '09:30:00'),
('T14', 'Fri', '10:00:00', '10:30:00'),
('T15', 'Fri', '11:00:00', '11:30:00'),
('T2', 'Mon', '10:00:00', '10:30:00'),
('T3', 'Mon', '11:00:00', '11:30:00'),
('T4', 'Tue', '09:00:00', '09:30:00'),
('T5', 'Tue', '10:00:00', '10:30:00'),
('T6', 'Tue', '11:00:00', '11:30:00'),
('T7', 'Wed', '09:00:00', '09:30:00'),
('T8', 'Wed', '10:00:00', '10:30:00'),
('T9', 'Wed', '11:00:00', '11:30:00');

-- --------------------------------------------------------

--
-- Table structure for table `phd`
--

CREATE TABLE `phd` (
  `student_id` varchar(10) NOT NULL,
  `qualifier` varchar(30) DEFAULT NULL,
  `proposal_defence_date` date DEFAULT NULL,
  `dissertation_defence_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phd`
--

INSERT INTO `phd` (`student_id`, `qualifier`, `proposal_defence_date`, `dissertation_defence_date`) VALUES
('5555555', NULL, NULL, NULL),
('6666666', NULL, NULL, NULL),
('7777777', NULL, NULL, NULL),
('8888888', NULL, NULL, NULL),
('9999999', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `prereq`
--

CREATE TABLE `prereq` (
  `course_id` varchar(20) NOT NULL,
  `prereq_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prereq`
--

INSERT INTO `prereq` (`course_id`, `prereq_id`) VALUES
('COMP1020', 'COMP1010'),
('COMP2010', 'COMP1020'),
('COMP2040', 'COMP2010');

-- --------------------------------------------------------

--
-- Table structure for table `section`
--

CREATE TABLE `section` (
  `course_id` varchar(20) NOT NULL,
  `section_id` varchar(10) NOT NULL,
  `semester` varchar(6) NOT NULL CHECK (`semester` in ('Fall','Winter','Spring','Summer')),
  `year` decimal(4,0) NOT NULL CHECK (`year` > 1990 and `year` < 2100),
  `instructor_id` varchar(10) DEFAULT NULL,
  `classroom_id` varchar(8) DEFAULT NULL,
  `time_slot_id` varchar(8) DEFAULT NULL,
  `numEnrolled` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `section`
--

INSERT INTO `section` (`course_id`, `section_id`, `semester`, `year`, `instructor_id`, `classroom_id`, `time_slot_id`, `numEnrolled`) VALUES
('COMP1010', 'Section101', 'Fall', 2022, '1', 'CR2', 'TS4', 0),
('COMP1010', 'Section101', 'Fall', 2023, '2', 'CR1', 'TS1', 11),
('COMP1010', 'Section102', 'Fall', 2023, '2', 'CR2', 'TS2', 6),
('COMP1010', 'Section103', 'Fall', 2023, '2', 'CR4', 'TS3', 0),
('COMP1010', 'Section104', 'Fall', 2023, '2', 'CR3', 'TS4', 0),
('COMP1020', 'Section101', 'Fall', 2023, '2', 'CR1', NULL, 1),
('COMP1020', 'Section101', 'Spring', 2024, '1', 'CR1', NULL, 1),
('COMP1020', 'Section102', 'Spring', 2022, '3', 'CR3', 'TS3', 0),
('COMP1020', 'Section102', 'Spring', 2024, '2', 'CR2', NULL, 12),
('COMP2010', 'Section101', 'Fall', 2023, '2', 'CR5', 'TS5', 0),
('COMP2010', 'Section102', 'Fall', 2023, '2', 'CR5', NULL, 0),
('COMP2010', 'Section102', 'Spring', 2024, '2', 'CR5', NULL, 13),
('COMP2040', 'Section201', 'Fall', 2024, '2', 'CR2', NULL, 9),
('COMP2040', 'Section201', 'Spring', 2024, '2', 'CR2', NULL, 11),
('COMP2040', 'Section202', 'Spring', 2024, '2', 'CR2', NULL, 15),
('MATH1310', 'Section101', 'Spring', 2024, '1', 'CR2', 'TS4', 2),
('MATH1310', 'Section102', 'Fall', 2023, '1', 'CR2', 'TS4', 2),
('MATH1310', 'Section102', 'Fall', 2024, '1', 'CR2', 'TS4', 3);

-- --------------------------------------------------------

--
-- Table structure for table `semester_date`
--

CREATE TABLE `semester_date` (
  `semester` varchar(6) NOT NULL CHECK (`semester` in ('Fall','Winter','Spring','Summer')),
  `start_month` decimal(2,0) NOT NULL CHECK (`start_month` >= 1 and `start_month` <= 12),
  `start_day` decimal(2,0) NOT NULL CHECK (`start_day` >= 1 and `start_day` <= 31),
  `end_month` decimal(2,0) NOT NULL CHECK (`end_month` >= 1 and `end_month` <= 12),
  `end_day` decimal(2,0) NOT NULL CHECK (`end_day` >= 1 and `end_day` <= 31)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `semester_date`
--

INSERT INTO `semester_date` (`semester`, `start_month`, `start_day`, `end_month`, `end_day`) VALUES
('Fall', 9, 4, 12, 12),
('Spring', 1, 16, 4, 26);

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `student_id` varchar(10) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `dept_name` varchar(100) DEFAULT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`student_id`, `name`, `email`, `dept_name`, `password`) VALUES
('0', 'John Smith', 'johnsmith@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('1111111', 'Sam Adams', 'samadams@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('123456', 'Paige Labbe', 'labbepaige@gmail.com', 'Miner School of Computer & Information Sciences', 'password'),
('1234567', 'NAME TEST', 'EMAIL@TEST.COM', 'Miner School of Computer & Information Sciences', '123456'),
('2222222', 'Alexander Hamilton', 'alexanderhamilton@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('3333333', 'George Washington', 'georgewashington@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('4444444', 'Abe Lincoln', 'abelincoln@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('5555555', 'Master Student', 'master@email.com', 'Miner School of Computer & Information Sciences', '123456'),
('6666666', 'John Smith', 'johnsmith@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('7777777', 'David Smith', 'davidsmith@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('8888888', 'Bob Riely', 'bobriely@email.com', 'Miner School of Computer & Information Sciences', 'password123'),
('9999999', 'Kim Dep', 'kimdep@email.com', 'Miner School of Computer & Information Sciences', 'password123');

-- --------------------------------------------------------

--
-- Table structure for table `ta`
--

CREATE TABLE `ta` (
  `student_id` varchar(10) NOT NULL,
  `course_id` varchar(8) NOT NULL,
  `section_id` varchar(8) NOT NULL,
  `semester` varchar(6) NOT NULL,
  `year` decimal(4,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `take`
--

CREATE TABLE `take` (
  `student_id` varchar(10) NOT NULL,
  `course_id` varchar(8) NOT NULL,
  `section_id` varchar(10) NOT NULL,
  `semester` varchar(6) NOT NULL,
  `year` decimal(4,0) NOT NULL,
  `grade` varchar(2) DEFAULT NULL CHECK (`grade` in ('A+','A','A-','B+','B','B-','C+','C','C-','D+','D','D-','F'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `take`
--

INSERT INTO `take` (`student_id`, `course_id`, `section_id`, `semester`, `year`, `grade`) VALUES
('1111111', 'COMP1010', 'Section104', 'Fall', 2023, 'B-'),
('123456', 'COMP1010', 'Section103', 'Fall', 2023, 'A-'),
('123456', 'COMP1020', 'Section101', 'Fall', 2023, 'A'),
('123456', 'COMP2010', 'Section102', 'Spring', 2024, 'B+'),
('123456', 'COMP2040', 'Section201', 'Fall', 2024, NULL),
('123456', 'MATH1310', 'Section101', 'Spring', 2024, 'A-'),
('2222222', 'COMP1010', 'Section104', 'Fall', 2023, 'B-'),
('2222222', 'COMP2010', 'Section102', 'Fall', 2023, 'A+'),
('3333333', 'COMP1010', 'Section104', 'Fall', 2023, 'B'),
('3333333', 'COMP2010', 'Section101', 'Fall', 2023, 'B+'),
('4444444', 'COMP1010', 'Section104', 'Fall', 2023, 'B'),
('5555555', 'COMP1010', 'Section104', 'Fall', 2023, 'B-');

-- --------------------------------------------------------

--
-- Table structure for table `time_slot`
--

CREATE TABLE `time_slot` (
  `time_slot_id` varchar(8) NOT NULL,
  `day` varchar(10) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `time_slot`
--

INSERT INTO `time_slot` (`time_slot_id`, `day`, `start_time`, `end_time`) VALUES
('TS1', 'MoWeFr', '11:00:00', '11:50:00'),
('TS2', 'MoWeFr', '12:00:00', '12:50:00'),
('TS3', 'MoWeFr', '13:00:00', '13:50:00'),
('TS4', 'TuTh', '11:00:00', '12:15:00'),
('TS5', 'TuTh', '12:30:00', '13:45:00');

-- --------------------------------------------------------

--
-- Table structure for table `undergraduate`
--

CREATE TABLE `undergraduate` (
  `student_id` varchar(10) NOT NULL,
  `total_credits` int(11) DEFAULT NULL,
  `class_standing` varchar(10) DEFAULT NULL CHECK (`class_standing` in ('Freshman','Sophomore','Junior','Senior'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `undergraduate`
--

INSERT INTO `undergraduate` (`student_id`, `total_credits`, `class_standing`) VALUES
('0', 6, 'Sophomore'),
('1111111', 6, 'Sophomore'),
('123456', NULL, NULL),
('1234567', NULL, NULL),
('2222222', NULL, 'Freshman'),
('3333333', NULL, 'Freshman'),
('4444444', NULL, 'Freshman');

-- --------------------------------------------------------

--
-- Table structure for table `undergraduategrader`
--

CREATE TABLE `undergraduategrader` (
  `student_id` varchar(10) NOT NULL,
  `course_id` varchar(8) NOT NULL,
  `section_id` varchar(8) NOT NULL,
  `semester` varchar(6) NOT NULL,
  `year` decimal(4,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`email`);

--
-- Indexes for table `advise`
--
ALTER TABLE `advise`
  ADD PRIMARY KEY (`instructor_id`,`student_id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `advise_appt`
--
ALTER TABLE `advise_appt`
  ADD PRIMARY KEY (`student_id`,`instructor_id`);

--
-- Indexes for table `advisor`
--
ALTER TABLE `advisor`
  ADD PRIMARY KEY (`instructor_id`,`dept_name`);

--
-- Indexes for table `appointment`
--
ALTER TABLE `appointment`
  ADD PRIMARY KEY (`course_id`,`section_id`,`semester`,`year`,`date_column`,`student_id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `classroom`
--
ALTER TABLE `classroom`
  ADD PRIMARY KEY (`classroom_id`);

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`course_id`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`dept_name`);

--
-- Indexes for table `instructor`
--
ALTER TABLE `instructor`
  ADD PRIMARY KEY (`instructor_id`);

--
-- Indexes for table `master`
--
ALTER TABLE `master`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `mastergrader`
--
ALTER TABLE `mastergrader`
  ADD PRIMARY KEY (`student_id`,`course_id`,`section_id`,`semester`,`year`),
  ADD KEY `course_id` (`course_id`,`section_id`,`semester`,`year`);

--
-- Indexes for table `office_hours`
--
ALTER TABLE `office_hours`
  ADD UNIQUE KEY `unique_pair` (`instructor_id`,`time_id`);

--
-- Indexes for table `office_time_slot`
--
ALTER TABLE `office_time_slot`
  ADD UNIQUE KEY `office_id` (`office_id`);

--
-- Indexes for table `phd`
--
ALTER TABLE `phd`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `prereq`
--
ALTER TABLE `prereq`
  ADD PRIMARY KEY (`course_id`,`prereq_id`),
  ADD KEY `prereq_id` (`prereq_id`);

--
-- Indexes for table `section`
--
ALTER TABLE `section`
  ADD PRIMARY KEY (`course_id`,`section_id`,`semester`,`year`),
  ADD KEY `instructor_id` (`instructor_id`),
  ADD KEY `time_slot_id` (`time_slot_id`);

--
-- Indexes for table `semester_date`
--
ALTER TABLE `semester_date`
  ADD PRIMARY KEY (`semester`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`student_id`),
  ADD KEY `dept_name` (`dept_name`);

--
-- Indexes for table `ta`
--
ALTER TABLE `ta`
  ADD PRIMARY KEY (`student_id`,`course_id`,`section_id`,`semester`,`year`),
  ADD KEY `course_id` (`course_id`,`section_id`,`semester`,`year`);

--
-- Indexes for table `take`
--
ALTER TABLE `take`
  ADD PRIMARY KEY (`student_id`,`course_id`,`section_id`,`semester`,`year`),
  ADD KEY `course_id` (`course_id`,`section_id`,`semester`,`year`);

--
-- Indexes for table `time_slot`
--
ALTER TABLE `time_slot`
  ADD PRIMARY KEY (`time_slot_id`);

--
-- Indexes for table `undergraduate`
--
ALTER TABLE `undergraduate`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `undergraduategrader`
--
ALTER TABLE `undergraduategrader`
  ADD PRIMARY KEY (`student_id`,`course_id`,`section_id`,`semester`,`year`),
  ADD KEY `course_id` (`course_id`,`section_id`,`semester`,`year`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `advise`
--
ALTER TABLE `advise`
  ADD CONSTRAINT `advise_ibfk_1` FOREIGN KEY (`instructor_id`) REFERENCES `instructor` (`instructor_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `advise_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `phd` (`student_id`) ON DELETE CASCADE;

--
-- Constraints for table `appointment`
--
ALTER TABLE `appointment`
  ADD CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`course_id`,`section_id`,`semester`,`year`) REFERENCES `section` (`course_id`, `section_id`, `semester`, `year`) ON DELETE CASCADE,
  ADD CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE;

--
-- Constraints for table `master`
--
ALTER TABLE `master`
  ADD CONSTRAINT `master_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE;

--
-- Constraints for table `mastergrader`
--
ALTER TABLE `mastergrader`
  ADD CONSTRAINT `mastergrader_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `master` (`student_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `mastergrader_ibfk_2` FOREIGN KEY (`course_id`,`section_id`,`semester`,`year`) REFERENCES `section` (`course_id`, `section_id`, `semester`, `year`) ON DELETE CASCADE;

--
-- Constraints for table `phd`
--
ALTER TABLE `phd`
  ADD CONSTRAINT `phd_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE;

--
-- Constraints for table `prereq`
--
ALTER TABLE `prereq`
  ADD CONSTRAINT `prereq_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `prereq_ibfk_2` FOREIGN KEY (`prereq_id`) REFERENCES `course` (`course_id`);

--
-- Constraints for table `section`
--
ALTER TABLE `section`
  ADD CONSTRAINT `section_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `section_ibfk_2` FOREIGN KEY (`instructor_id`) REFERENCES `instructor` (`instructor_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `section_ibfk_3` FOREIGN KEY (`time_slot_id`) REFERENCES `time_slot` (`time_slot_id`) ON DELETE SET NULL;

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`dept_name`) REFERENCES `department` (`dept_name`) ON DELETE SET NULL;

--
-- Constraints for table `ta`
--
ALTER TABLE `ta`
  ADD CONSTRAINT `ta_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `phd` (`student_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ta_ibfk_2` FOREIGN KEY (`course_id`,`section_id`,`semester`,`year`) REFERENCES `section` (`course_id`, `section_id`, `semester`, `year`) ON DELETE CASCADE;

--
-- Constraints for table `take`
--
ALTER TABLE `take`
  ADD CONSTRAINT `take_ibfk_1` FOREIGN KEY (`course_id`,`section_id`,`semester`,`year`) REFERENCES `section` (`course_id`, `section_id`, `semester`, `year`) ON DELETE CASCADE,
  ADD CONSTRAINT `take_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE;

--
-- Constraints for table `undergraduate`
--
ALTER TABLE `undergraduate`
  ADD CONSTRAINT `undergraduate_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE;

--
-- Constraints for table `undergraduategrader`
--
ALTER TABLE `undergraduategrader`
  ADD CONSTRAINT `undergraduategrader_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `undergraduate` (`student_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `undergraduategrader_ibfk_2` FOREIGN KEY (`course_id`,`section_id`,`semester`,`year`) REFERENCES `section` (`course_id`, `section_id`, `semester`, `year`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
