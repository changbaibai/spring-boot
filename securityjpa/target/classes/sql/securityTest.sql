USE [master]
GO
/****** Object:  Database [springboot]    Script Date: 12/02/2019 15:30:28 ******/
CREATE DATABASE [springboot] ON  PRIMARY 
( NAME = N'springboot', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL10_50.SQLEXPRESS\MSSQL\DATA\springboot.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'springboot_log', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL10_50.SQLEXPRESS\MSSQL\DATA\springboot_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [springboot] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [springboot].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [springboot] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [springboot] SET ANSI_NULLS OFF
GO
ALTER DATABASE [springboot] SET ANSI_PADDING OFF
GO
ALTER DATABASE [springboot] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [springboot] SET ARITHABORT OFF
GO
ALTER DATABASE [springboot] SET AUTO_CLOSE OFF
GO
ALTER DATABASE [springboot] SET AUTO_CREATE_STATISTICS ON
GO
ALTER DATABASE [springboot] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [springboot] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [springboot] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [springboot] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [springboot] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [springboot] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [springboot] SET QUOTED_IDENTIFIER OFF
GO
ALTER DATABASE [springboot] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [springboot] SET  DISABLE_BROKER
GO
ALTER DATABASE [springboot] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [springboot] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [springboot] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [springboot] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [springboot] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [springboot] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [springboot] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [springboot] SET  READ_WRITE
GO
ALTER DATABASE [springboot] SET RECOVERY FULL
GO
ALTER DATABASE [springboot] SET  MULTI_USER
GO
ALTER DATABASE [springboot] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [springboot] SET DB_CHAINING OFF
GO
USE [springboot]
GO
/****** Object:  Table [dbo].[tb_user]    Script Date: 12/02/2019 15:30:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[tb_user](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[login_name] [varchar](255) NULL,
	[password] [varchar](255) NULL,
	[username] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[tb_teacher]    Script Date: 12/02/2019 15:30:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[tb_teacher](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[login_name] [varchar](255) NULL,
	[password] [varchar](255) NULL,
	[username] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[tb_student]    Script Date: 12/02/2019 15:30:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[tb_student](
	[id] [varchar](50) NULL,
	[username] [varchar](50) NULL,
	[jobs] [varchar](50) NULL,
	[phone] [varchar](50) NULL,
	[password] [varchar](50) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[tb_role]    Script Date: 12/02/2019 15:30:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[tb_role](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[authority] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[tb_user_role]    Script Date: 12/02/2019 15:30:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tb_user_role](
	[user_id] [bigint] NOT NULL,
	[role_id] [bigint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  ForeignKey [FK7vn3h53d0tqdimm8cp45gc0kl]    Script Date: 12/02/2019 15:30:29 ******/
ALTER TABLE [dbo].[tb_user_role]  WITH CHECK ADD  CONSTRAINT [FK7vn3h53d0tqdimm8cp45gc0kl] FOREIGN KEY([user_id])
REFERENCES [dbo].[tb_user] ([id])
GO
ALTER TABLE [dbo].[tb_user_role] CHECK CONSTRAINT [FK7vn3h53d0tqdimm8cp45gc0kl]
GO
/****** Object:  ForeignKey [FKea2ootw6b6bb0xt3ptl28bymv]    Script Date: 12/02/2019 15:30:29 ******/
ALTER TABLE [dbo].[tb_user_role]  WITH CHECK ADD  CONSTRAINT [FKea2ootw6b6bb0xt3ptl28bymv] FOREIGN KEY([role_id])
REFERENCES [dbo].[tb_role] ([id])
GO
ALTER TABLE [dbo].[tb_user_role] CHECK CONSTRAINT [FKea2ootw6b6bb0xt3ptl28bymv]
GO
