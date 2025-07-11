from fastapi import FastAPI, HTTPException, APIRouter
from pydantic import BaseModel
from typing import List, Optional, Dict, Any
import requests
import uvicorn
import httpx
import src.service.store_query as sq

app = APIRouter()

## Base URL for the Fake Store API
# FAKE_STORE_API = "http://localhost:6400" #-- For if we want to use a self hosted store with fake products and our own mongodb database
FAKE_STORE_API = "https://fakestoreapi.com" #-- Fake store api can be used online with predefined products and data

## MCP Protocol Endpoints
@app.get("/sse")
async def tools_list():
    # MCP Protocol's endpoint for tool discovery
    try:
        return await sq.get_tools_list()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    # MCP Protocol Health Check
    try:
        # Test the Fake Store API connection
        return await sq.ping()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

## Product Tools
@app.post("/tools/get-products")
async def get_products():
    try:
        return await sq.get_products()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/tools/get-product")
async def get_product(product_id: int):
    try:
        return await sq.get_product(product_id)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/tools/get-categories")
async def get_categories():
    try:
        return await sq.get_categories()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/tools/get-products-by-category")
async def get_products_by_category(category: str):
    try:
        return await sq.get_products_by_category(category)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# Cart Tools
@app.post("/tools/get-carts")
async def get_carts():
    try:
        return await sq.get_carts()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/tools/get-cart")
async def get_cart(cart_id: int):
    try:
        return await sq.get_cart(cart_id)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/tools/get-user-cart")
async def get_user_cart(user_id: int):
    try:
        return await sq.get_user_cart(user_id)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# User Tools
@app.post("/tools/get-users")
async def get_users():
    try:
        return await sq.get_users()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/tools/get-user")
async def get_user(user_id: int):
    try:
        return await sq.get_user(user_id)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# Auth Tools
@app.post("/tools/login")
async def login(username: str, password: str):
    try:
        return await sq.login(username, password)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run(
        "store_mcp:app",
        host="0.0.0.0",
        port=5028,
        reload=True
    )

