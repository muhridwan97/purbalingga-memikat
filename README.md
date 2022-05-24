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
  - size : number?,
  - page : number?
  - sortBy : string? = "created_at:ASC"
  
Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : {
    "items": [
      {
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
      },
      {
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
    ],
    "paging": {
      "item_per_page": 10,
      "page": 0,
      "total_item": 3,
      "total_page": 1
    },
    "sorting": {
      "key": "created_at",
      "direction": "ASC"
    }
        
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
- Endpoint : `/api/v1/carousels/{id_carousel}`
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

## Delete Carousel

Request:
- Method : DELETE
- Endpoint : `/api/v1/carousel/{id_carousel}`
- Header :
    - Accept : application/json

- Body :

```json
{
  "softDelete" : "Boolean?=true)",
  "deleted_by" : "integer"
}
```

Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : "string"
}
```





