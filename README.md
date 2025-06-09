# planning-poker
The Technologies in Software Development: Planning Poker Project of the Erasmus GOATs.

- Backend (Java + Spring Boot + PostgreSQL) running inside Docker
- Frontend (React)

---

## Start Backend

* **Build the backend jar:**

  ```bash
  ./gradlew clean build
  ```

* **Build the Docker image:**

  ```bash
  docker build -t your-backend-image-name .
  ```

* **Run Docker container:**

  ```bash
  docker-compose up
  ```

---

## Start Frontend

* **Running React app:**

  ```bash
  npm install
  npm start
  ```

* This starts React on `http://localhost:3000`.
