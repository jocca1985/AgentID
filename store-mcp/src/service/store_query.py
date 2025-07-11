import httpx
import asyncio
import json

## Base URL for the Fake Store API
# FAKE_STORE_API = "http://localhost:6400" #-- For if we want to use a self hosted store with fake products and our own mongodb database
FAKE_STORE_API = "https://fakestoreapi.com" #-- Fake store api can be used online with predefined products and data

client = httpx.AsyncClient()  # Create a single client

def ping_handler():
    # Tools
    try:    
        response = httpx.get(f"{FAKE_STORE_API}/products")
        response.raise_for_status()
        return "pong"
    except Exception as e:
        raise Exception("Store is down") from e

def get_tools_list_handler():
    return json.dumps({
        "tools": [
            {
                "name": "get-products",
                "description": "Get all products from the store",
                "parameters": {}
            },
            {
                "name": "get-product",
                "description": "Get a specific product by ID",
                "parameters": {
                    "product_id": "integer"
                }
            },
            {
                "name": "get-categories",
                "description": "Get all product categories",
                "parameters": {}
            },
            {
                "name": "get-products-by-category",
                "description": "Get products by category",
                "parameters": {
                    "category": "string"
                }
            },
            {
                "name": "get-carts",
                "description": "Get all carts",
                "parameters": {}
            },
            {
                "name": "get-cart",
                "description": "Get a specific cart by ID",
                "parameters": {
                    "cart_id": "integer"
                }
            },
            {
                "name": "get-user-cart",
                "description": "Get a user's cart",
                "parameters": {
                    "user_id": "integer"
                }
            },
            {
                "name": "get-users",
                "description": "Get all users",
                "parameters": {}
            },
            {
                "name": "get-user",
                "description": "Get a specific user by ID",
                "parameters": {
                    "user_id": "integer"
                }
            },
            {
                "name": "login",
                "description": "Login with username and password",
                "parameters": {
                    "username": "string",
                    "password": "string"
                }
            }
        ]
    }, indent=2)


## Fake storee getter functions
async def get_products_handler():
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{FAKE_STORE_API}/products")
            response.raise_for_status()
            return json.dumps(response.json(), indent=2)
    except Exception as e:
        return f"Error fetching products: {str(e)}"

async def get_product_handler(product_id: int):
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{FAKE_STORE_API}/products/{product_id}")
            response.raise_for_status()
            return json.dumps(response.json(), indent=2)
    except Exception as e:
        return f"Error fetching product {product_id}: {str(e)}"

async def get_categories_handler():
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{FAKE_STORE_API}/products/categories")
            response.raise_for_status()
            return json.dumps(response.json(), indent=2)
    except Exception as e:
        return f"Error fetching categories: {str(e)}"

async def get_products_by_category_handler(category: str):
    try:
        response = await client.get(f"{FAKE_STORE_API}/products/category/{category}")
        response.raise_for_status()
        return json.dumps(response.json(), indent=2)
    except Exception as e:
        raise e

# Cart Tools
async def get_carts_handler():
    try:
        response = await client.get(f"{FAKE_STORE_API}/carts")
        response.raise_for_status()
        return json.dumps(response.json(), indent=2)
    except Exception as e:
        raise e

async def get_cart_handler(cart_id: int):
    try:
        response = await client.get(f"{FAKE_STORE_API}/carts/{cart_id}")
        response.raise_for_status()
        return json.dumps(response.json(), indent=2)
    except Exception as e:
        raise e

async def get_user_cart_handler(user_id: int):
    try:
        response = await client.get(f"{FAKE_STORE_API}/carts/user/{user_id}")
        response.raise_for_status()
        return json.dumps(response.json(), indent=2)
    except Exception as e:
        raise e

# User Tools
async def get_users_handler():
    try:
        response = await client.get(f"{FAKE_STORE_API}/users")
        response.raise_for_status()
        return json.dumps(response.json(), indent=2)
    except Exception as e:
        raise e

async def get_user_handler(user_id: int):
    try:
        response = await client.get(f"{FAKE_STORE_API}/users/{user_id}")
        response.raise_for_status()
        return json.dumps(response.json(), indent=2)
    except Exception as e:
        raise e

# Auth Tools
async def login_handler(username: str, password: str):
    try:
        response = await client.post(
            f"{FAKE_STORE_API}/auth/login",
            json={"username": username, "password": password}
        )
        response.raise_for_status()
        return json.dumps(response.json(), indent=2)
    except Exception as e:
        raise e