-- noinspection SqlNoDataSourceInspection,SqlDialectInspection

CREATE TABLE `rollingstone_users` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                         `user_id` varchar(255) NOT NULL,
                                         `first_name` varchar(255) NOT NULL,
                                         `last_name` varchar(255) NOT NULL,
                                         `user_name` varchar(255) NOT NULL,
                                         `email_id` varchar(255) NOT NULL,
                                         `encrypted_password` varchar(255) NOT NULL,
                                         `tenant_id` varchar(255) NOT NULL,
                                         `preferred_username` varchar(255) NOT NULL,
                                         `email_verification_status` varchar(3) NOT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;