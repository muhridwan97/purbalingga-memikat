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
    (note: use table name ex. updated_at equal with updatedAt)
  
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

===============================
=

===============================
=

## Create Category

Request:
- Method : POST
- Endpoint : `/api/v1/categories`
- Header :
  - Content-Type : application/json
  - Accept : application/json
- Body :

```json
{
  "name" : "string",
  "slug" : "string",
  "file" : "multipart/form-data?",
  "link" : "string?",
  "description" : "string?",
  "isActive" : "boolean?=true",
  "isModule" : "boolean?=false",
  "moduleName" : "string?",
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
    "name" : "string",
    "photo" : "string",
    "slug" : "string",
    "order" : "integer",
    "isModule" : "boolean",
    "moduleName" : "string",
    "link" : "string",
    "isDeleted" : "boolean",
    "description" : "string",
    "isActive" : "boolean",
    "createdAt" : "date",
    "updatedAt" : "date",
    "deletedAt" : "date",
    "createdBy" : "integer",
    "updatedBy" : "integer",
    "deletedBy" : "integer"
  }
}
```

## List Category

Request:
- Method : GET
- Endpoint : `/api/v1/categories`
- Header :
  - Content-Type : application/json
- Query Param :
  - size : number?,
  - page : number?
  - sortBy : string? = "created_at:ASC"
    `note: use table name e.g updated_at equal with updatedAt .etc, 
    Except  order = the_order`

Response:

```json
{
  "code" : "number",
  "status" : "string",
  "data" : {
    "items": [
      {
        "id" : "integer, unique",
        "name" : "string",
        "photo" : "string",
        "slug" : "string",
        "order" : "integer",
        "isModule" : "boolean",
        "moduleName" : "string",
        "link" : "string",
        "isDeleted" : "boolean",
        "description" : "string",
        "isActive" : "boolean",
        "createdAt" : "date",
        "updatedAt" : "date",
        "deletedAt" : "date",
        "createdBy" : "integer",
        "updatedBy" : "integer",
        "deletedBy" : "integer"
      },
      {
        "id" : "integer, unique",
        "name" : "string",
        "photo" : "string",
        "slug" : "string",
        "order" : "integer",
        "isModule" : "boolean",
        "moduleName" : "string",
        "link" : "string",
        "isDeleted" : "boolean",
        "description" : "string",
        "isActive" : "boolean",
        "createdAt" : "date",
        "updatedAt" : "date",
        "deletedAt" : "date",
        "createdBy" : "integer",
        "updatedBy" : "integer",
        "deletedBy" : "integer"
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




