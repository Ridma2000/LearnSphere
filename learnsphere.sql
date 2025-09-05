-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 05, 2025 at 11:12 AM
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
-- Database: `learnsphere`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(3, 'Business'),
(5, 'Chemistry'),
(2, 'Design'),
(4, 'Physics'),
(1, 'Programming');

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `id` bigint(20) NOT NULL,
  `durationHours` int(11) NOT NULL,
  `instructor` varchar(200) NOT NULL,
  `name` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`id`, `durationHours`, `instructor`, `name`) VALUES
(1, 24, 'Alice Johnson', 'Java Fundamentals'),
(2, 18, 'Bob Lee', 'Creative Marketing'),
(3, 7, 'Binuri Jayawickrama', 'Physics'),
(4, 5, 'Kasun Perera', 'Industrial Automation');

-- --------------------------------------------------------

--
-- Table structure for table `course_category`
--

CREATE TABLE `course_category` (
  `course_id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course_category`
--

INSERT INTO `course_category` (`course_id`, `category_id`) VALUES
(1, 1),
(2, 2),
(2, 3),
(4, 4);

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments` (
  `id` bigint(20) NOT NULL,
  `enrolledAt` datetime(6) NOT NULL,
  `course_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `enrollments`
--

INSERT INTO `enrollments` (`id`, `enrolledAt`, `course_id`, `user_id`) VALUES
(1, '2025-08-23 16:51:30.000000', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id` bigint(20) NOT NULL,
  `createdAt` datetime(6) NOT NULL,
  `rating` int(11) NOT NULL,
  `text` varchar(2000) DEFAULT NULL,
  `course_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id`, `createdAt`, `rating`, `text`, `course_id`, `user_id`) VALUES
(1, '2025-08-23 16:53:12.000000', 5, 'superb subject and teacvhing', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(150) NOT NULL,
  `name` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `name`) VALUES
(1, 'test@learnsphere.io', 'Test User'),
(2, 'ridma@gmail.com', 'Ridma'),
(3, 'ashani@gmail.com', 'Ashani');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_t8o6pivur7nn124jehx7cygw5` (`name`),
  ADD UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_course_name` (`name`);

--
-- Indexes for table `course_category`
--
ALTER TABLE `course_category`
  ADD PRIMARY KEY (`course_id`,`category_id`),
  ADD KEY `FKngybquxul4r8oailq0ud7gykk` (`category_id`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKg1muiskd02x66lpy6fqcj6b9q` (`user_id`,`course_id`),
  ADD KEY `FKho8mcicp4196ebpltdn9wl6co` (`course_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKgvg1ect42p0nkk171cbuwho8o` (`user_id`,`course_id`),
  ADD KEY `FKccbfc9u1qimejr5ll7yuxbtqs` (`course_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `enrollments`
--
ALTER TABLE `enrollments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `course_category`
--
ALTER TABLE `course_category`
  ADD CONSTRAINT `FK6tbd3me7ilucs62uys9rj5xh1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `FKngybquxul4r8oailq0ud7gykk` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `FK3hjx6rcnbmfw368sxigrpfpx0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKho8mcicp4196ebpltdn9wl6co` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `FKccbfc9u1qimejr5ll7yuxbtqs` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
