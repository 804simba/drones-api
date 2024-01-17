# Drones API

## Table of Contents

- [Introduction](#introduction)
- [Requirements](#requirements)
- [Setup](#setup)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Database Setup](#database-setup)
- [Usage](#usage)
    - [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Briefly describe the purpose and functionality of the Drones API.

## Requirements

- Java 17
- Docker

## Setup

### Prerequisites

Ensure you have the following software installed:

- Java 17
- Docker

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/drones-api.git
2. Change into project directory:
   ```bash
   cd drones-api
3. Build Project
   ```bash
   ./mvnw clean install

### Running with Docker

1. Build the Docker image

   ```bash
   docker build -t drones-api .
   
2. Run the Docker container

   ```bash
   docker run -p 8080:8080 drones-api

### Usage
[Swagger] (URL http://localhost:8080/swagger-ui/index.html?urls.primaryName=Users)

### Payload Structure
TODO: add payload structure

#### Drone Operations
Register a drone:
- Endpoint: `/api/v1/drones/register`
- Method: `POST`
- Consumes: `application/json`
- Produces: `application/json`

```bash
   request:
    {
        "serial_number": "mufasa",(100 characters max)
        "model": "Lightweight", (Lightweight, Middleweight, Cruiseweight, Heavyweight)
        "weight_limit": 300, (500gr max)
        "battery_capacity": 100 (100% max)
    }
    response:
    {
        "responseCode": 200,
        "responseMessage": "drone registered successfully",
        "response_time": "2024-01-15 12:49:20",
        "data": {
            "id": 1,
            "serialNumber": "mufasa",
            "model": "LIGHTWEIGHT",
            "weightLimit": 300,
            "batteryLevel": 100,
            "state": "IDLE",
            "medications": null
    }
}
    
```

Load a drone with medication:
- Endpoint: `/api/v1/drones/{droneId}/load`
- Method: `POST`
- Consumes: `application/json`
- Produces: `application/json`

```bash
    request:
    {
        "drone_serial_number": "mufasa",(100 characters max)
        "medication_name": "aIzv-5WET_",(allowed only letters, numbers, "-", "_")
        "medication_weight": 10.0,
        "medication_code": "LLENYATE1I_NP1HICGOSa",(allowed only upper case letters, underscore, numbers, "-", "_")
        "medication_image_id": 1
    }
    response:
    {
        "responseCode": 200,
        "responseMessage": "drone loaded successfully",
        "response_time": "2024-01-15 12:49:24",
        "data": {
            "id": 1,
            "serialNumber": "mufasa",
            "model": "string",
            "weightLimit": 400,
            "batteryLevel": 100,
            "state": "IDLE",
            "medications": []
    }
}
    
```

Get loaded drone medications:
- Endpoint: `/api/v1/drones/{droneId}/loaded-medications`
- Method: `GET`
- Consumes: `application/json`
- Produces: `application/json`

```bash
    response:
   {
    "responseCode": 200,
    "responseMessage": "fetched medications records successfully for drone with id 1",
    "response_time": "2024-01-14 21:21:43",
    "data": {
        "drone_id": 1,
        "drone_serial_number": "simba",
        "medications": [
            {
                "medication_id": 1,
                "medication_name": "aIzv-5WET_",
                "medication_weight": 10,
                "medication_code": "LLENYATE1I_NP1HICGOSa",
                "medication_image": "https://www.dummy.com/upload/v1705399096/img2024-01-16T10:58:12.738839.jpg"
            }
        ]
    }
  }
}
    
```

Get available drones for loading:
- Endpoint: `/api/v1/drones/available-for-loading`
- Method: `GET`
- Consumes: `application/json`
- Produces: `application/json`

```bash
    response:
   {
    "responseCode": 200,
    "responseMessage": "fetched available drones successfully",
    "response_time": "2024-01-15 12:49:34",
    "data": {
        "status": "success",
        "status_code": 200,
        "message": "fetched available drones",
        "response_time": "2024-01-15 12:49:34",
        "data": {
            "content": [
                {
                    "id": 1,
                    "serialNumber": "mufasa",
                    "model": "LIGHTWEIGHT",
                    "weightLimit": 400,
                    "batteryLevel": 100,
                    "state": "IDLE",
                    "medications": [
                        {
                            "medication_id": 1,
                            "medication_name": "panadol",
                            "medication_weight": 12,
                            "medication_code": "HKJK5IJ",
                            "medication_image": "lorem"
                        }
                    ]
                }
            ],
            "metadata": {
                "page_number": 0,
                "page_size": 30,
                "total_pages": 1,
                "total_elements": 1
            }
        }
    }
  }
    
```

Get drone battery level:
- Endpoint: `/api/v1/drones/{droneId}/battery-level`
- Method: `GET`
- Consumes: `application/json`
- Produces: `application/json`

```bash 
  response:
    {
    "responseCode": 200,
    "responseMessage": "success",
    "response_time": "2024-01-15 12:51:21",
    "data": {
        "drone_serial_number": "mufasa",
        "battery_level": 100
    }
  }
```

#### File Upload Service
- Endpoint: `/api/v1/files/upload`
- Method: `POST`
- Request Type: Multipart Form Data
- Consumes: `multipart/form-data`
- Produces: `application/json`

```bash 
      {
        "responseCode": 200,
        "responseMessage": "file upload successful",
        "response_time": "2024-01-16 10:58:16",
        "data": {
            "media_id": 1,
            "image_url": "http://res.cloudinary.com/dgprtrm4b/image/upload/v1705399096/img2024-01-16T10:58:12.738839.jpg",
            "media_filename": "file",
            "cloudinary_public_id": "img2024-01-16T10:58:12.738839",
            "media_type": "jpg"
      }
}
```

## Testing

   ```bash
   ./mvnw test
