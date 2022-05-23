# API Spec

##Authentication

All API must use this authentication
Request :
- Header :
    - X-Api-Key : "Your secret api key"

## Create Carousel

Request:
- Method : POST
- Endpoint : `/api/v1/carousels`
- Header :
    - Content-Type : application/json
    - Accept : application/json
- Body :

```json
{
  "file" : "multipart/form-data",
  "link" : "string",
  "description" : "string",
  "is_active" : "integer",
  "created_by" : "integer"
}
```

Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : {
    "id" : "integer, unique",
    "photo" : "string",
    "link" : "string",
    "description" : "string",
    "is_active" : "integer",
    "created_at" : "date",
    "updated_at" : "date",
    "deleted_at" : "date",
    "created_by" : "integer",
    "updated_by" : "integer",
    "deleted_by" : "integer"
  }
}
```

## List Carousel

Request:
- Method : GET
- Endpoint : `/api/v1/carousels`
- Header :
  - Content-Type : application/json
- Query Param :
  - size : number,
  - page : number
  
Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : {
    "id" : "integer, unique",
    "photo" : "string",
    "link" : "string?",
    "description" : "string?",
    "is_active" : "integer",
    "created_at" : "date",
    "updated_at" : "date",
    "deleted_at" : "date",
    "created_by" : "integer",
    "updated_by" : "integer",
    "deleted_by" : "integer"
  }
}
```

## Get Carousel

Request:
- Method : GET
- Endpoint : `/api/v1/carousels/{id_carousel}`
- Header :
    - Content-Type : application/json

Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : {
    "id" : "integer, unique",
    "photo" : "string",
    "link" : "string?",
    "description" : "string?",
    "is_active" : "integer",
    "created_at" : "date",
    "updated_at" : "date",
    "deleted_at" : "date",
    "created_by" : "integer",
    "updated_by" : "integer",
    "deleted_by" : "integer"
  }
}
```

## Update Carousel

Request:
- Method : PATCH
- Endpoint : `/api/carousels/{id_carousel}`
- Header :
    - Content-Type : application/json
    - Accept : application/json
- Body :

```json
{
  "photo" : "string",
  "link" : "string",
  "description" : "string",
  "is_active" : "integer",
  "updated_by" : "integer"
}
```

Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : {
    "id" : "integer, unique",
    "photo" : "string",
    "link" : "string",
    "description" : "string",
    "is_active" : "integer",
    "created_at" : "date",
    "updated_at" : "date",
    "deleted_at" : "date",
    "created_by" : "integer",
    "updated_by" : "integer",
    "deleted_by" : "integer"
  }
}
```
## List Carousel

Request:
- Method : GET
- Endpoint : `/api/carousels`
- Header :
    - Accept : application/json
- Query Param :
    - size : number,
    - page : number

Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : [
    {
      "id" : "integer, unique",
      "photo" : "string",
      "link" : "string",
      "description" : "string",
      "is_active" : "integer",
      "created_at" : "date",
      "updated_at" : "date",
      "deleted_at" : "date",
      "created_by" : "integer",
      "updated_by" : "integer",
      "deleted_by" : "integer"
    },{
      "id" : "integer, unique",
      "photo" : "string",
      "link" : "string",
      "description" : "string",
      "is_active" : "integer",
      "created_at" : "date",
      "updated_at" : "date",
      "deleted_at" : "date",
      "created_by" : "integer",
      "updated_by" : "integer",
      "deleted_by" : "integer"
    }
  ]
}
```
## Delete Carousel

Request:
- Method : DELETE
- Endpoint : `/api/carousel/{id_carousel}`
- Header :
    - Accept : application/json

- Body :

```json
{
  "deleted_by" : "integer"
}
```

Response:

```json
{
  "code" : "number",
  "status" : "string"
}
```





