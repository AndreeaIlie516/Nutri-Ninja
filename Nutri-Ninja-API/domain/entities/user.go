package entities

import (
	"gorm.io/gorm"
	"time"
)

type User struct {
	gorm.Model
	Username      string     `gorm:"column:username;unique;not null" json:"username" binding:"required" validate:"required,usernameValidator"`
	Password      string     `gorm:"column:password;not null" json:"password" binding:"required" validate:"required,passwordValidator"`
	FirstName     string     `gorm:"column:first_name;not null" json:"first_name" binding:"required" validate:"required,nameValidator"`
	LastName      string     `gorm:"column:last_name;not null" json:"last_name" binding:"required" validate:"required,nameValidator"`
	Email         string     `gorm:"column:email;unique;not null" json:"email" binding:"required" validate:"required,email"`
	DateOfBirth   time.Time  `gorm:"column:date_of_birth;not null" json:"date_of_birth" binding:"required"`
	Gender        string     `gorm:"column:gender;not null" json:"gender" binding:"required"`
	Height        int        `gorm:"column:height;not null" json:"height" binding:"required"`
	Weight        float32    `gorm:"column:weight;not null" json:"weight" binding:"required"`
	ActivityLevel string     `gorm:"column:activity_level;not null" json:"activity_level" binding:"required"`
	Objective     string     `gorm:"column:objective;not null" json:"objective" binding:"required"`
	CaloriesGoal  int        `gorm:"column:calories_goal;not null" json:"calories_goal" binding:"required"`
	ProteinsGoal  int        `gorm:"column:proteins_goal;not null" json:"proteins_goal" binding:"required"`
	CarbsGoal     int        `gorm:"column:carbs_goal;not null" json:"carbs_goal" binding:"required"`
	FatsGoal      int        `gorm:"column:fats_goal;not null" json:"fats_goal" binding:"required"`
	Role          AccessType `gorm:"column:access_type,type:tinyint;not null"`
}

type LoginRequest struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

type LoginResponse struct {
	Email string `json:"email"`
	Jwt   string `json:"jwt"`
}

type AccessType uint8

const (
	NormalUser AccessType = iota
	Admin
)
