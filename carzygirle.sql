-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 09, 2021 at 02:05 AM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `carzygirle`
--
CREATE DATABASE IF NOT EXISTS `carzygirle` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `carzygirle`;

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `id` int(11) NOT NULL,
  `name` varchar(700) NOT NULL,
  `balance` varchar(700) NOT NULL,
  `owner` varchar(700) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`id`, `name`, `balance`, `owner`) VALUES
(1, 'الرئيسي', '2000', 'المحل');

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE `clients` (
  `id` int(11) NOT NULL,
  `name` varchar(700) NOT NULL,
  `tele1` varchar(700) DEFAULT NULL,
  `tele2` varchar(700) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`id`, `name`, `tele1`, `tele2`) VALUES
(1, 'احمد', 'aaa', ''),
(2, 'aaaaa', '', 'scsc');

-- --------------------------------------------------------

--
-- Table structure for table `clients_accounts`
--

CREATE TABLE `clients_accounts` (
  `id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `acc_id` int(11) DEFAULT NULL,
  `amount` varchar(700) NOT NULL,
  `date` date NOT NULL,
  `type` varchar(700) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `expenses`
--

CREATE TABLE `expenses` (
  `id` int(11) NOT NULL,
  `acc_id` int(11) NOT NULL,
  `amount` varchar(700) NOT NULL,
  `date` date NOT NULL,
  `details` varchar(700) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_buy`
--

CREATE TABLE `invoice_buy` (
  `id` int(11) NOT NULL,
  `provider_id` int(11) NOT NULL,
  `acc_id` int(11) DEFAULT NULL,
  `date` date NOT NULL,
  `cost` varchar(700) NOT NULL,
  `discount` varchar(700) DEFAULT NULL,
  `discount_percent` varchar(700) DEFAULT NULL,
  `total` varchar(700) NOT NULL,
  `note` varchar(700) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `invoice_buy`
--

INSERT INTO `invoice_buy` (`id`, `provider_id`, `acc_id`, `date`, `cost`, `discount`, `discount_percent`, `total`, `note`) VALUES
(1, 1, NULL, '2021-08-09', '18000.0', '0', '0', '18000.0', 'لايوجد');

-- --------------------------------------------------------

--
-- Table structure for table `invoice_buy_details`
--

CREATE TABLE `invoice_buy_details` (
  `id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `cost` varchar(700) NOT NULL,
  `amount` varchar(700) NOT NULL,
  `total_cost` varchar(700) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `invoice_buy_details`
--

INSERT INTO `invoice_buy_details` (`id`, `invoice_id`, `product_id`, `cost`, `amount`, `total_cost`) VALUES
(2, 1, 1, '150', '120', '18000');

-- --------------------------------------------------------

--
-- Table structure for table `invoice_sell`
--

CREATE TABLE `invoice_sell` (
  `id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `acc_id` int(11) DEFAULT NULL,
  `date` date NOT NULL,
  `cost` varchar(700) NOT NULL,
  `discount` varchar(700) DEFAULT NULL,
  `discount_percent` varchar(700) DEFAULT NULL,
  `total` varchar(700) NOT NULL,
  `note` varchar(700) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_sell_details`
--

CREATE TABLE `invoice_sell_details` (
  `id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `cost` varchar(700) NOT NULL,
  `amount` varchar(700) NOT NULL,
  `total_cost` varchar(700) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `priviliges_name`
--

CREATE TABLE `priviliges_name` (
  `name` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `priviliges_name`
--

INSERT INTO `priviliges_name` (`name`) VALUES
('Accounts'),
('AccountsScreenAccounts'),
('AccountsScreenClients'),
('AccountsScreenProviders'),
('ClientScreen'),
('invoices'),
('products'),
('provider'),
('StoreScreenTransactionsEntrance');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(700) NOT NULL,
  `barcode` varchar(700) DEFAULT NULL,
  `amount` varchar(700) DEFAULT NULL,
  `buy_cost` varchar(700) NOT NULL,
  `min_cost` varchar(700) DEFAULT NULL,
  `cost` varchar(700) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `barcode`, `amount`, `buy_cost`, `min_cost`, `cost`) VALUES
(1, 'asasas', '56293357255301', '5000', '100', '120', '150'),
(2, 'aaa', '50408478211258', 'aa', '', 'aa', 'aa');

-- --------------------------------------------------------

--
-- Table structure for table `providers`
--

CREATE TABLE `providers` (
  `id` int(11) NOT NULL,
  `name` varchar(700) NOT NULL,
  `location` varchar(700) DEFAULT NULL,
  `acc_num` varchar(700) DEFAULT NULL,
  `tele1` varchar(700) DEFAULT NULL,
  `tele2` varchar(700) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `providers`
--

INSERT INTO `providers` (`id`, `name`, `location`, `acc_num`, `tele1`, `tele2`) VALUES
(1, 'ض', '', '', '', ''),
(2, 'ضس', 'سضس', 'س', '', ''),
(3, 'as', 'asas', '', 's', ''),
(4, 'cs', 'cscsc', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `providers_accounts`
--

CREATE TABLE `providers_accounts` (
  `id` int(11) NOT NULL,
  `provider_id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `acc_id` int(11) DEFAULT NULL,
  `amount` varchar(700) NOT NULL,
  `date` date NOT NULL,
  `type` varchar(700) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `static_values`
--

CREATE TABLE `static_values` (
  `attribute` varchar(250) NOT NULL,
  `value` varchar(400) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `static_values`
--

INSERT INTO `static_values` (`attribute`, `value`) VALUES
('MAIN_ACCOUNT_ID', '1');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `user_name` varchar(700) NOT NULL,
  `password` varchar(700) NOT NULL,
  `role` varchar(700) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `user_name`, `password`, `role`) VALUES
(1, 'a', 'a', 'super_admin'),
(2, 's', 'a', 'user');

-- --------------------------------------------------------

--
-- Table structure for table `users_permissions`
--

CREATE TABLE `users_permissions` (
  `user_id` int(11) NOT NULL,
  `privileges` varchar(700) NOT NULL,
  `value` varchar(700) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `clients_accounts`
--
ALTER TABLE `clients_accounts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `client_id` (`client_id`),
  ADD KEY `invoice_id` (`invoice_id`),
  ADD KEY `acc_id` (`acc_id`);

--
-- Indexes for table `expenses`
--
ALTER TABLE `expenses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `acc_id` (`acc_id`);

--
-- Indexes for table `invoice_buy`
--
ALTER TABLE `invoice_buy`
  ADD PRIMARY KEY (`id`),
  ADD KEY `acc_id` (`acc_id`);

--
-- Indexes for table `invoice_buy_details`
--
ALTER TABLE `invoice_buy_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `invoice_id` (`invoice_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `invoice_sell`
--
ALTER TABLE `invoice_sell`
  ADD PRIMARY KEY (`id`),
  ADD KEY `acc_id` (`acc_id`);

--
-- Indexes for table `invoice_sell_details`
--
ALTER TABLE `invoice_sell_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `invoice_id` (`invoice_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `priviliges_name`
--
ALTER TABLE `priviliges_name`
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `providers`
--
ALTER TABLE `providers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `providers_accounts`
--
ALTER TABLE `providers_accounts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `provider_id` (`provider_id`),
  ADD KEY `invoice_id` (`invoice_id`),
  ADD KEY `acc_id` (`acc_id`);

--
-- Indexes for table `static_values`
--
ALTER TABLE `static_values`
  ADD UNIQUE KEY `attribute` (`attribute`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_permissions`
--
ALTER TABLE `users_permissions`
  ADD KEY `user_id` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `clients`
--
ALTER TABLE `clients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `clients_accounts`
--
ALTER TABLE `clients_accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `expenses`
--
ALTER TABLE `expenses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice_buy`
--
ALTER TABLE `invoice_buy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `invoice_buy_details`
--
ALTER TABLE `invoice_buy_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `invoice_sell`
--
ALTER TABLE `invoice_sell`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice_sell_details`
--
ALTER TABLE `invoice_sell_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `providers`
--
ALTER TABLE `providers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `providers_accounts`
--
ALTER TABLE `providers_accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `clients_accounts`
--
ALTER TABLE `clients_accounts`
  ADD CONSTRAINT `clients_accounts_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`),
  ADD CONSTRAINT `clients_accounts_ibfk_2` FOREIGN KEY (`invoice_id`) REFERENCES `invoice_sell` (`id`),
  ADD CONSTRAINT `clients_accounts_ibfk_3` FOREIGN KEY (`acc_id`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `expenses`
--
ALTER TABLE `expenses`
  ADD CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`acc_id`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `invoice_buy`
--
ALTER TABLE `invoice_buy`
  ADD CONSTRAINT `invoice_buy_ibfk_1` FOREIGN KEY (`acc_id`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `invoice_buy_details`
--
ALTER TABLE `invoice_buy_details`
  ADD CONSTRAINT `invoice_buy_details_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice_buy` (`id`),
  ADD CONSTRAINT `invoice_buy_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `invoice_sell`
--
ALTER TABLE `invoice_sell`
  ADD CONSTRAINT `invoice_sell_ibfk_1` FOREIGN KEY (`acc_id`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `invoice_sell_details`
--
ALTER TABLE `invoice_sell_details`
  ADD CONSTRAINT `invoice_sell_details_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice_sell` (`id`),
  ADD CONSTRAINT `invoice_sell_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `providers_accounts`
--
ALTER TABLE `providers_accounts`
  ADD CONSTRAINT `providers_accounts_ibfk_1` FOREIGN KEY (`provider_id`) REFERENCES `providers` (`id`),
  ADD CONSTRAINT `providers_accounts_ibfk_2` FOREIGN KEY (`invoice_id`) REFERENCES `invoice_buy` (`id`),
  ADD CONSTRAINT `providers_accounts_ibfk_3` FOREIGN KEY (`acc_id`) REFERENCES `accounts` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
