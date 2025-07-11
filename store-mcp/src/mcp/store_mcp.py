from typing import Any
import httpx
from mcp.server.fastmcp import FastMCP
import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
import service.store_query as sq

mcp = FastMCP("store")

@mcp.tool()
def ping():
    """
    Ping the fake store api
    Returns:
        A string
    """ 
    return sq.ping()

@mcp.tool()
def get_tools_list():
    """
    Get the list of tools available in the fake store api
    Returns:
        A list of tool objects (dict)
    """
    return sq.get_tools_list()

@mcp.tool()
async def get_products():
    """
    Get all products from the fake store api
    Returns:
        A list of product objects (dict)
    """
    return await sq.get_products()

@mcp.tool()
async def get_product(product_id: int):
    """
    Get a specificproduct from the fake store api
    Args:
        product_id: The id of the product to get (int)
    Returns:
        A product object (dict)
    """
    return await sq.get_product(product_id)

@mcp.tool()
async def get_categories():
    """
    Get all categories from the fake store api
    Returns:
        A list of category objects (dict)
    """
    return await sq.get_categories()

@mcp.tool()
async def get_products_by_category(category: str):
    """
    Get all products by category from the fake store api
    Args:
        category: The category to get products from (str)
    Returns:
        A list of product objects (dict)
    """
    return await sq.get_products_by_category(category)

@mcp.tool()
async def get_carts():
    """
    Get all carts from the fake store api
    Returns:
        A list of cart objects (dict)
    """
    return await sq.get_carts()

@mcp.tool()
async def get_cart(cart_id: int):
    """
    Get a cart from the fake store api
    Args:
        cart_id: The id of the cart to get (int)
    Returns:
        A cart object (dict)
    """
    return await sq.get_cart(cart_id)

@mcp.tool()
async def get_user_cart(user_id: int):
    """
    Get a user's cart from the fake store api
    Args:
        user_id: The id of the user to get the cart from (int)
    Returns:
        A cart object (dict)
    """
    return await sq.get_user_cart(user_id)

@mcp.tool()
async def get_users():
    """
    Get all users from the fake store api
    Returns:
        A list of user objects (dict)
    """
    return await sq.get_users()

@mcp.tool()
async def get_user(user_id: int):
    """
    Get a specific user from the fake store api
    Args:
        user_id: The id of the user to get (int)
    Returns:
        A user object (dict)
    """
    return await sq.get_user(user_id)

@mcp.tool()
async def login(username: str, password: str):
    """
    Login to the fake store api
    Args:
        username: The username to login with (str)
        password: The password to login with (str)
    Returns:
        A token (str)
    """
    return await sq.login(username, password)

if __name__ == "__main__":
    mcp.run(transport='sse')