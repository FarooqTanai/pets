# Pets Service API
This service provides RESTful endpoints to manage pets. It supports creating, retrieving, updating, and deleting pets, along with listing them with pagination.

##  Database Setup (Mysql)
This application uses Mysql. Before running the service for the first time, make sure your Mysql has the correct database and user credentials.

You can create them using the following command:

CREATE DATABASE IF NOT EXISTS petdb;
CREATE USER 'anemone'@'%' IDENTIFIED BY 'anemone';
GRANT SELECT, INSERT, UPDATE, DELETE ON petdb.* TO 'anemone'@'%';
FLUSH PRIVILEGES;


Alternatively, update the credentials in your `application.properties` file.

---
## Base Configuration

- **Base URL**: `/api/v1/pets`
- **Application Port**: `9999`

---
## Endpoints

### 1. Get Pet by ID

- **Method**: `GET`
- **URL**: `/api/v1/pets/{id}`
- **Description**: Retrieve a single Pet using its ID.

#### Example Response
{
"id": 136,
"name": "elli",
"species": "CAT",
"age": 10,
"owner_name": "farooq",
"created_at": "2025-05-14T14:48:39.635350Z",
"last_modified": "2025-05-14T14:48:39.635350Z"
}

---
### 2. Get All Pets (Paginated)

- **Method**: `GET`
- **URL**: `/api/v1/pets`
- **Description**: Returns a paginated list of pets.

#### Example Response
{
"content": [{ /* PET JSON */ }],
"totalElements": 5,
"totalPages": 1,
"number": 0,
"size": 10
}

---
### 3. Create pet

- **Method**: `POST`
- **URL**: `/api/v1/pets`
- **Description**: Creates a new pet.

#### Request Body
{
"name": "elli",
"species": "CAT",
"age": 10,
"owner_name": "farooq"
}

#### Response
- `200 OK` with the created pet.

---
### 4. Update Pet

- **Method**: `PUT`
- **URL**: `/api/v1/pets/{id}`
- **Description**: Updates an existing pet.

#### Request Body
{
"name": "mia-mia",
"species": "CAT",
"age": 3,
"owner_name": "Tanai"
}

#### Response
- `200 OK` with the updated pet.

---
### 5. Delete pet

- **Method**: `DELETE`
- **URL**: `/api/v1/pets/{ad_id}`
- **Description**: Deletes the specified pet.

#### Response
204 No Content


---
## Notes
- Pagination defaults to page `0` and size `10`.
- species can be one of (CAT, DOG, MONKEY, MOUSE, RABBIT, FISH), defined by the `species` enum field.
