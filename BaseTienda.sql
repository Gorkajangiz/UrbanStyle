-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: tienda
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `id_client` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `mail` varchar(40) DEFAULT NULL,
  `password` varchar(30) NOT NULL,
  `discount` int DEFAULT '10',
  `purchases` int DEFAULT '0',
  PRIMARY KEY (`id_client`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'admin','admin','&firns678',100,0),(2,'Gorka','gorka@gmail.com','&678',5,15),(3,'Edrian','edrian@gmail.com','&678',25,10),(4,'Andoni','andoni@gmail.com','&678',15,5),(5,'Victor','victor@gmail.com','&678',36,0),(6,'Oscar','oscar@gmail.com','&678',15,5),(7,'Julen','julen@gmail.com','&678',5,15);
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id_product` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` decimal(6,2) NOT NULL,
  `stock` int DEFAULT NULL,
  `category` varchar(40) NOT NULL,
  `color` varchar(20) NOT NULL,
  `talla` varchar(40) DEFAULT NULL,
  `url` varchar(300) NOT NULL,
  `rating` int DEFAULT '0',
  `description` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id_product`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Camiseta Essential',14.99,143,'Camisetas','Negro','S,M,L,XL','c1f.jpg,c1b.jpg,c1m.jpg,c1mb.jpg',4,'Street'),(2,'Pantalón Urban Classic',29.50,174,'Pantalones','Gris','M,L,XL,XXL','p2f.jpg,p2b.jpg,p2m.jpg,p2mb.jpg',5,'Urban'),(3,'Gorra Essential',14.99,145,'Gorras','Negro','Única','g1f.jpg,g1b.jpg,g1m.jpg,g1mb.jpg',4,'Street'),(4,'Gorra Urban Classic',19.50,0,'Gorras','Gris','Única','g2f.jpg,g2b.jpg,g2m.jpg,g2mb.jpg',5,'Urban'),(5,'Camiseta Urban Classic',19.50,7,'Camisetas','Gris','M,L,XL,XXL','c2f.jpg,c2b.jpg,c2m.jpg,c2mb.jpg',3,'Urban'),(6,'Sudadera Essential',29.99,3,'Sudaderas','Negro','S,M,L,XL','s1f.jpg,s1b.jpg,s1m.jpg,s1mb.jpg',4,'Street'),(7,'Sudadera Urban Classic',34.50,178,'Sudaderas','Gris','M,L,XL,XXL','s2f.jpg,s2b.jpg,s2m.jpg,s2mb.jpg',5,'Urban'),(8,'Camiseta Premium Fit',19.99,110,'Camisetas','Blanco','XS,S,M','c3f.jpg,c3b.jpg,c3m.jpg,c3mb.jpg',3,'Minimal'),(9,'Gorra Premium Fit',19.99,112,'Gorras','Blanco','Única','g3f.jpg,g3b.jpg,g3m.jpg,g3mb.jpg',3,'Minimal'),(10,'Pantalón Premium Fit',29.99,112,'Pantalones','Beige','XS,S,M','p3f.jpg,p3b.jpg,p3m.jpg,p3mb.jpg',4,'Minimal'),(11,'Sudadera Premium Fit',34.99,111,'Sudaderas','Blanco','XS,S,M','s3f.jpg,s3b.jpg,s3m.jpg,s3mb.jpg',5,'Minimal'),(12,'Camiseta Street Style',12.95,189,'Camisetas','Azul Marino','L,XL','c4f.jpg,c4b.jpg,c4m.jpg,c4mb.jpg',3,'Street'),(13,'Pantalón Street Style',22.95,189,'Pantalones','Azul Marino','L,XL','p4f.jpg,p4b.jpg,p4m.jpg,p4mb.jpg',5,'Street'),(14,'Gorra Street Style',12.95,188,'Gorras','Azul Marino','Única','g4f.jpg,g4b.jpg,g4m.jpg,g4mb.jpg',3,'Street'),(15,'Sudadera Street Style',27.95,189,'Sudaderas','Azul Marino','L,XL','s4f.jpg,s4b.jpg,s4m.jpg,s4mb.jpg',3,'Street'),(16,'Camiseta Comfort',17.75,156,'Camisetas','Verde Oliva','S,M,L,XL,XXL','c5f.jpg,c5b.jpg,c5m.jpg,c5mb.jpg',4,'Urban'),(17,'Gorra Comfort',17.75,156,'Gorras','Verde Oliva','Única','g5f.jpg,g5b.jpg,g5m.jpg,g5mb.jpg',4,'Urban'),(18,'Pantalón Comfort',27.75,156,'Pantalones','Verde Oliva','S,M,L,XL,XXL','p5f.jpg,p5b.jpg,p5m.jpg,p5mb.jpg',5,'Urban'),(19,'Sudadera Comfort',32.75,156,'Sudaderas','Verde Oliva','S,M,L,XL,XXL','s5f.jpg,s5b.jpg,s5m.jpg,s5mb.jpg',4,'Urban'),(20,'Camiseta Limited Edition',18.99,8,'Camisetas','Rojo Bordeaux','M,L','c6f.jpg,c6b.jpg,c6m.jpg,c6mb.jpg',5,'Minimal'),(21,'Gorra Limited Edition',18.99,8,'Gorras','Rojo Bordeaux','Única','g6f.jpg,g6b.jpg,g6m.jpg,g6mb.jpg',5,'Minimal'),(22,'Pantalón Limited Edition',28.99,8,'Pantalones','Rojo Bordeaux','M,L','p6f.jpg,p6b.jpg,p6m.jpg,p6mb.jpg',5,'Minimal'),(23,'Sudadera Limited Edition',37.99,6,'Sudaderas','Rojo Bordeaux','M,L','s6f.jpg,s6b.jpg,s6m.jpg,s6mb.jpg',4,'Minimal'),(24,'Camiseta Vintage',10.50,6,'Camisetas','Beige','S,M','c7f.jpg,c7b.jpg,c7m.jpg,c7mb.jpg',3,'Street'),(25,'Gorra Vintage',10.50,4,'Gorras','Beige','Única','g7f.jpg,g7b.jpg,g7m.jpg,g7mb.jpg',3,'Street'),(26,'Pantalón Vintage',20.50,0,'Pantalones','Beige','S,M','p7f.jpg,p7b.jpg,p7m.jpg,p7mb.jpg',5,'Street'),(27,'Sudadera Vintage',25.50,0,'Sudaderas','Beige','S,M','s7f.jpg,s7b.jpg,s7m.jpg,s7mb.jpg',5,'Street'),(28,'Camiseta Oversized',18.25,9,'Camisetas','Negro Carbón','XL,XXL','c8f.jpg,c8b.jpg,c8m.jpg,c8mb.jpg',4,'Urban'),(29,'Gorra Oversized',18.25,9,'Gorras','Negro Carbón','Única','g8f.jpg,g8b.jpg,g8m.jpg,g8mb.jpg',4,'Urban'),(30,'Pantalón Oversized',28.25,9,'Pantalones','Negro Carbón','XL,XXL','p8f.jpg,p8b.jpg,p8m.jpg,p8mb.jpg',4,'Urban'),(31,'Sudadera Oversized',36.25,8,'Sudaderas','Negro Carbón','XL,XXL','s8f.jpg,s8b.jpg,s8m.jpg,s8mb.jpg',4,'Urban'),(32,'Camiseta Exclusive',19.00,0,'Camisetas','Blanco Roto','S,M,L','c9f.jpg,c9b.jpg,c9m.jpg,c9mb.jpg',5,'Minimal'),(33,'Gorra Exclusive',19.00,0,'Gorras','Blanco Roto','Única','g9f.jpg,g9b.jpg,g9m.jpg,g9mb.jpg',5,'Minimal'),(34,'Pantalón Exclusive',29.00,0,'Pantalones','Blanco Roto','S,M,L','p9f.jpg,p9b.jpg,p9m.jpg,p9mb.jpg',5,'Minimal'),(35,'Sudadera Exclusive',42.00,0,'Sudaderas','Blanco Roto','S,M,L','s9f.jpg,s9b.jpg,s9m.jpg,s9mb.jpg',3,'Minimal'),(36,'Camiseta Art Collection',14.99,0,'Camisetas','Morado','M,L,XL','c10f.jpg,c10b.jpg,c10m.jpg,c10mb.jpg',4,'Street'),(37,'Gorra Art Collection',14.99,0,'Gorras','Morado','Única','g10f.jpg,g10b.jpg,g10m.jpg,g10mb.jpg',4,'Street'),(38,'Pantalón Art Collection',24.99,0,'Pantalones','Morado','M,L,XL','p10f.jpg,p10b.jpg,p10m.jpg,p10mb.jpg',4,'Street'),(39,'Camiseta Daily',11.99,134,'Camisetas','Gris Claro','XS,S,M,L','c11f.jpg,c11b.jpg,c11m.jpg,c11mb.jpg',3,'Urban'),(40,'Gorra Daily',11.99,134,'Gorras','Gris Claro','Única','g11f.jpg,g11b.jpg,g11m.jpg,g11mb.jpg',3,'Urban'),(41,'Pantalón Daily',21.99,134,'Pantalones','Gris Claro','XS,S,M,L','p11f.jpg,p11b.jpg,p11m.jpg,p11mb.jpg',3,'Urban'),(42,'Sudadera Daily',24.99,134,'Sudaderas','Gris Claro','XS,S,M,L','s11f.jpg,s11b.jpg,s11m.jpg,s11mb.jpg',2,'Urban'),(43,'Camiseta Sport',16.45,167,'Camisetas','Azul Cielo','M,L,XL','c12f.jpg,c12b.jpg,c12m.jpg,c12mb.jpg',4,'Minimal'),(44,'Gorra Sport',16.45,167,'Gorras','Azul Cielo','Única','g12f.jpg,g12b.jpg,g12m.jpg,g12mb.jpg',4,'Minimal'),(45,'Pantalón Sport',26.45,167,'Pantalones','Azul Cielo','M,L,XL','p12f.jpg,p12b.jpg,p12m.jpg,p12mb.jpg',4,'Minimal'),(46,'Sudadera Sport',31.45,167,'Sudaderas','Azul Cielo','M,L,XL','s12f.jpg,s12b.jpg,s12m.jpg,s12mb.jpg',4,'Minimal'),(47,'Camiseta Modern',18.99,98,'Camisetas','Negro Mate','S,M','c13f.jpg,c13b.jpg,c13m.jpg,c13mb.jpg',5,'Street'),(48,'Gorra Modern',18.99,98,'Gorras','Negro Mate','Única','g13f.jpg,g13b.jpg,g13m.jpg,g13mb.jpg',5,'Street'),(49,'Pantalón Modern',28.99,98,'Pantalones','Negro Mate','S,M','p13f.jpg,p13b.jpg,p13m.jpg,p13mb.jpg',5,'Street'),(50,'Sudadera Modern',35.99,98,'Sudaderas','Negro Mate','S,M','s13f.jpg,s13b.jpg,s13m.jpg,s13mb.jpg',3,'Street'),(51,'Pantalón Relax',23.50,123,'Pantalones','Verde Bosque','L,XL,XXL','p14f.jpg,p14b.jpg,p14m.jpg,p14mb.jpg',3,'Urban'),(52,'Gorra Relax',13.50,123,'Gorras','Verde Bosque','Única','g14f.jpg,g14b.jpg,g14m.jpg,g14mb.jpg',3,'Urban'),(53,'Pantalón Basic',20.99,187,'Pantalones','Blanco Hueso','XS,S,M,L,XL','p15f.jpg,p15b.jpg,p15m.jpg,p15mb.jpg',4,'Minimal'),(54,'Gorra Basic',10.99,187,'Gorras','Blanco Hueso','Única','g15f.jpg,g15b.jpg,g15m.jpg,g15mb.jpg',4,'Minimal'),(55,'Pantalón Trendy',29.75,7,'Pantalones','Naranja','M,L,XL','p16f.jpg,p16b.jpg,p16m.jpg,p16mb.jpg',5,'Street'),(56,'Gorra Trendy',19.75,7,'Gorras','Naranja','Única','g16f.jpg,g16b.jpg,g16m.jpg,g16mb.jpg',5,'Street');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-02 14:57:54
