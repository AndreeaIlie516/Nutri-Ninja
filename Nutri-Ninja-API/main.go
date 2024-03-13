package main

import (
	"Nutri-Ninja-API/database"
	"Nutri-Ninja-API/domain/entities"
	"Nutri-Ninja-API/domain/services"
	"Nutri-Ninja-API/infrastructure/dataaccess"
	"Nutri-Ninja-API/infrastructure/middlewares"
	"Nutri-Ninja-API/presentation/handlers"
	"Nutri-Ninja-API/routes"
	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"log"
)

func main() {
	router := gin.Default()

	if err := godotenv.Load(); err != nil {
		log.Fatal("Error loading .env file")
	}

	db := database.ConnectDB()

	entitiesToMigrate := []interface{}{
		&entities.User{},
	}

	for _, entity := range entitiesToMigrate {
		err := db.AutoMigrate(entity)
		if err != nil {
			log.Fatalf("Failed to migrate database: %v", err)
		}
	}

	authMiddleware := middlewares.AuthMiddleware{}

	userRepository := dataaccess.NewGormUserRepository(db)

	userService := services.UserService{Repo: userRepository}

	userHandler := handlers.UserHandler{Service: &userService}

	routes.RegisterUserRoutes(router, &userHandler, authMiddleware)

	err := router.Run("localhost:8080")
	if err != nil {
		log.Fatalf("Failed to run server: %v", err)
		return
	}
}
