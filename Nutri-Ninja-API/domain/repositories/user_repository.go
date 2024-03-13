package repositories

import "Nutri-Ninja-API/domain/entities"

type UserRepository interface {
	AllUsers() ([]entities.User, error)
	UserByID(id uint) (*entities.User, error)
	AllUserIDs() ([]uint, error)
	Register(user entities.User) (entities.User, error)
	Login(loginData entities.LoginRequest) (entities.LoginResponse, error)
	UpdateUser(id uint, user entities.User) (entities.User, error)
	DeleteUser(id uint) (entities.User, error)
}
