# 📝 ToDo List API

## Description
This is a backend API for a Todo List application. Users can create multiple todo list catalogs, manage todo list entries and assign tags for filtering. 
The API is secured using JWT authentication.

---

## ⚙️ How to use

### ▶️ Run Locally
1. Clone the repository:
```bash
git clone https://github.com/yourusername/todo-api.git
```
2. Create an environment file
3. Configure environment variables in .env:
```env
TODO_USER=root
TODO_PASSWORD=yourpassword

JWT_SECRET=THIS_IS_A_32_CHAR_MINIMUM_SECRET_KEY_PUT_YOURS_HERE
```
4. Run the application with Maven:
```bash
mvn spring-boot:run
```

### ▶️ Run with Docker
1. Make sure Docker is installed.
2. Build and start containers:
```bash
docker compose up --build
```

---

## 🧩 Structure

### Class Diagram
![Class Diagram](todoStructure.png)

### Endpoints
Endpoints will be documented here.

---

## 🧪 Testing
Run all tests using Maven:
```bash
mvn test
```
---

## Branch Naming Policy
To maintain a clean and organized Git workflow, all branches must follow a standard naming convention.
The `main` branch is protected and should not be uesed for direct development.

### ✔️ Allowed Patterns 
- `feature/<description>` - new features
- `task/<description>` – smaller tasks
- `fix/<description>` – minor code fixes or adjustments
- `bug/<description>` – bug fixes

### Example of Valid Branch Names
- `feature/initial-setup`
- `task/add-readme`
- `fix/code-formatting`
- `bug/login-error`
